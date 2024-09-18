package com.wach;


import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * 数据库文档生成工具
 */
public class DatabaseDocExport {


    public static void main(String[] args) {
        run();
    }

    public static void run(){

        EngineFileType FILE_TYPE = EngineFileType.MD;
        String FILE_OUTPUT_DIR = "E:\\doc";//文档输出目录
        String DOC_FILE_NAME = "数据库设计文档";//文档名称
        String DOC_VERSION = "1.0.0";//文档版本
        String DOC_DESCRIPTION = "数据库设计文档";//文档描述

        String driver = "org.postgresql.Driver";
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/test";
        String username = "postgres";
        String password = "test";

        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);

        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                .fileOutputDir(FILE_OUTPUT_DIR)
                .openOutputDir(true)
                .fileType(FILE_TYPE)
                .produceType(EngineTemplateType.freemarker)
                .fileName(DOC_FILE_NAME).build();

        ProcessConfig processConfig = ProcessConfig.builder()
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(new ArrayList<>())
                //忽略表前缀
                .ignoreTablePrefix(new ArrayList<>())
                //忽略表后缀
                .ignoreTableSuffix(new ArrayList<>()).build();

        //配置
        Configuration config = Configuration.builder()
                //版本
                .version(DOC_VERSION)
                //描述
                .description(DOC_DESCRIPTION)
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }

}

