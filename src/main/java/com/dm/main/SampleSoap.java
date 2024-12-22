package com.dm.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
@EnableWs
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "com.dm")
public class SampleSoap {

	public static void main(String[] args) {
		SpringApplication.run(SampleSoap.class, args);
//		val.toString();
	}
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
