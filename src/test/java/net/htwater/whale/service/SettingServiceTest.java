package net.htwater.whale.service;

import net.htwater.whale.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import javax.xml.ws.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingServiceTest {
    @Autowired
    ISettingService service;
    @Test
    public void addColorSeriesTest(){
        Date date=new Date();
        ColorSeries series=new ColorSeries();
        series.setName("深色系带");
        series.setIsDefault(1);
        List<Color> colors=new ArrayList<>();
        Color color1=new Color();
        Color color2=new Color();
        Color color3=new Color();
        Color color4=new Color();
        Color color5=new Color();
        Color color6=new Color();
        Color color7=new Color();
        Color color8=new Color();
        Color color9=new Color();
        Color color10=new Color();
        colors.add(color1.setColor("#DFECF2"));
        colors.add(color2.setColor("#23FEFD"));
        colors.add(color3.setColor("#23C1FF"));
        colors.add(color4.setColor("#2376FF"));
        colors.add(color5.setColor("#234BFF"));
        colors.add(color6.setColor("#7D19C5"));
        colors.add(color7.setColor("#5A27C5"));
        colors.add(color8.setColor("#4C0669"));
        colors.add(color9.setColor("#340669"));
        colors.add(color10.setColor("#240634"));
        service.addColorSeries(series,colors);
    }

    @Test
    public void updateColorSeries(){
        ColorSeries series=new ColorSeries();
        series.setGmtModified(new Date());
        series.setName("气象深色带");
        series.setId( 6L) ;
        service.updateColorSeries(series);

    }
    @Test
    public void deleteColorSeriesTest(){
        service.deleteColorSeries(7L);
    }

    @Test
    public void listColorSeriesTest(){
        List<ColorSeries> series=service.listColorSeries();
        Assert.assertTrue(series.size()>0);
    }
    @Test
    public void listColorTest(){
        List<Color> colors=service.listColorsBySeriesId(0L);
        Assert.assertTrue(colors.size()>0);
    }
    @Test
    public void addStationSeriesTest(){
        StationSeries stationSeries=new StationSeries();
        stationSeries.setName("测试");
        stationSeries.setIsDefault(0);
        Station station=new Station();
        Station station1=new Station();
        station.setStcd("62414650");
        station1.setStcd("63000100");
        List<Station> stations=new ArrayList<>();
        stations.add(station);
        stations.add(station1);
        long id=service.addStationSeries(stationSeries,stations);
        System.out.println(id);
    }
    @Test
    public void listStationForVacuateTest(){
        List<RainStation> stations=service.listStationForVacuate(10);
        int count=0;
        for (RainStation station:stations){
            if (station.getSelected()){
                count++;
                System.out.println(station.getLgtd()+" "+station.getLttd()+" "+station.getStcd());
            }
        }
        Assert.assertTrue(stations.size()>0&&count>0);
    }
}
