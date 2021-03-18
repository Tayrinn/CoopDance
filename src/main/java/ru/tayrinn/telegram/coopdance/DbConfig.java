package ru.tayrinn.telegram.coopdance;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Statement;

@Configuration
public class DbConfig {

    private Statement db;
//
//    public void createDbConnection() {
//        try {
//            db = dataSource().getConnection().createStatement();
//            createTables();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//    private void createTables() {
//    }

    @Bean
    public BasicDataSource dataSource() {
        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + ":" + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }

}
