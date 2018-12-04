package net.htwater.whale.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import net.htwater.whale.entity.*;
import net.htwater.whale.service.IGenerateContourPictureService;
import net.htwater.whale.service.IContourService;
import net.htwater.whale.service.ISettingService;
import net.htwater.whale.util.ContourUtil;
import net.htwater.whale.util.DateTimeUtil;
import net.htwater.whale.util.FileUtil;
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
import org.meteoinfo.map.MapView;
import org.meteoinfo.shape.ShapeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * @author wangzhifan
 */
@Service
public class GenerateContourPictureServiceImpl implements IGenerateContourPictureService {
    private static final String COMMA=",";
    private static final String ZHE_JIANG_CODE="330000";
    @Autowired
    private ISettingService settingService;
    @Autowired
    private IContourService service;
    @Override
    public String generateContourPicture(int type) {
        double[] breaks;
        Color[] colors;
        List<FixedIntervals> fixedIntervals=settingService.listFixedIntervals(type);
        breaks=new double[fixedIntervals.size()+1];
        colors=new Color[fixedIntervals.size()];
        for (int i = 0; i <fixedIntervals.size() ; i++) {
            breaks[i]=fixedIntervals.get(i).getMin();
            colors[i]=HexUtil.decodeColor(fixedIntervals.get(i).getColor());
        }
        breaks[fixedIntervals.size()]=fixedIntervals.get(fixedIntervals.size()-1).getMax();
        String[] times= DateTimeUtil.getStartAndEndTime(type);
        List<StationRain> rains = ContourUtil.recommendStations(service.listAllRainStation(times[0], times[1]),40);
        StationData data=new StationData();
        for (StationRain rain:rains){
            data.addData(rain.getStcd(),rain.getLgtd(),rain.getLttd(),rain.getValue());
        }
        String fileName=service.drawContourFeature(data,breaks,ZHE_JIANG_CODE,colors,ContourUtil.CONTOUR_POLYGON_PNG,type);
        String filePath ="\\contour_img\\"+fileName;
        service.addPicture(fileName,filePath,DateTimeUtil.getEvenDay(0),String.valueOf(type));
        return fileName;
    }

    @Override
    public void test(int type) {
        double[] breaks;
        Color[] colors;
        List<FixedIntervals> fixedIntervals=settingService.listFixedIntervals(type);
        breaks=new double[fixedIntervals.size()+1];
        colors=new Color[fixedIntervals.size()];
        for (int i = 0; i <fixedIntervals.size() ; i++) {
            breaks[i]=fixedIntervals.get(i).getMin();
            colors[i]=HexUtil.decodeColor(fixedIntervals.get(i).getColor());
        }
        breaks[fixedIntervals.size()]=fixedIntervals.get(fixedIntervals.size()-1).getMax();
        for (int i = 0; i <30 ; i++) {
            String start=DateTimeUtil.parseDateToString(DateTimeUtil.getEvenDayInDate(i+1));
            String end=DateTimeUtil.parseDateToString(DateTimeUtil.getEvenDayInDate(i));
            System.out.println(end);
            System.out.println(i+"/200");
            List<StationRain> rains = ContourUtil.recommendStations(service.listAllRainStation(start, end),40);
            StationData data=new StationData();
            for (StationRain rain:rains){
                data.addData(rain.getStcd(),rain.getLgtd(),rain.getLttd(),rain.getValue());
            }
            String fileName=drawContourFeature(ZHE_JIANG_CODE,data,breaks,colors,ContourUtil.CONTOUR_POLYGON_PNG,type,DateTimeUtil.getEvenDayInDate(i));
            String filePath ="\\contour_img\\"+fileName;
            //service.addPicture(fileName,filePath,end,String.valueOf(type));
        }
    }
    private static String TEMPLATES_FILE_PATH="D:\\JAVA\\apache-tomcat-7.0.40\\webapps\\ROOT\\contour_img\\";
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
    private static final boolean SMOOTH_SHAPE=false;
    private static final boolean HAS_NO_DATA=false;
    private static final String COMMON_PATTERN="yyyy-MM-dd HH:mm:ss";
    private static final String  CONDENSE_PATTERN="yyyyMMddHHmm";
    private static final SimpleDateFormat COMMON_FORMAT = new SimpleDateFormat(COMMON_PATTERN);
    private static final SimpleDateFormat CONDENSE_FORMAT = new SimpleDateFormat(CONDENSE_PATTERN);
    private  String drawContourFeature(String district,StationData stationData,double[] breaks, Color[] colors,int type,int duration,Date date){
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
            Map<Integer,String> map=new HashMap<>(10);
            map.put(5,"PRE_CON_DAY_1_");
            map.put(6,"PRE_CON_DAY_3_");
            map.put(7,"PRE_CON_DAY_7_");
            DateTimeFormatter format = DateTimeFormatter.ofPattern(COMMON_PATTERN);
            String pictureName=map.get(duration)+CONDENSE_FORMAT.format(date)+SUFFIXES[4];
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
           return pictureName;
    }
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
                    if ((r==255) && (g ==251) && (b==195)) {
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
}