package cn.skcks.docking.gb28181.sip.generic;

import lombok.SneakyThrows;

import javax.sip.header.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.List;

public class SipResponseBuilder {

    @SneakyThrows
    public static Response createResponse(int statusCode, Request request){
        return SipBuilder.getMessageFactory().createResponse(statusCode, request);
    }

    @SneakyThrows
    public static Response createResponse(int statusCode, CallIdHeader callId, CSeqHeader cSeq,
                                         FromHeader from, ToHeader to, List<ViaHeader> via, MaxForwardsHeader maxForwards){
        return SipBuilder.getMessageFactory().createResponse(statusCode,callId,cSeq,from,to,via,maxForwards);
    }

    @SneakyThrows
    public static <T> Response createResponse(int statusCode, Request request, ContentTypeHeader contentType, T content){
        return SipBuilder.getMessageFactory().createResponse(statusCode,request,contentType,content);
    }

    @SneakyThrows
    public static <T> Response createXmlResponse(int statusCode, Request request, T content){
        return SipBuilder.getMessageFactory().createResponse(statusCode,request, SipContentType.XML,content);
    }

    @SneakyThrows
    public static <T> Response createXmlResponse(int statusCode, Request request, T content, String charset) {
        return SipBuilder.getMessageFactory(charset).createResponse(statusCode, request, SipContentType.XML, content);
    }
}
