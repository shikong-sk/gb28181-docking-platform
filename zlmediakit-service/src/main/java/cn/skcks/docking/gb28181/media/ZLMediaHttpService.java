package cn.skcks.docking.gb28181.media;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange()
public interface ZLMediaHttpService {
    @GetExchange("/")
    void test();
}
