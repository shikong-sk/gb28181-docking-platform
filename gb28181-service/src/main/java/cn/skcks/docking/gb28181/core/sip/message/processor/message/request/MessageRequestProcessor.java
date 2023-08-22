package cn.skcks.docking.gb28181.core.sip.message.processor.message.request;

import cn.skcks.docking.gb28181.common.json.ResponseStatus;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.request.dto.MessageDTO;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRequestProcessor implements MessageProcessor {
    private final SipListener sipListener;
    private final DockingDeviceService deviceService;
    private final SipMessageSender sender;

    @PostConstruct
    @Override
    public void init() {
        sipListener.addProcessor(Method.MESSAGE, this);
    }

    @Override
    public void process(RequestEvent requestEvent) {
        SIPRequest request = (SIPRequest)requestEvent.getRequest();
        String deviceId = SipUtil.getUserIdFromFromHeader(request);
        CallIdHeader callIdHeader = request.getCallIdHeader();

        MessageDTO messageDto = XmlUtils.parse(request.getRawContent(), MessageDTO.class, GB28181Constant.CHARSET);
        log.debug("接收到的消息 => {}", messageDto);

        DockingDevice device = deviceService.getDevice(deviceId);
        String senderIp = request.getLocalAddress().getHostAddress();

        if(device == null){
            log.info("未找到相关设备信息 => {}", deviceId);
            Response response = response(request,Response.NOT_FOUND,"设备未注册");
            sender.send(senderIp,response);
            return;
        }

        Response response;
        if(messageDto.getCmdType().equalsIgnoreCase(CmdType.KEEPALIVE)){
            response = response(request, Response.OK, "OK");
            // 更新设备在线状态
            deviceService.online(device, response);
        } else {
            response = response(request, Response.NOT_IMPLEMENTED, ResponseStatus.NOT_IMPLEMENTED.getMessage());
        }
        sender.send(senderIp,response);
    }

    @SneakyThrows
    public Response response(SIPRequest request, int status, String message){
        if (request.getToHeader().getTag() == null) {
            request.getToHeader().setTag(SipUtil.generateTag());
        }
        SIPResponse response = (SIPResponse)getMessageFactory().createResponse(status, request);
        if (message != null) {
            response.setReasonPhrase(message);
        }
        return  response;
    }
}
