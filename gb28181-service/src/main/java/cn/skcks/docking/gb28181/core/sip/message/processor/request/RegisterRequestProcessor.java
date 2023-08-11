package cn.skcks.docking.gb28181.core.sip.message.processor.request;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.RemoteInfo;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.sip.GbSipDate;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.auth.DigestServerAuthenticationHelper;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.DockingDeviceService;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.header.SIPDate;
import gov.nist.javax.sip.header.SIPDateHeader;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegisterRequestProcessor implements MessageProcessor {
    private final static String METHOD = "REGISTER";

    private final SipListener sipListener;
    private final SipMessageSender sender;
    private final SipConfig sipConfig;
    private final DockingDeviceService dockingDeviceService;

    @PostConstruct
    private void init(){
        sipListener.addProcessor(METHOD,this);
    }

    @SneakyThrows
    @Override
    public void process(RequestEvent requestEvent) {
        SIPRequest request = (SIPRequest)requestEvent.getRequest();
        FromHeader fromHeader = request.getFrom();
        Address address = fromHeader.getAddress();
        log.debug("From {}",address);

        SipUri uri = (SipUri)address.getURI();
        String deviceId = uri.getUser();
        log.debug("请求注册 设备id => {}", deviceId);
        DockingDevice deviceInfo = dockingDeviceService.getDeviceInfo(deviceId);
        if(deviceInfo == null){
            log.info("新注册的设备 deviceId => {}", deviceId);
        }

        RemoteInfo remoteInfo = SipUtil.getRemoteInfoFromRequest(request, false);
        log.debug("远程连接信息 => {}", remoteInfo);

        String password = sipConfig.getPassword();
        Authorization authorization = request.getAuthorization();
        String senderIp = request.getLocalAddress().getHostAddress();
        if(authorization == null && StringUtils.isNotBlank(password)){
            Response response = getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
            DigestServerAuthenticationHelper.generateChallenge(getHeaderFactory(),response,sipConfig.getDomain());
            sender.send(senderIp,response);
            return;
        }

        log.debug("认证信息 => {}", authorization);
        boolean authPass = StringUtils.isBlank(password) ||
                DigestServerAuthenticationHelper.doAuthenticatePlainTextPassword(request,password);
        if(!authPass){
            Response response = getMessageFactory().createResponse(Response.FORBIDDEN, request);
            response.setReasonPhrase("认证失败");
            log.info("设备注册信息认证失败 deviceId => {}", deviceId);
            sender.send(senderIp,response);
            return;
        }


        log.debug("设备 deviceId => {}, 认证通过", deviceId);
        Response response = generateRegisterResponse(request);
        if(response.getStatusCode() != Response.OK){
            sender.send(senderIp, response);
            return;
        }


        if (deviceInfo == null) {
            deviceInfo = new DockingDevice();
            deviceInfo.setStreamMode(ListeningPoint.UDP);
            deviceInfo.setCharset("GB2312");
            deviceInfo.setGeoCoordSys("WGS84");
            deviceInfo.setDeviceId(deviceId);
            deviceInfo.setOnLine(false);
        } else {
            if (ObjectUtils.isEmpty(deviceInfo.getStreamMode())) {
                deviceInfo.setStreamMode(ListeningPoint.UDP);
            }
            if (ObjectUtils.isEmpty(deviceInfo.getCharset())) {
                deviceInfo.setCharset("GB2312");
            }
            if (ObjectUtils.isEmpty(deviceInfo.getGeoCoordSys())) {
                deviceInfo.setGeoCoordSys("WGS84");
            }
        }

        deviceInfo.setIp(remoteInfo.getIp());
        deviceInfo.setPort(remoteInfo.getPort());
        deviceInfo.setHostAddress(remoteInfo.getIp().concat(":").concat(String.valueOf(remoteInfo.getPort())));
        deviceInfo.setLocalIp(senderIp);

        int expires = request.getExpires().getExpires();
        deviceInfo.setExpires(expires);
        // expires == 0 时 注销
        if (expires == 0) {
            log.info("设备注销 deviceId => {}", deviceId);
        } else {
            deviceInfo.setRegisterTime(DateUtil.now());
            SipTransactionInfo sipTransactionInfo = new SipTransactionInfo((SIPResponse)response);
        }

        sender.send(senderIp, response);
    }

    @SneakyThrows
    private Response generateRegisterResponse(Request request){
        SIPRequest sipRequest = (SIPRequest) request;
        ExpiresHeader expires = sipRequest.getExpires();
        if(expires == null){
            return getMessageFactory().createResponse(Response.BAD_REQUEST, request);
        }

        Response response = getMessageFactory().createResponse(Response.OK, request);
        // 添加date头
        SIPDateHeader dateHeader = new SIPDateHeader();
        // GB28181 日期
        GbSipDate gbSipDate = new GbSipDate(Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());
        dateHeader.setDate(gbSipDate);

        response.addHeader(dateHeader);
        response.addHeader(sipRequest.getContactHeader());
        response.addHeader(expires);

        return response;
    }
}
