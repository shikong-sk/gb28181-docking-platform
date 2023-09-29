package cn.skcks.docking.gb28181.sip.generic;

import cn.skcks.docking.gb28181.constant.GB28181Constant;
import cn.skcks.docking.gb28181.sip.header.XGBVerHeader;
import cn.skcks.docking.gb28181.sip.header.impl.XGBVerHeaderImpl;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SipBuilder {
    @Setter
    public static String DEFAULT_CHARSET = GB28181Constant.CHARSET;
    @Setter
    public static XGBVerHeader GB_VERSION = XGBVerHeaderImpl.GB28181_2016;

    public static SipFactory getSipFactory(){
        return SipFactory.getInstance();
    }

    public static UserAgentHeader userAgentHeader = SipUtil.getUserAgentHeader();

    @SneakyThrows
    public static MessageFactory getMessageFactory(){
        MessageFactoryImpl messageFactory = (MessageFactoryImpl)getSipFactory().createMessageFactory();
        messageFactory.setDefaultContentEncodingCharset(DEFAULT_CHARSET);
        messageFactory.setDefaultUserAgentHeader(userAgentHeader);
        log.debug("将使用 {} 编码 sip 消息", DEFAULT_CHARSET);
        return messageFactory;
    }

    @SneakyThrows
    public static MessageFactory getMessageFactory(String charset){
        MessageFactoryImpl messageFactory = (MessageFactoryImpl)getSipFactory().createMessageFactory();
        messageFactory.setDefaultContentEncodingCharset(charset);
        messageFactory.setDefaultUserAgentHeader(userAgentHeader);
        log.debug("将使用 {} 编码 sip 消息", charset);
        return messageFactory;
    }

    @SneakyThrows
    public static AddressFactory getAddressFactory() {
        return getSipFactory().createAddressFactory();
    }

    @SneakyThrows
    public static HeaderFactory getHeaderFactory() {
        return getSipFactory().createHeaderFactory();
    }

    @SneakyThrows
    public static String createHostAddress(String ip, int port) {
        return StringUtils.joinWith(":", ip, port);
    }

    @SneakyThrows
    public static SipURI createSipURI(String id, String address) {
        return getAddressFactory().createSipURI(id, address);
    }

    @SneakyThrows
    public static Address createAddress(SipURI uri) {
        return getAddressFactory().createAddress(uri);
    }

    @SneakyThrows
    public static ToHeader createToHeader(Address toAddress, String toTag) {
        return getHeaderFactory().createToHeader(toAddress, toTag);
    }

    @SneakyThrows
    public static ToHeader createToHeader(Address toAddress) {
        return createToHeader(toAddress, null);
    }

    @SneakyThrows
    public static FromHeader createFromHeader(Address fromAddress, String fromTag) {
        return getHeaderFactory().createFromHeader(fromAddress, fromTag);
    }

    @SneakyThrows
    public static FromHeader createFromHeader(Address fromAddress) {
        return createFromHeader(fromAddress, null);
    }

    @SneakyThrows
    public static CSeqHeader createCSeqHeader(long cSeq, String method){
        return getHeaderFactory().createCSeqHeader(cSeq, method);
    }


    @SneakyThrows
    public static MaxForwardsHeader createMaxForwardsHeader(int maxForwards) {
        return getHeaderFactory().createMaxForwardsHeader(maxForwards);
    }

    @SneakyThrows
    public static ViaHeader createViaHeader(String ip, int port, String transport, String viaTag){
        ViaHeader viaHeader = getHeaderFactory().createViaHeader(ip, port, transport, viaTag);
        viaHeader.setRPort();
        return viaHeader;
    }

    @SneakyThrows
    public static List<ViaHeader> createViaHeaders(String ip, int port, String transport, String viaTag) {
        return Collections.singletonList(createViaHeader(ip, port, transport, viaTag));
    }


    @SneakyThrows
    public static ContactHeader createContactHeader(Address address){
        return getHeaderFactory().createContactHeader(address);
    };

    @SneakyThrows
    public static ContentTypeHeader createContentTypeHeader(String contentType, String subType){
        return getHeaderFactory().createContentTypeHeader(contentType, subType);
    }

    @SneakyThrows
    public static SubjectHeader createSubjectHeader(String subject){
        return getHeaderFactory().createSubjectHeader(subject);
    }

    @SneakyThrows
    public static ContentTypeHeader createSDPContentTypeHeader(){
        return getHeaderFactory().createContentTypeHeader("APPLICATION", "SDP");
    }

    public static UserAgentHeader createUserAgentHeader(String userAgent){
        return createUserAgentHeader(Arrays.stream(StringUtils.split(userAgent, StringUtils.SPACE)).toList());
    }

    @SneakyThrows
    public static UserAgentHeader createUserAgentHeader(List<String> product){
        return getHeaderFactory().createUserAgentHeader(product);
    }

    @SneakyThrows
    public static CallIdHeader createCallIdHeader(String callId){
        return getHeaderFactory().createCallIdHeader(callId);
    }

    @SneakyThrows
    public static ExpiresHeader createExpiresHeader(int expires){
        return getHeaderFactory().createExpiresHeader(expires);
    }

    public static DateHeader createDateHeader(Calendar date){
        return getHeaderFactory().createDateHeader(date);
    }

    @SneakyThrows
    public static EventHeader createEventHeader(String event){
        return getHeaderFactory().createEventHeader(event);
    }

    @SneakyThrows
    public static SubscriptionStateHeader createSubscriptionStateHeader(String state){
        return getHeaderFactory().createSubscriptionStateHeader(state);
    }

    @SneakyThrows
    public static XGBVerHeader createXGBVerHeader(int m,int n){
        return new XGBVerHeaderImpl(m,n);
    }

    public static Response addHeaders(Response response,Header ...headers){
        for (Header header : headers) {
            response.addHeader(header);
        }
        return response;
    }

    public static Request addHeaders(Request request, Header ...headers){
        for (Header header : headers) {
            request.addHeader(header);
        }
        return request;
    }
}
