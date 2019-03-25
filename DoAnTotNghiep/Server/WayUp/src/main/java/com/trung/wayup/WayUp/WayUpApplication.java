package com.trung.wayup.WayUp;

import com.trung.wayup.WayUp.model.FileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorage.class
})
@EnableScheduling
public class WayUpApplication {

	public static void main(String[] args) {
		System.getProperties().put( "server.port", 9999 );
		SpringApplication.run(WayUpApplication.class, args);
	}

}
