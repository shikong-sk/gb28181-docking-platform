package cn.skcks.docking.gb28181.core.sip.message.processor.message.request;

import cn.skcks.docking.gb28181.common.json.ResponseStatus;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.request.dto.MessageDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
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
import java.util.EventObject;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageRequestProcessor implements MessageProcessor {
    private final SipListener sipListener;
    private final DockingDeviceService deviceService;
    private final SipMessageSender sender;
    private final SipSubscribe subscribe;

    @PostConstruct
    @Override
    public void init() {
        sipListener.addRequestProcessor(Method.MESSAGE, this);
    }

    @Override
    public void process(EventObject eventObject) {
        RequestEvent requestEvent = (RequestEvent) eventObject;
        SIPRequest request = (SIPRequest)requestEvent.getRequest();
        String deviceId = SipUtil.getUserIdFromFromHeader(request);
        CallIdHeader callIdHeader = request.getCallIdHeader();

        byte[] content = request.getRawContent();
        MessageDTO messageDto = XmlUtils.parse(content, MessageDTO.class, GB28181Constant.CHARSET);
        log.debug("接收到的消息 => {}", messageDto);

        DockingDevice device = deviceService.getDevice(deviceId);
        String senderIp = request.getLocalAddress().getHostAddress();

        if(device == null){
            log.info("未找到相关设备信息 => {}", deviceId);
            Response response = response(request,Response.NOT_FOUND,"设备未注册");
            sender.send(senderIp,response);
            return;
        }

        Response ok = response(request, Response.OK, "OK");
        Response response;
        if(messageDto.getCmdType().equalsIgnoreCase(CmdType.KEEPALIVE)){
            response = ok;
            // 更新设备在线状态
            deviceService.online(device, response);
        } else if(messageDto.getCmdType().equalsIgnoreCase(CmdType.RECORD_INFO)){
            response = ok;
            RecordInfoResponseDTO dto = XmlUtils.parse(content, RecordInfoResponseDTO.class, GB28181Constant.CHARSET);
            String key = GenericSubscribe.Helper.getKey(CmdType.RECORD_INFO, dto.getDeviceId(), dto.getSn());
            Optional.ofNullable(subscribe.getRecordInfoSubscribe().getPublisher(key))
                    .ifPresentOrElse(publisher-> publisher.submit(dto),
                            ()-> log.warn("对应订阅 {} 已结束, 异常数据 => {}",key, dto));
        } else {
            response = response(request, Response.NOT_IMPLEMENTED, ResponseStatus.NOT_IMPLEMENTED.getMessage());
        }
        sender.send(senderIp, response);
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
