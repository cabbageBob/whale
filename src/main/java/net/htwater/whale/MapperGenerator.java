/**
 * @author shitianlong
 * 根据数据库表生成mybatis的Mapper
 */
package net.htwater.whale;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Jokki
 * @description: generate mapper
 * @date: Created in 上午9:32 18-6-4
 * @modified By:
 */
public class MapperGenerator {
    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        AutoGenerator mpg = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(baseDir + "/src/main/java");
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setAuthor("shitianlong");
        mpg.setGlobalConfig(gc);
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("whale");
        dsc.setPassword("htwater");
        dsc.setUrl("jdbc:mysql://172.16.35.52:3306/whale?allowMultiQueries=true&userUnicode=true" +
                "&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
        mpg.setDataSource(dsc);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[] { "contour_"});
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(new String[] { "contour_fixed_intervals"});
        strategy.setDbColumnUnderline(false);
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);
        PackageConfig pc = new PackageConfig();
        pc.setParent("net.htwater.whale");
        mpg.setPackageInfo(pc);
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseDir + "/src/main/resources/mapper/sys/" + tableInfo.getEntityName() + ".xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        TemplateConfig tc = new TemplateConfig();
        tc.setEntity("/templates/entity.java.vm");
        mpg.setTemplate(tc);
        mpg.execute();
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }
}