package cn.worth.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author chenxiaoqing
 */
@Slf4j
public class ResourcesGenerator {

    private static Properties properties;

    static {
        properties = new Properties();
        InputStream in = ResourcesGenerator.class.getClassLoader().getResourceAsStream("config/generator.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            log.error("file not exist :{}", e.getMessage(), e);
        }
    }

    public static void generateCode() {
        boolean serviceNameStartWithI = true;//user -> UserService, 设置成true: user -> IUserService
        generateByTables(serviceNameStartWithI, "ms_test");
    }

    private static void generateByTables(boolean serviceNameStartWithI, String... tableNames) {

        AutoGenerator autoGenerator = new AutoGenerator();
        /*
         * 全局配置
         */
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/worth-generator/src/main/java");
        globalConfig.setOpen(false);
        //实体属性 Swagger2 注解
//        globalConfig.setSwagger2(true);
        // 覆盖生成文件
        globalConfig.setFileOverride(true);
        globalConfig.setActiveRecord(true);
        // ID生成策略
        globalConfig.setIdType(IdType.AUTO);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML columList
        globalConfig.setBaseColumnList(true);
        globalConfig.setAuthor("chenxiaoqing");
        autoGenerator.setGlobalConfig(globalConfig);

        /*
         * 数据源配置
         */
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName(properties.getProperty("jdbc.driver"));
        dsc.setUsername(properties.getProperty("jdbc.username"));
        dsc.setPassword(properties.getProperty("jdbc.password"));
        dsc.setUrl(properties.getProperty("jdbc.url"));
        autoGenerator.setDataSource(dsc);

        /*
         * 策略配置
         */
        StrategyConfig strategyConfig = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //设置父类
        strategyConfig.setSuperControllerClass(properties.getProperty("base.controller"));
//        strategyConfig.setSuperEntityClass(properties.getProperty("base.model"));
//        strategyConfig.setSuperServiceImplClass(properties.getProperty("base.serviceImpl"));
        // 表名生成策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //去除表名前缀 update
        strategyConfig.setTablePrefix(NamingStrategy.removePrefix("ms_"));

        strategyConfig.setCapitalMode(true);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setInclude(tableNames);
//        strategyConfig.setVersionFieldName("");
        strategyConfig.setRestControllerStyle(true);
        autoGenerator.setStrategy(strategyConfig);

        /*
         * 包配置
         */
        PackageConfig pc = new PackageConfig();
        //update
        pc.setParent(properties.getProperty("gen.basePackageName"));

        pc.setModuleName(scanner("模块名"));
        pc.setController("controller");
        pc.setEntity("domain");
        autoGenerator.setPackageInfo(pc);

//        if (!serviceNameStartWithI) {
//            globalConfig.setServiceName("%sService");
//        }
        autoGenerator.execute();
    }

    public static void main(String[] args) {
        generateCode();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
