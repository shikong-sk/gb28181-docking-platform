package cn.skcks.docking.gb28181.sip.method.subscribe.request;

import cn.skcks.docking.gb28181.sip.method.RequestBuilder;
import cn.skcks.docking.gb28181.sip.method.subscribe.SubscribeBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.sip.message.Request;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscribeRequestBuilder extends RequestBuilder implements SubscribeBuilder  {
    public Request createSubscribeRequest(){
        return null;
    }
}
