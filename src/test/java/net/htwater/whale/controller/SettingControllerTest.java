package net.htwater.whale.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.htwater.whale.entity.RainStation;
import net.htwater.whale.entity.Station;
import net.htwater.whale.entity.StationSeries;
import net.htwater.whale.entity.param.StationSettingParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    public void listStationSeriesTest() throws Exception{
        RequestBuilder request = get("/setting/listStationSeries")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        List<StationSeries> series=JSON.parseArray(object.get("data").toString(),StationSeries.class);
        Assert.assertTrue(series.size()>0);
        Assert.assertEquals(status,200);
    }
    @Test
    public void addStationSeriesTest() throws Exception{
        StationSettingParams params=new StationSettingParams();
        params.setStations("60202160,62414650,62414660,62414670,62414690,62414710,62414720,62414730,62414740,62441280");
        params.setName("测试站点序列");
        params.setChoose(true);
        RequestBuilder request = post("/setting/addStationSeries")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertTrue((Boolean) object.get("data"));
        Assert.assertEquals(status,200);
    }
    @Test
    public void updateStationSeriesTest() throws Exception{
        StationSettingParams params=new StationSettingParams();
        RequestBuilder request = get("/setting/updateStationSeries")
                .param("id","12")
                .param("name","20公里均匀分布测试")
                .param("choose","true")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertTrue((Boolean) object.get("data"));
        Assert.assertEquals(status,200);
    }
    @Test
    public void updateStationTest() throws Exception{
        StationSettingParams params=new StationSettingParams();
        params.setStations("60202160,62414650");
        params.setId(8L);
        RequestBuilder request = post("/setting/updateStationsById")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertTrue((Boolean) object.get("data"));
        Assert.assertEquals(status,200);
    }
    @Test
    public void deleteStationSeriesTest() throws Exception{
        StationSettingParams params=new StationSettingParams();
        RequestBuilder request = get("/setting/deleteStationSeries")
                .param("id","13")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertTrue((Boolean) object.get("data"));
        Assert.assertEquals(status,200);
    }
    @Test
    public void listEquidistributionStationTest() throws Exception{
        RequestBuilder request = post("/setting/listEquidistributionStation")
                .param("kilometres","40")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        List<RainStation> stations=JSON.parseArray(object.get("data").toString(),RainStation.class);
        Assert.assertTrue(stations.size()>0);
        Assert.assertEquals(status,200);
    }
    @Test
    public void listStationsTest() throws Exception{
        RequestBuilder request = post("/setting/listStations")
                .param("id","7")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        JSONObject object= JSON.parseObject(mvcResult.getResponse().getContentAsString());
        List<RainStation> stations=JSON.parseArray(object.get("data").toString(),RainStation.class);
        Assert.assertTrue(stations.size()>0);
        Assert.assertEquals(status,200);
    }
    @Test
    public void listColorBar() throws Exception{
        RequestBuilder request = get("/setting/colorBar")
                .param("start","2018-09-21 08:00:00")
                .param("end","2018-09-22 08:00:00")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(status,200);
    }
}
