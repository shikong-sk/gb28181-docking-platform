package cn.skcks.docking.gb28181.sip.generic;

import lombok.SneakyThrows;

import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.util.List;

import static cn.skcks.docking.gb28181.sip.generic.SipBuilder.GB_VERSION;

public class SipRequestBuilder {
    @SneakyThrows
    public static Request createRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                        FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards) {
        return SipBuilder.addHeaders(
                SipBuilder.getMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards),
                GB_VERSION);
    }

    @SneakyThrows
    public static <T> Request createRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                            FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards,
                                            ContentTypeHeader contentType, T content) {
        return SipBuilder.addHeaders(
                SipBuilder.getMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, contentType, content),
                GB_VERSION);
    }

    @SneakyThrows
    public static <T> Request createXmlRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                               FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards, T content) {
        return SipBuilder.addHeaders(
                SipBuilder.getMessageFactory().createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, SipContentType.XML, content),
                GB_VERSION);
    }

    @SneakyThrows
    public static <T> Request createXmlRequest(SipURI requestURI, String method, CallIdHeader callId, CSeqHeader cSeq,
                                               FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards, T content, String charset) {
        return SipBuilder.addHeaders(
                SipBuilder.getMessageFactory(charset).createRequest(requestURI, method, callId, cSeq, from, to, via, maxForwards, SipContentType.XML, content),
                GB_VERSION);
    }
}
