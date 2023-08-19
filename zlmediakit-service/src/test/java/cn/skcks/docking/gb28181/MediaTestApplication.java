package cn.skcks.docking.gb28181;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MediaTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaTestApplication.class, args);
    }
}
