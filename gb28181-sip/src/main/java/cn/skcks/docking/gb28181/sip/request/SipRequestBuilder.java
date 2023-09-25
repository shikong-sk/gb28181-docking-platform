package cn.skcks.docking.gb28181.sip.request;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipContentType;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import lombok.SneakyThrows;

import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.util.List;

public class SipRequestBuilder {
    @SneakyThrows
    public static Request createRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                            FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards){
        return SipBuilder.getMessageFactory().createRequest(requestURI, method,callId,cSeq,from,to,via,maxForwards);
    }

    @SneakyThrows
    public static <T> Request createRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                            FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards,
                                            ContentTypeHeader contentType, T content) {
        return SipBuilder.getMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, contentType, content);
    }

    @SneakyThrows
    public static <T> Request createXmlRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                            FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards, T content) {
        return SipBuilder.getMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, SipContentType.XML, content);
    }

    @SneakyThrows
    public static <T> Request createXmlRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                               FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards, T content, String charset) {
        return SipBuilder.getMessageFactory(charset).createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, SipContentType.XML, content);
    }
}
