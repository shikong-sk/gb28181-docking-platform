package cn.skcks.docking.gb28181.core.sip.message.processor.request;

import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.RemoteInfo;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.auth.DigestServerAuthenticationHelper;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.DockingDeviceService;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.message.SIPRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.header.FromHeader;
import javax.sip.message.Response;

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
        if(authorization == null && StringUtils.isNotBlank(password)){
            Response response = getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
            DigestServerAuthenticationHelper.generateChallenge(getHeaderFactory(),response,sipConfig.getDomain());
            sender.send(request.getLocalAddress().getHostAddress(),response);
            return;
        }

        log.debug("认证信息 => {}", authorization);
        boolean authPass = StringUtils.isBlank(password) ||
                DigestServerAuthenticationHelper.doAuthenticatePlainTextPassword(request,password);
        if(!authPass){
            Response response = getMessageFactory().createResponse(Response.FORBIDDEN, request);
            response.setReasonPhrase("认证失败");
            log.info("设备注册信息认证失败 deviceId => {}", deviceId);
            sender.send(request.getLocalAddress().getHostAddress(),response);
            return;
        }
        log.debug("设备 deviceId => {}, 认证通过", deviceId);
    }
}
