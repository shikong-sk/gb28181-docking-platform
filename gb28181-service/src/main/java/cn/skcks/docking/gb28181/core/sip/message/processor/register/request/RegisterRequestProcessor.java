package cn.skcks.docking.gb28181.core.sip.message.processor.register.request;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.RemoteInfo;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import cn.skcks.docking.gb28181.core.sip.gb28181.sip.GbSipDate;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.auth.DigestServerAuthenticationHelper;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.header.SIPDateHeader;
import gov.nist.javax.sip.message.SIPRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Calendar;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegisterRequestProcessor implements MessageProcessor {
    private final SipListener sipListener;
    private final SipMessageSender sender;
    private final SipConfig sipConfig;
    private final DockingDeviceService dockingDeviceService;

    @PostConstruct
    @Override
    public void init(){
        sipListener.addProcessor(Method.REGISTER,this);
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
        DockingDevice device = dockingDeviceService.getDevice(deviceId);
        String senderIp = request.getLocalAddress().getHostAddress();
        RemoteInfo remoteInfo = SipUtil.getRemoteInfoFromRequest(request, false);
        log.debug("远程连接信息 => {}", remoteInfo);

        if(device == null){
            log.info("新注册的设备 deviceId => {}", deviceId);
        } else {
            if(dockingDeviceService.isOnline(deviceId) && dockingDeviceService.hasTransaction(deviceId)){
                SipTransactionInfo transactionInfo = dockingDeviceService.getTransaction(deviceId);
                if(request.getCallIdHeader().getCallId().equals(transactionInfo.getCallId())){
                    log.info("设备注册续订 deviceId => {}",deviceId);
                    registerDevice(deviceId, device, request, senderIp,remoteInfo);
                }
            }
        }

        String password = sipConfig.getPassword();
        Authorization authorization = request.getAuthorization();
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
        registerDevice(deviceId, device, request, senderIp, remoteInfo);
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

    private void registerDevice(String deviceId, DockingDevice device, SIPRequest request, String senderIp, RemoteInfo remoteInfo) {
        Response response = generateRegisterResponse(request);
        if(response.getStatusCode() != Response.OK){
            sender.send(senderIp, response);
            return;
        }

        if (device == null) {
            device = new DockingDevice();
            device.setStreamMode(ListeningPoint.UDP);
            device.setCharset(GB28181Constant.CHARSET);
            device.setGeoCoordSys(GB28181Constant.GEO_COORD_SYS);
            device.setDeviceId(deviceId);
            device.setOnLine(false);
        } else {
            if (ObjectUtils.isEmpty(device.getStreamMode())) {
                device.setStreamMode(ListeningPoint.UDP);
            }
            if (ObjectUtils.isEmpty(device.getCharset())) {
                device.setCharset(GB28181Constant.CHARSET);
            }
            if (ObjectUtils.isEmpty(device.getGeoCoordSys())) {
                device.setGeoCoordSys(GB28181Constant.GEO_COORD_SYS);
            }
        }

        device.setIp(remoteInfo.getIp());
        device.setPort(remoteInfo.getPort());
        device.setHostAddress(remoteInfo.getIp().concat(":").concat(String.valueOf(remoteInfo.getPort())));
        device.setLocalIp(senderIp);
        ViaHeader viaHeader = request.getTopmostViaHeader();
        String transport = viaHeader.getTransport();
        device.setTransport(ListeningPoint.TCP.equalsIgnoreCase(transport) ? ListeningPoint.TCP : ListeningPoint.UDP);

        int expires = request.getExpires().getExpires();
        device.setExpires(expires);
        // expires == 0 时 注销
        if (expires == 0) {
            device.setOnLine(false);
            log.info("设备注销 deviceId => {}", deviceId);
            dockingDeviceService.offline(device);
        } else {
            device.setRegisterTime(DateUtil.now());
            dockingDeviceService.online(device, response);
        }

        sender.send(senderIp, response);
    }
}
