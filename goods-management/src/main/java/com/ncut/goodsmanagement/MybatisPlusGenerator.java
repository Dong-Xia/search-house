package com.ncut.goodsmanagement;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * mybatsiplus生成代码工类
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String packageName = "com.ncut.xia.redproduct.servcie";
        boolean serviceNameStartWithI = true;//user -> UserService, 设置成true: user -> IUserService
        String[] tables = {"t_red_packet","t_user_red_packet"};
        generateByTables(serviceNameStartWithI, packageName, tables);
    }

    public static void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames){
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/redpacket?characterEncoding=utf8&characterSetResults=utf8&useUnicode=false&rewriteBatchedStatements=true&autoReconnect=true&useSSL=false&serverTimezone=UTC";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("891204")
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setTablePrefix("")//表前缀
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false)
                .setIdType(IdType.INPUT)
                .setEnableCache(false)
                .setAuthor("xiadong")
                .setOutputDir("F:\\redcode")
                //.setOutputDir("/Users/yangliping/dev/codeGen")
                .setFileOverride(true);

        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setEntity("domain")
                ).execute();

    }
}
