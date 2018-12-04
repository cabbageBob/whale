package net.htwater.whale.util;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
/**
 * GIS 工具类
 * @author shitianlong
 * @since 2018-9-1
 */
 class GisUtil {
     static String  ShapeToGeoJSON(String shpPath){
         FeatureJSON featureJSON = new FeatureJSON();
         StringBuffer sb = new StringBuffer();
         try{
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource =shpDataStore.getFeatureSource (typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            while (itertor.hasNext())
            {
                SimpleFeature feature = itertor.next();
                StringWriter writer = new StringWriter();
                featureJSON.writeFeature(feature, writer);
                array.add(JSON.parseObject(writer.toString()));
            }
            itertor.close();
            sb.append(array.toString());
            sb.append("}");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
