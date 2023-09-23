package cn.skcks.docking.gb28181.sip.generic;

import cn.skcks.docking.gb28181.sip.header.XGBVerHeader;
import cn.skcks.docking.gb28181.sip.header.impl.XGBVerHeaderImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SipBuilder {
    public static SipFactory getSipFactory(){
        return SipFactory.getInstance();
    }

    @SneakyThrows
    public static MessageFactory getMessageFactory(){
        return getSipFactory().createMessageFactory();
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
    public static FromHeader createFromHeader(Address fromAddress, String fromTag) {
        return getHeaderFactory().createFromHeader(fromAddress, fromTag);
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
    public static XGBVerHeader createXGBVerHeader(int m,int n){
        return new XGBVerHeaderImpl(m,n);
    }
}
