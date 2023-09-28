package cn.skcks.docking.gb28181.sip.method.notify.request;


import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.method.RequestBuilder;
import cn.skcks.docking.gb28181.sip.method.notify.NotifyBuilder;
import gov.nist.javax.sip.header.SubscriptionState;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.sip.message.Request;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotifyRequestBuilder extends RequestBuilder implements NotifyBuilder {
    @SneakyThrows
    public Request createNotifyRequest(String callId, long cSeq, String event, byte[] content, Request request){
        SIPRequest sipRequest = (SIPRequest) request;
        String toTag = sipRequest.getToTag();
        int expires = sipRequest.getExpires().getExpires();
        return createNotifyRequest(callId, cSeq, event, content, toTag, expires);
    }
                                       @SneakyThrows
    public Request createNotifyRequest(String callId, long cSeq, String event, byte[] content, String toTag, int expire) {
        SIPRequest notifyRequest = (SIPRequest) createNotifyRequest(callId, cSeq, event, content, toTag);
        SubscriptionState subscriptionState = (SubscriptionState) notifyRequest.getHeader(SubscriptionState.NAME);
        subscriptionState.setExpires(expire);
        return notifyRequest;
    }

    public Request createNotifyRequest(String callId, long cSeq, String event, byte[] content) {
        return createNotifyRequest(callId, cSeq, event, content, "");
    }

    @SneakyThrows
    public Request createNotifyRequest(String callId, long cSeq, String event, byte[] content, String toTag) {
        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, content);
        if (StringUtils.isNotBlank(toTag)) {
            request.getToHeader().setTag(toTag);
        }
        return SipBuilder.addHeaders(request,
                SipBuilder.createEventHeader(event),
                SipBuilder.createSubscriptionStateHeader("active"));
    }
}
