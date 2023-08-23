package cn.skcks.docking.gb28181;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Gb28181DockingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(Gb28181DockingPlatformApplication.class, args);
	}

}
