package net.htwater.whale.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.htwater.whale.entity.DiscretePoint;
import net.htwater.whale.entity.param.ContourGeoJsonParams;
import net.htwater.whale.entity.param.StationSettingParams;
import net.htwater.whale.util.ContourUtil;
import net.htwater.whale.util.DateTimeUtil;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ContourControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    ContourController contourController;
    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        List<DiscretePoint > points=new ArrayList<>();
        points.add(new DiscretePoint("1971",121.9015833,29.74874722,63.5));
        points.add(new DiscretePoint("1917",121.7763861,29.97072778,106.5));
        points.add(new DiscretePoint("2004",121.7983528,29.89108056,110.5));
        points.add(new DiscretePoint("7827",121.9740555,29.853083,53));
        points.add(new DiscretePoint("2017",121.8246861,29.79736111,164));
    }
    @Test
    public void listContourRainsTest() throws Exception{
        RequestBuilder request = get("/contour/listContourRains")
                .param("series","0")
                .param("start","2018-8-1 08:00:00.000")
                .param("end","2018-8-1 09:00:00.000")
                .param("type","-5")
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(status,200);
    }
    @Test
    public void listContourIntervalTest() throws Exception{
        RequestBuilder request = get("/contour/listContourInterval")
                .param("series","0")
                .param("min","0")
                .param("max","100")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(status,200);
    }
    @Test
    public void getContourGeoJsonTest() throws Exception{
        RequestBuilder request = post("/contour/getContourGeoJson")
                .param("start","2018-8-1 08:00:00.000")
                .param("end","2018-8-1 09:00:00.000")
                .param("breaks","10,20,30,40,50")
                .param("stations","62414650,63000100")
                .param("district","330000")
                .param("type","1")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(status,200);
    }
    @Test
    public void listStationSeriesTest() throws Exception{
        RequestBuilder request = get("/contour/listStationSeries")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(status,200);
    }
    @Test
    public void listColorSeriesTest() throws Exception{
        RequestBuilder request = get("/contour/listColorSeries")
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(status,200);
    }

    @Test
    public void getRegionRainByYearTest() throws Exception {
        RequestBuilder requestBuilder = get("/contour/getRegionRainByYear")
                .param("year", "2018");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

    @Test
    public void getConcurrentContourGeoJson() throws Exception {
        RequestBuilder requestBuilder = post("/contour/geojson/realtime")
                .param("type", "-5")
                .param("date", "2018-9-21");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
    }
    @Test
    public void getSingleContourPicture() throws Exception {
        RequestBuilder requestBuilder = post("/contour/history/picture/single")
                .param("duration", String.valueOf(DateTimeUtil.HYDROLOGICAL_DAY_1))
                .param("date", "2018-09-23");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
    }
    @Test
    public void getSeriesContourPicture() throws Exception {
        RequestBuilder requestBuilder = get("/contour/history/picture/series")
                .param("duration", String.valueOf(DateTimeUtil.HYDROLOGICAL_DAY_1))
                .param("begin", "2018-09-22")
                .param("end", "2018-09-24");
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
    }
    @Test
    public void drawCurrentContourGeoJson() throws Exception {
        ContourGeoJsonParams params = new ContourGeoJsonParams();
        params.setStations("120.590463:28.756797:52.5,120.824851:27.936421:49.5,120.14419:30.203006:47.5,120.271111:28.151389:42,120.394381:28.628895:41.5,119.43139:27.61194:37.5,120.68:27.83:35,119.970556:30.199167:34.5,120.96168:28.124111:34.5,120.733333:28.666666:34,120.06657:28.132151:33.5,121.057008:28.60929:32.5,120.423:28.0295:31.5,120.171694:27.571431:31.5,120.6192:27.7846:30,120.02519:28.125236:30,119.788056:27.566389:30,119.45333:27.85194:28,120.8489:29.9145:26.5,120.383704:28.261902:25.5,121.172014:28.544994:25,120.836572:29.903512:24.5,120.02194:27.97389:23.5,120.258293:30.158118:23.5,120.441:27.9136:22,119.071006:29.855935:21.5,119.85148:27.361263:21,118.608412:28.420253:20.5,120.559477:28.064855:20,120.403055:30.285833:19.5,120.10611:28.29278:19.5,120.685777:30.521306:19.5,121.242188:28.536711:19,120.01:27.852:18.5,121.229874:28.492291:18,121.25244:28.654103:17.5,120.7885:29.8519:17,120.971096:28.590217:17,120.241889:28.180107:16.5,120.16:28.1075:16.5,120.111:27.786:16,121.190323:29.790738:16,120.6337:27.9946:16,120.556944:27.671944:16,121.048217:28.446673:16,121.104577:28.849054:15.5,121.302944:28.59775:15.5,119.686944:27.563055:15,118.552685:28.465464:15,120.9809:28.2518:15,121.379944:28.574638:14.5,121.311144:28.851798:14.5,120.167222:30.125277:14.5,119.64306:27.97389:14.5,121.328:28.6943:14,119.636388:30.168333:14,120.420775:30.521017:14,121.014236:28.381708:14,120.044663:27.676405:14,120.291666:27.606944:14,121.15652:27.85915:13.5,119.714996:27.810331:13.5,119.530486:28.664978:13,120.520384:28.46327:13,120.9365:28.883194:13,121.44675:28.687416:13,120.815927:28.781542:13,120.153:27.756:12.5,120.156662:30.122382:12.5,119.592919:28.622527:12.5,120.189833:30.506388:12.5,120.846:30.527:12,121.389355:28.456817:12,121.589286:28.821498:11.5,121.1506:30.6335:11.5,120.583889:29.983889:11.5,121.41426:28.795527:11,121.202948:28.901138:11,119.808653:27.770097:11,121.377055:28.37725:11,121.052863:28.946459:11,119.633:28.835786:11,121.372684:30.289346:11,119.892347:28.830522:10.5,119.420609:28.433668:10.5,119.54639:28.0675:10.5,120.013907:28.897914:10,120.701944:30.085833:10,119.72:30.24:10,120.628056:30.130167:10,120.013403:28.317891:10,121.524459:28.799375:9.5,120.087134:28.802374:9.5,121.509888:28.491361:9,120.166652:28.890263:9,120.245277:30.0375:9,120.749971:28.270519:8.5,121.335764:29.927631:8.5,120.866317:30.020589:8.5,119.15139:28.70833:8.5,119.5825:28.11556:8.5,119.846723:28.655325:8.5,120.7428:28.839636:8.5,119.195905:28.816024:8,118.619166:28.703888:8,119.9169:27.81275:8,118.278766:29.258046:8,119.29417:27.84167:8,121.137222:29.224444:8,119.843:27.843:7.5,119.29139:28.50139:7.5,121.227853:29.955904:7.5,121.023111:29.138056:7.5,118.7825:28.365:7.5,118.980555:30.086944:7,120.935532:29.233146:7,121.24575:29.208444:7,121.606083:28.501527:7,121.314694:29.002333:7,120.211362:28.414128:6.5,121.046606:29.950607:6.5,121.628357:29.042072:6.5,120.060181:29.03032:6.5,120.058605:28.522466:6.5,121.418021:28.361173:6.5,121.341628:29.304804:6.5,120.605278:29.871111:6.5,120.220446:28.531493:6,121.370515:29.130345:6,119.796944:30.250555:6,119.776111:30.118055:6,120.071956:28.655288:6,120.865931:29.797056:6,120.861602:28.12085:6,119.40806:28.58028:6,120.83954:29.261196:6,120.276998:27.774073:6,119.832777:28.908888:6,120.9996:29.9621:6,119.987879:29.085262:6,120.588798:29.359962:6,118.902674:29.927263:5.5,119.611708:28.945316:5.5,121.58457:29.9648:5.5,119.955372:28.423209:5.5,121.410222:29.285389:5.5,120.8114:29.2735:5.5,121.53302:30.037771:5.5,119.0291:28.15033:5.5,121.156722:29.123075:5.5,120.809694:29.033555:5.5,119.31194:27.63472:5.5,120.903192:30.373813:5.5,119.575833:30.351944:5,120.754058:29.052389:5,121.299132:29.302852:5,120.9313:30.06:5,121.802909:29.194678:5,120.045847:29.278941:5,120.3847:27.50141:5,119.466511:28.846527:5,120.698254:29.204453:5,120.852755:28.108569:4.5,120.996666:30.151388:4.5,120.4975:27.568888:4.5,121.425392:29.394442:4.5,119.266603:28.980491:4.5,120.439977:29.059588:4.5,121.46475:30.234:4.5,121.720377:29.482876:4.5,118.336542:29.405219:4.5,119.219261:28.536373:4.5,119.906972:30.267194:4.5,121.025521:30.690608:4.5,119.615972:29.086905:4.5,119.925611:30.276305:4.5,120.169887:29.030075:4.5,119.246116:28.56228:4.5,119.535385:28.212443:4,120.043238:29.131741:4,120.167964:29.137286:4,120.469914:29.231323:4,120.5992:29.4212:4,121.272888:29.682:4,119.66092:28.22719:4,120.799063:29.116021:4,121.147408:30.03781:4,119.90694:28.43917:4,121.408719:29.106912:4,119.332308:28.934832:4,119.834251:29.13653:4,120.438907:29.11973:4,120.6425:29.2895:4,120.2552:29.5167:3.8,122.0151:30.0863:3.5,120.222583:29.292361:3.5,120.921617:29.370561:3.5,120.534151:28.814852:3.5,119.626765:30.263472:3.5,120.012719:29.207867:3.5,121.16:29.4026:3.5,119.39962:28.970914:3.5,121.114052:29.381505:3.5,121.229304:30.090918:3.5,119.732869:29.016561:3.5,121.151507:29.987415:3.5,119.478918:28.974924:3,120.998498:29.432352:3,119.964916:30.397805:3,120.552358:29.464277:3,120.575:29.7768:3,122.019026:30.046874:3,119.100879:28.063831:3,119.609213:28.37444:3,120.342489:29.482547:3,120.133888:30.318888:3,120.301427:30.416764:3,119.386055:30.190822:3,120.545396:30.401719:3,121.384675:30.107875:3,120.077469:30.866664:3,120.444453:29.835847:3,118.657031:29.478229:2.5,121.639:29.1855:2.5,119.508476:29.929624:2.5,120.68912:29.429121:2.5,119.220069:28.155892:2.5,121.344386:29.857901:2.5,120.551772:27.595611:2.5,120.7971:29.75:2.5,119.912653:30.348055:2.5,121.617904:29.320194:2.5,119.86896:30.868329:2.5,121.737442:29.443615:2.5,122.0777:30.04872:2.5,120.6548:29.5333:2,119.956944:30.047777:2,119.70889:28.28028:2,119.78:30.421111:2,120.47608:29.429421:2,121.540005:29.904629:2,121.051:29.4539:2,120.525556:30.625556:2,118.928551:29.736646:2,120.5114:29.5807:2,119.42871:29.964424:2,119.25:29.075:2,120.388609:29.581035:2,121.000126:29.514581:2,121.512673:29.677037:2,120.101592:30.956725:2,119.472786:29.131415:2,121.831801:29.458003:2,119.65:30.378888:2,119.05556:27.79167:2,121.195298:29.652572:2,120.295079:29.925336:1.6,121.79772:29.419507:1.5,120.3015:27.4762:1.5,120.594071:29.543092:1.5,121.716313:29.950379:1.5,119.167621:29.053157:1.5,121.129179:29.515049:1.5,121.355674:29.573668:1.5,119.327926:29.10261:1.5,121.027067:29.633669:1.5,120.076888:30.544913:1.5,121.327232:30.15026:1.5,118.944591:29.133323:1.5,119.95076:29.315724:1.5,119.912837:29.296149:1.5,119.755001:28.376123:1.5,121.001945:29.519109:1.5,119.466345:29.218416:1.5,121.530023:29.772042:1.5,118.306028:28.884972:1,118.543045:28.959479:1,119.339304:30.061625:1,120.924929:29.579961:1,118.195219:29.120749:1,119.088567:27.741866:1,120.275538:30.611721:1,118.503179:29.466102:1,119.793944:29.791835:1,119.303333:30.343888:1,122.17719:30.08877:1,119.87055:29.517731:1,119.826939:29.446012:1,119.753055:30.332777:1,121.58239:29.695081:1,119.681149:29.811629:1,119.056257:29.068226:1,119.843138:29.454047:1,121.718385:29.271553:1,119.60137:30.331973:1,118.637625:29.124788:1,118.944759:30.29265:1,119.572931:30.431542:1,119.351666:30.256111:1,119.16127:30.041842:1,121.432094:29.830264:1,119.604608:30.02915:1,120.902176:29.50735:1,120.700421:30.75559:1,119.412911:29.722394:1,120.825884:29.696461:1,119.650658:29.708551:1,119.760555:29.4425:1,118.859472:28.972875:0.5,121.797572:29.894088:0.5,119.709902:30.344047:0.5,119.908787:30.761995:0.5,119.612298:30.514913:0.5,119.685163:31.114006:0.5,120.421996:30.886579:0.5,120.830792:29.59513:0.5,120.388563:29.725694:0.5,120.013565:30.667021:0.5,119.030922:30.282333:0.5,121.793278:29.864586:0.5,119.991149:29.449916:0.5,122.101376:29.773243:0.5,119.523886:30.43477:0.5,118.925112:28.9852:0.5,121.735416:29.792305:0.5,121.688333:29.659805:0.5,119.910628:31.002358:0.5,118.610154:29.056634:0.5,120.590727:29.603033:0.5,121.111373:29.47709:0.5,120.494978:30.755272:0.5,119.519101:30.300625:0.5,118.698611:29.344444:0.5,120.085006:29.669275:0.5,120.6406:29.6898:0.5,119.942304:30.672598:0.5,118.411638:29.139166:0.5,119.85508:29.376283:0.5,119.91425:29.444838:0.5,120.267696:29.642366:0.4,120.125973:29.558103:0.2,120.042957:29.707588:0.2,119.483172:30.633535:0,119.734638:30.530944:0,120.166266:29.466232:0,121.63996:29.79335:0,119.60444:27.72417:0,119.816839:31.044368:0,119.949657:29.5242:0,119.755378:30.805582:0,119.473164:30.53918:0,119.681725:30.644335:0,118.625591:29.311433:0,119.701188:30.588845:0,119.938155:31.107768:0,119.514444:29.3675:0,119.350137:29.368019:0,120.701293:30.887031:0,118.529381:28.898189:0,120.190247:29.42725:0,119.603423:30.832266:0,119.604346:30.953449:0,119.496952:29.539204:0,118.416694:29.301833:0,119.711222:30.738306:0,120.7031:29.609:0,120.53465:27.50132:0,120.232638:29.703556:0,120.1328:29.4157:0,119.739612:30.562299:0,120.4348:27.4411:0,119.118929:27.621331:0,119.883383:30.514056:0,120.921466:30.851501:0,119.552743:30.592927:0,119.34697:29.29804:0,119.884101:29.503342:0,119.518496:30.747872:0,119.60861:30.90075:0,118.966666:29.216666:0");
        params.setBreaks("0,10,25,50,100,250");
        params.setDistrict("330000");
        params.setColors("#7FFFFF,#23B7FF,#007BB4,#0027BC,#9602F4,#50038C");
        params.setType(ContourUtil.CONTOUR_POLYGON_JSON);
        RequestBuilder requestBuilder = post("/contour/current/picture")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
    }

}
