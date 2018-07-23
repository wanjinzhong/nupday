package com.nupday.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    public DBInfo dbInfo() {
        String prefix = "jdbc:mysql://";
        String host = url.substring(prefix.length(), url.indexOf(":", 13));
        String port = url.substring(prefix.length() + host.length() + 1, url.indexOf("/", prefix.length() + host.length()));
        String database = url.substring(prefix.length() + host.length() + port.length() + 2, url.indexOf("?"));
        DBInfo dbInfo = new DBInfo();
        dbInfo.setDatabase(database);
        dbInfo.setHost(host);
        dbInfo.setPassword(password);
        dbInfo.setPort(port);
        dbInfo.setUrl(url);
        dbInfo.setUserName(userName);
        return dbInfo;
    }


}
