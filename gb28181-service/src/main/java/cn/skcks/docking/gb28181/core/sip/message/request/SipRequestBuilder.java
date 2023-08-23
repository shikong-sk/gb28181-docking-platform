package cn.skcks.docking.gb28181.core.sip.message.request;

import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import cn.skcks.docking.gb28181.core.sip.message.MessageHelper;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DependsOn({"sipConfig"})
@Component
public class SipRequestBuilder implements ApplicationContextAware {
    private static SipConfig sipConfig;

    private static SipFactory getSipFactory(){
        return SipFactory.getInstance();
    }

    @SneakyThrows
    private static SipURI getSipURI(String id,String address){
        return MessageHelper.createSipURI(id, address);
    }

    @SneakyThrows
    private static Address getAddress(SipURI uri){
        return MessageHelper.createAddress(uri);
    }

    @SneakyThrows
    private static FromHeader getFromHeader(Address fromAddress,String fromTag){
        return MessageHelper.createFromHeader(fromAddress, fromTag);
    }

    @SneakyThrows
    private static ToHeader getToHeader(Address toAddress,String toTag){
        return MessageHelper.createToHeader(toAddress, toTag);
    }

    @SneakyThrows
    private static MaxForwardsHeader getMaxForwardsHeader(int maxForwards){
        return getSipFactory().createHeaderFactory().createMaxForwardsHeader(maxForwards);
    }

    @SneakyThrows
    private static List<ViaHeader> getDeviceViaHeaders(DockingDevice device, String viaTag){
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(device.getLocalIp(), sipConfig.getPort(), device.getTransport(), viaTag);
        viaHeader.setRPort();
        return Collections.singletonList(viaHeader);
    }

    public static Request createMessageRequest(DockingDevice device, String content, String viaTag, String fromTag, String toTag, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        Request request = null;
        // sip uri
        SipURI requestURI = getSipURI(device.getDeviceId(), device.getHostAddress());
        // via
        List<ViaHeader> viaHeaders = getDeviceViaHeaders(device, viaTag);
        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, fromTag);
        // to
        SipURI toSipURI = getSipURI(device.getDeviceId(), device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, toTag);

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);
        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.MESSAGE);

        request = getSipFactory().createMessageFactory().createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);

        request.addHeader(SipUtil.createUserAgentHeader());

        ContentTypeHeader contentTypeHeader = getSipFactory().createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
        request.setContent(content, contentTypeHeader);
        return request;
    }

    public static Request createInviteRequest(DockingDevice device, String channelId, String content, String viaTag, String fromTag, String toTag, String ssrc, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        Request request = null;
        // 请求行
        SipURI requestLine = getSipURI(channelId, device.getHostAddress());
        // via
        List<ViaHeader> viaHeaders = getDeviceViaHeaders(device, viaTag);

        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, fromTag); // 必须要有标记，否则无法创建会话，无法回应ack
        // to
        SipURI toSipURI = getSipURI(channelId, device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, null);

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.INVITE);
        request = getSipFactory().createMessageFactory().createRequest(requestLine, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

        request.addHeader(SipUtil.createUserAgentHeader());

        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getLocalIp() + ":" + sipConfig.getPort()));
        // Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getHost().getIp()+":"+device.getHost().getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));
        // Subject
        SubjectHeader subjectHeader = getSipFactory().createHeaderFactory().createSubjectHeader(String.format("%s:%s,%s:%s", channelId, ssrc, sipConfig.getId(), 0));
        request.addHeader(subjectHeader);
        ContentTypeHeader contentTypeHeader = getSipFactory().createHeaderFactory().createContentTypeHeader("APPLICATION", "SDP");
        request.setContent(content, contentTypeHeader);
        return request;
    }

    public static Request createPlaybackInviteRequest(DockingDevice device, String channelId, String content, String viaTag, String fromTag, String toTag, CallIdHeader callIdHeader, String ssrc) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        Request request = null;
        // 请求行
        SipURI requestLine = getSipURI(channelId, device.getHostAddress());
        // via
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(device.getLocalIp(), sipConfig.getPort(), device.getTransport(), viaTag);
        viaHeader.setRPort();
        viaHeaders.add(viaHeader);
        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, fromTag); // 必须要有标记，否则无法创建会话，无法回应ack
        // to
        SipURI toSipURI = getSipURI(channelId, device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, null);

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.INVITE);
        request = getSipFactory().createMessageFactory().createRequest(requestLine, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getLocalIp() + ":" + sipConfig.getPort()));
        // Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getHost().getIp()+":"+device.getHost().getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));

        request.addHeader(SipUtil.createUserAgentHeader());

        // Subject
        SubjectHeader subjectHeader = getSipFactory().createHeaderFactory().createSubjectHeader(String.format("%s:%s,%s:%s", channelId, ssrc, sipConfig.getId(), 0));
        request.addHeader(subjectHeader);

        ContentTypeHeader contentTypeHeader = getSipFactory().createHeaderFactory().createContentTypeHeader("APPLICATION", "SDP");
        request.setContent(content, contentTypeHeader);
        return request;
    }

    public static Request createByteRequest(DockingDevice device, String channelId, SipTransactionInfo transactionInfo) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        Request request = null;
        // 请求行
        SipURI requestLine = getSipURI(channelId, device.getHostAddress());
        // via
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(device.getLocalIp(), sipConfig.getPort(), device.getTransport(), SipUtil.generateViaTag());
        viaHeaders.add(viaHeader);
        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, transactionInfo.getFromTag());
        // to
        SipURI toSipURI = getSipURI(channelId, device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, transactionInfo.getToTag());

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.BYE);
        CallIdHeader callIdHeader = getSipFactory().createHeaderFactory().createCallIdHeader(transactionInfo.getCallId());
        request = getSipFactory().createMessageFactory().createRequest(requestLine, Request.BYE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

        request.addHeader(SipUtil.createUserAgentHeader());

        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getLocalIp() + ":" + sipConfig.getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));

        request.addHeader(SipUtil.createUserAgentHeader());

        return request;
    }

    public static Request createSubscribeRequest(DockingDevice device, String content, SIPRequest requestOld, Integer expires, String event, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        Request request = null;
        // sipuri
        SipURI requestURI = getSipURI(device.getDeviceId(), device.getHostAddress());
        // via
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(device.getLocalIp(), sipConfig.getPort(),
                device.getTransport(), SipUtil.generateViaTag());
        viaHeader.setRPort();
        viaHeaders.add(viaHeader);
        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, requestOld == null ? SipUtil.generateFromTag() : requestOld.getFromTag());
        // to
        SipURI toSipURI = getSipURI(device.getDeviceId(), device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, requestOld == null ? null : requestOld.getToTag());

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.SUBSCRIBE);

        request = getSipFactory().createMessageFactory().createRequest(requestURI, Request.SUBSCRIBE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);


        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getLocalIp() + ":" + sipConfig.getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));

        // Expires
        ExpiresHeader expireHeader = getSipFactory().createHeaderFactory().createExpiresHeader(expires);
        request.addHeader(expireHeader);

        // Event
        EventHeader eventHeader = getSipFactory().createHeaderFactory().createEventHeader(event);

        int random = (int) Math.floor(Math.random() * 10000);
        eventHeader.setEventId(String.valueOf(random));
        request.addHeader(eventHeader);

        ContentTypeHeader contentTypeHeader = getSipFactory().createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
        request.setContent(content, contentTypeHeader);

        request.addHeader(SipUtil.createUserAgentHeader());

        return request;
    }

    public static SIPRequest createInfoRequest(DockingDevice device, String channelId, String content, SipTransactionInfo transactionInfo)
            throws SipException, ParseException, InvalidArgumentException {
        if (device == null || transactionInfo == null) {
            return null;
        }
        SIPRequest request = null;
        // 请求行
        SipURI requestLine = getSipURI(channelId, device.getHostAddress());
        // via
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(device.getLocalIp(), sipConfig.getPort(), device.getTransport(), SipUtil.generateViaTag());
        viaHeaders.add(viaHeader);
        // from
        SipURI fromSipURI = getSipURI(sipConfig.getId(), sipConfig.getDomain());
        Address fromAddress = MessageHelper.createAddress(fromSipURI);
        FromHeader fromHeader = getFromHeader(fromAddress, transactionInfo.getFromTag());
        // to
        SipURI toSipURI = getSipURI(channelId, device.getHostAddress());
        Address toAddress = MessageHelper.createAddress(toSipURI);
        ToHeader toHeader = getToHeader(toAddress, transactionInfo.getToTag());

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(getCSeq(), Request.INFO);
        CallIdHeader callIdHeader = getSipFactory().createHeaderFactory().createCallIdHeader(transactionInfo.getCallId());
        request = (SIPRequest) getSipFactory().createMessageFactory().createRequest(requestLine, Request.INFO, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);

        request.addHeader(SipUtil.createUserAgentHeader());

        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), device.getLocalIp() + ":" + sipConfig.getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));

        request.addHeader(SipUtil.createUserAgentHeader());

        if (content != null) {
            ContentTypeHeader contentTypeHeader = getSipFactory().createHeaderFactory().createContentTypeHeader("Application",
                    "MANSRTSP");
            request.setContent(content, contentTypeHeader);
        }
        return request;
    }

    public static Request createAckRequest(String localIp, SipURI sipURI, SIPResponse sipResponse) throws ParseException, InvalidArgumentException, PeerUnavailableException {


        // via
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = getSipFactory().createHeaderFactory().createViaHeader(localIp, sipConfig.getPort(), sipResponse.getTopmostViaHeader().getTransport(), SipUtil.generateViaTag());
        viaHeaders.add(viaHeader);

        // Forwards
        MaxForwardsHeader maxForwards = getSipFactory().createHeaderFactory().createMaxForwardsHeader(70);

        // ceq
        CSeqHeader cSeqHeader = getSipFactory().createHeaderFactory().createCSeqHeader(sipResponse.getCSeqHeader().getSeqNumber(), Request.ACK);

        Request request = getSipFactory().createMessageFactory().createRequest(sipURI, Request.ACK, sipResponse.getCallIdHeader(), cSeqHeader, sipResponse.getFromHeader(), sipResponse.getToHeader(), viaHeaders, maxForwards);

        request.addHeader(SipUtil.createUserAgentHeader());

        Address concatAddress = MessageHelper.createAddress(getSipURI(sipConfig.getId(), localIp + ":" + sipConfig.getPort()));
        request.addHeader(getSipFactory().createHeaderFactory().createContactHeader(concatAddress));

        request.addHeader(SipUtil.createUserAgentHeader());

        return request;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        sipConfig = applicationContext.getBean(SipConfig.class);
    }


    public static long getCSeq() {
        String key = CacheUtil.getKey(CacheUtil.SIP_C_SEQ_PREFIX,sipConfig.getId());

        long result = 1L;
        if(RedisUtil.KeyOps.hasKey(key)){
            try {
                result =  RedisUtil.StringOps.incrBy(key,1L);
            } finally {
                if (result > Integer.MAX_VALUE) {
                    RedisUtil.StringOps.set(key, String.valueOf(1L));
                    result = 1L;
                }
            }
        } else {
            RedisUtil.StringOps.set(key, String.valueOf(result));
        }

        return result;
    }
}
