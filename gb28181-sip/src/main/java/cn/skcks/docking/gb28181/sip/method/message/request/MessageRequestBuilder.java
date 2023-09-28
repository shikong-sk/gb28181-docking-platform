package cn.skcks.docking.gb28181.sip.method.message.request;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.method.RequestBuilder;
import cn.skcks.docking.gb28181.sip.method.message.MessageBuilder;
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
public class MessageRequestBuilder extends RequestBuilder implements MessageBuilder {
    public Request createMessageRequest(String callId, long cSeq, byte[] content) {
        return createMessageRequest(callId, cSeq, content, "");
    }

    @SneakyThrows
    public Request createMessageRequest(String callId, long cSeq, byte[] content, String toTag) {
        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, content);
        if (StringUtils.isNotBlank(toTag)) {
            request.getToHeader().setTag(toTag);
        }
        return SipBuilder.addHeaders(request);
    }
}
