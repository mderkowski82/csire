//package pl.npesystem.configuration;
//
//import org.jooq.DSLContext;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DefaultConfiguration;
//import org.jooq.impl.DefaultDSLContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class JooqConfiguration {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public DSLContext dsl() {
//        return new DefaultDSLContext(configuration());
//    }
//
//    private DefaultConfiguration configuration() {
//        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
//        jooqConfiguration.set(SQLDialect.POSTGRES);
//        jooqConfiguration.set(dataSource);
//        return jooqConfiguration;
//    }
//}