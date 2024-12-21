package ru.uvuv643.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {

    private final DataSource dataSource;

    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource; // Injects the datasource from Spring Boot
    }

    @Bean
    public DSLContext dslContext() {
        return new DefaultDSLContext(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
    }
}