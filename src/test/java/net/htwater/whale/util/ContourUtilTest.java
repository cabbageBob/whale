package net.htwater.whale.util;

import net.htwater.whale.entity.DiscretePoint;
import org.junit.Before;
import org.junit.Test;
import org.meteoinfo.geoprocess.analysis.InterpolationMethods;

import java.util.ArrayList;
import java.util.List;

public class ContourUtilTest {
    List<DiscretePoint > points=new ArrayList<>();
    @Before
    public void initialize(){
        DiscretePoint point=new DiscretePoint();
        points.add(new DiscretePoint("1971",121.9015833,29.74874722,90));
        points.add(new DiscretePoint("1917",121.7763861,29.97072778,50));
        points.add(new DiscretePoint("2004",121.7983528,29.89108056,40));
        points.add(new DiscretePoint("7827",121.9740555,29.853083,0));
        points.add(new DiscretePoint("2017",121.8246861,29.79736111,100));
    }

}
