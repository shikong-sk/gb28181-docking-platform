package cn.skcks.docking.gb28181.core.sip.message.processor.request;

import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.RemoteInfo;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.auth.DigestServerAuthenticationHelper;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.message.SIPRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

        RemoteInfo remoteInfo = SipUtil.getRemoteInfoFromRequest(request, false);
        log.debug("远程连接信息 => {}", remoteInfo);

        String password = sipConfig.getPassword();
        Authorization authorization = request.getAuthorization();
        log.debug("认证信息 => {}", authorization);

        Response response = getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
        DigestServerAuthenticationHelper.generateChallenge(getHeaderFactory(),response,sipConfig.getDomain());
        sender.send(request.getLocalAddress().getHostAddress(),response);
    }
}
