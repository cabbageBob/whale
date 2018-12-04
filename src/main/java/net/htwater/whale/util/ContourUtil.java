package net.htwater.whale.util;

import net.htwater.whale.entity.RainStation;
import net.htwater.whale.entity.StationRain;
import net.htwater.whale.entity.VacuatePosition;

import org.apache.commons.lang.StringUtils;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.StationData;
import org.meteoinfo.data.mapdata.MapDataManage;
import org.meteoinfo.data.meteodata.DrawMeteoData;
import org.meteoinfo.data.meteodata.GridDataSetting;
import org.meteoinfo.geoprocess.analysis.InterpolationMethods;
import org.meteoinfo.geoprocess.analysis.InterpolationSetting;
import org.meteoinfo.layer.VectorLayer;
import org.meteoinfo.layout.MapLayout;
import org.meteoinfo.legend.LegendManage;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.MapFrame;
import org.meteoinfo.shape.ShapeTypes;
import org.meteoinfo.map.MapView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
/**
 * @author shitianlong
 * @since 2018-9-1
 */
@Component
public class ContourUtil {
    private static String TEMPLATES_FILE_PATH="";
    @Value("${templates.path}")
    public void setTemplatesFilePath(String templatesFilePath) {
        if(!StringUtils.isBlank(templatesFilePath)) {
            TEMPLATES_FILE_PATH = templatesFilePath;
        }
    }
    public static final int CONTOUR_POLYGON_JSON=1;
    public static final int CONTOUR_POLYLINE_JSON=2;
    public static final int CONTOUR_POLYGON_PNG=3;
    private static final double DEGREE_TO_METER=Math.PI*6378137/180.0;
    private static final double INVALID_VALUE=-999999.0;
    private static final double GRID_DELTA=0.02;
    private static final int BUFFER_SIZE=new Double(1000 * 1.0e7 / DEGREE_TO_METER).intValue();
    private static final String PRECIPITATION_UNIT="mm";
    private static final String VALUE_FIELD="Data";
    private static final String[] SUFFIXES={".shp",".shx",".prj",".dbf",".png"};
    private static final boolean SMOOTH_SHAPE=true;
    private static final boolean HAS_NO_DATA=false;
    /**
     * draw contour polyline or polygon
     * @param district clip layer id
     * @param breaks interpolate break
     * @param type 1:polygon 2:polyline 3:polygon image
     * @param duration datetime util
     * @return type 1&2 return geojson||type 3 return png file path
     */
    public static String drawContourFeature(String district,StationData stationData,double[] breaks, Color[] colors,int type,int duration){
        String fileName=System.currentTimeMillis()+"-"+UUID.randomUUID().toString();
        String tmpFilePath=TEMPLATES_FILE_PATH+fileName;
        String clipFilePath=TEMPLATES_FILE_PATH+district+SUFFIXES[0];
        String result="";
        VectorLayer clipLayer = null;
        GridDataSetting gridDataSetting=new GridDataSetting();
        try {
            clipLayer = MapDataManage.readMapFile_ShapeFile(clipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gridDataSetting.dataExtent = clipLayer.getExtent();
        stationData.projInfo = clipLayer.getProjInfo();
        gridDataSetting.xNum = 2000;
        gridDataSetting.yNum = 2000;
        InterpolationSetting interSet = new InterpolationSetting();
        interSet.setGridDataSetting(gridDataSetting);
        interSet.setInterpolationMethod(InterpolationMethods.IDW_Radius);
        interSet.setRadius(2);
        interSet.setMinPointNum(1);
        GridData gridData = stationData.interpolateData(interSet);
        if(colors.length==0){
            colors=LegendManage.createRandomColors(breaks.length);
        }
        double[] extreme = new double[2];
        boolean hasUndef = gridData.getMaxMinValue(extreme);
        LegendScheme legendScheme =LegendManage.createGraduatedLegendScheme(breaks, colors
                , ShapeTypes.Polygon,extreme[1],extreme[0],HAS_NO_DATA,INVALID_VALUE);
        if(type==CONTOUR_POLYGON_JSON||type==CONTOUR_POLYLINE_JSON){
            VectorLayer contourLayer = (type==CONTOUR_POLYGON_JSON)?
                DrawMeteoData.createShadedLayer(gridData,legendScheme, PRECIPITATION_UNIT, VALUE_FIELD,SMOOTH_SHAPE)
               :DrawMeteoData.createContourLayer(gridData,legendScheme, PRECIPITATION_UNIT, VALUE_FIELD,SMOOTH_SHAPE);
            VectorLayer lastLayer = contourLayer.clip(clipLayer);
            lastLayer.setProjInfo(clipLayer.getProjInfo());
            lastLayer.saveFile(tmpFilePath+SUFFIXES[0]);
            result= GisUtil.ShapeToGeoJSON(tmpFilePath+SUFFIXES[0]);
            List<String> list=Arrays.asList(SUFFIXES);
            for(int i=0;i<SUFFIXES.length-1;i++){
                FileUtil.deleteFile(tmpFilePath+SUFFIXES[i]);
            }
        }else if(type==CONTOUR_POLYGON_PNG){
            String pictureName=DateTimeUtil.getContourPictureNameByDuration(duration)+SUFFIXES[4];
            String picturePath=TEMPLATES_FILE_PATH+pictureName;
            tmpFilePath=TEMPLATES_FILE_PATH+UUID.randomUUID().toString()+SUFFIXES[4];
            //add map container
            MapLayout maplayout=new MapLayout();
            maplayout.setSize(1764, 1600);
            maplayout.setBackground(new Color(255, 255, 255, 0));
            MapFrame mapFrame=maplayout.getActiveMapFrame();
            MapView mapView =mapFrame.getMapView();
            VectorLayer contourLayer = DrawMeteoData.createShadedLayer(gridData, legendScheme, PRECIPITATION_UNIT
                    , VALUE_FIELD,true);
            VectorLayer lastLayer = contourLayer.clip(clipLayer);
            lastLayer.setProjInfo(clipLayer.getProjInfo());
            //export page size setting
            mapView.addLayer(clipLayer);
            mapView.addLayer(lastLayer);
            mapView.setGridXDelt(GRID_DELTA);
            mapView.setGridYDelt(GRID_DELTA);
            maplayout.setPageBounds(new Rectangle(0,0,1764,1600));
            maplayout.getActiveLayoutMap().setLeft(0);
            maplayout.getActiveLayoutMap().setTop(0);
            maplayout.getActiveLayoutMap().setWidth(1764);
            maplayout.getActiveLayoutMap().setHeight(1600);
            //export image and polish up image
            try {
                maplayout.exportToPicture(tmpFilePath);
                boolean re=transferAlpha2File(tmpFilePath,picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtil.deleteFile(tmpFilePath);
            result=pictureName;
        }
        return result;
    }
    /**
     * vacuate points according distance
     * @author shitianlong
     * @param stations all station
     * @param km unit of distance
     * @return List<RainStation>
     */
    public static List<RainStation> vacuateStations(List<RainStation> stations,int km){
        List<RainStation> result=new ArrayList<>();
        List<VacuatePosition> positions=new LinkedList<>();
        for(RainStation station:stations){
            int x=new Double(station.getLgtd()*1e7).intValue();
            int y=new Double(station.getLttd()*1e7).intValue();
            VacuatePosition position=new VacuatePosition(x,y,km*BUFFER_SIZE);
            if(positions.contains(position)){
                station.setSelected(false);
            }else{
                positions.add(position);
                station.setSelected(true);
            }
            result.add(station);
        }
        return result;
    }
    /**
     * vacuate points according distance
     * @author shitianlong
     * @param stations all station
     * @param km unit of distance
     * @return List<StationRain>
     */
    public static List<StationRain> recommendStations(List<StationRain> stations, int km){
        if(stations.size()<4){
            return stations;
        }else{
            List<StationRain> result=new ArrayList<>();
            List<VacuatePosition> positions=new LinkedList<>();
            for (int i = 0; i <3 ; i++) {
                result.add(stations.get(i));
            }
            for(StationRain station:stations){
                int x=new Double(station.getLgtd()*1e7).intValue();
                int y=new Double(station.getLttd()*1e7).intValue();
                VacuatePosition position=new VacuatePosition(x,y,km*BUFFER_SIZE);
                if(!positions.contains(position)){
                    positions.add(position);
                    result.add(station);
                }
            }
            return result;
        }
    }
    /**
     * remove white and black background,make it transparent
     * @param imgSrc source file path
     * @param resultSrc result file path
     * @return boolean
     */
    private static boolean transferAlpha2File(String imgSrc,String resultSrc) {
        File file = new File(imgSrc);
        InputStream is = null;
        boolean result;
        try {
            is = new FileInputStream(file);
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(is));
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int r= (rgb & 0xff0000) >> 16;
                    int g= (rgb & 0xff00) >> 8;
                    int b = (rgb & 0xff);
                    if (((255 - r) < 30) && ((255 - g) < 30) && ((255 - b) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    if ((r<10) && (g < 10) && (b < 10)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    if ((r==255) && (g == 251) && (b ==195)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            result = ImageIO.write(bufferedImage, "png", new File(resultSrc));
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    /**
     * draw contour polyline or polygon
     * @param district clip layer id
     * @param breaks interpolate break
     * @param type 1:polygon 2:polyline 3:polygon image
     * @param duration datetime util
     * @return type 1&2 return geojson||type 3 return png file path
     */
    public static String drawContourFeatureTemp(String district,StationData stationData,double[] breaks, Color[] colors,int type,int duration){
        String fileName=System.currentTimeMillis()+"-"+UUID.randomUUID().toString();
        String tmpFilePath=TEMPLATES_FILE_PATH+fileName;
        String clipFilePath=TEMPLATES_FILE_PATH+district+SUFFIXES[0];
        String result="";
        VectorLayer clipLayer = null;
        GridDataSetting gridDataSetting=new GridDataSetting();
        try {
            clipLayer = MapDataManage.readMapFile_ShapeFile(clipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gridDataSetting.dataExtent = clipLayer.getExtent();
        stationData.projInfo = clipLayer.getProjInfo();
        gridDataSetting.xNum = 2000;
        gridDataSetting.yNum = 2000;
        InterpolationSetting interSet = new InterpolationSetting();
        interSet.setGridDataSetting(gridDataSetting);
        interSet.setInterpolationMethod(InterpolationMethods.IDW_Radius);
        interSet.setRadius(2);
        interSet.setMinPointNum(1);
        GridData gridData = stationData.interpolateData(interSet);
        if(colors.length==0){
            colors=LegendManage.createRandomColors(breaks.length);
        }
        double[] extreme = new double[2];
        boolean hasUndef = gridData.getMaxMinValue(extreme);
        LegendScheme legendScheme =LegendManage.createGraduatedLegendScheme(breaks, colors
                , ShapeTypes.Polygon,extreme[1],extreme[0],HAS_NO_DATA,INVALID_VALUE);
            VectorLayer contourLayer = (type==CONTOUR_POLYGON_JSON)?
                    DrawMeteoData.createShadedLayer(gridData,legendScheme, PRECIPITATION_UNIT, VALUE_FIELD,SMOOTH_SHAPE)
                    :DrawMeteoData.createContourLayer(gridData,legendScheme, PRECIPITATION_UNIT, VALUE_FIELD,SMOOTH_SHAPE);
            VectorLayer lastLayer = contourLayer.clip(clipLayer);
            lastLayer.setProjInfo(clipLayer.getProjInfo());
            lastLayer.saveFile(tmpFilePath+SUFFIXES[0]);
            result= GisUtil.ShapeToGeoJSON(tmpFilePath+SUFFIXES[0]);
            List<String> list=Arrays.asList(SUFFIXES);
            for(int i=0;i<SUFFIXES.length-1;i++){
                FileUtil.deleteFile(tmpFilePath+SUFFIXES[i]);
            }
            String pictureName=DateTimeUtil.getContourPictureNameByDuration(-1)+SUFFIXES[4];
            String picturePath=TEMPLATES_FILE_PATH+pictureName;
            tmpFilePath=TEMPLATES_FILE_PATH+UUID.randomUUID().toString()+SUFFIXES[4];
            //add map container
            MapLayout maplayout=new MapLayout();
            maplayout.setSize(1764, 1600);
            maplayout.setBackground(new Color(255, 255, 255, 0));
            MapFrame mapFrame=maplayout.getActiveMapFrame();
            MapView mapView =mapFrame.getMapView();
            VectorLayer contourLayer1 = DrawMeteoData.createShadedLayer(gridData, legendScheme, PRECIPITATION_UNIT
                    , VALUE_FIELD,true);
            VectorLayer lastLayer1 = contourLayer1.clip(clipLayer);
            lastLayer.setProjInfo(clipLayer.getProjInfo());
            //export page size setting
            mapView.addLayer(clipLayer);
            mapView.addLayer(lastLayer1);
            mapView.setGridXDelt(GRID_DELTA);
            mapView.setGridYDelt(GRID_DELTA);
            maplayout.setPageBounds(new Rectangle(0,0,1764,1600));
            maplayout.getActiveLayoutMap().setLeft(0);
            maplayout.getActiveLayoutMap().setTop(0);
            maplayout.getActiveLayoutMap().setWidth(1764);
            maplayout.getActiveLayoutMap().setHeight(1600);
            //export image and polish up image
            try {
                maplayout.exportToPicture(tmpFilePath);
                boolean re=transferAlpha2File(tmpFilePath,picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtil.deleteFile(tmpFilePath);
        return result;
    }
}
