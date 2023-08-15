package cn.skcks.docking.gb28181;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Gb28181DockingPlatformApplicationTest {
	public static void main(String[] args) {
		SpringApplication.run(Gb28181DockingPlatformApplication.class, args);
	}
}
