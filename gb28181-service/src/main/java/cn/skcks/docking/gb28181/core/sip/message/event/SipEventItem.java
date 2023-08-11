package cn.skcks.docking.gb28181.core.sip.message.event;

import cn.skcks.docking.gb28181.common.json.ResponseStatus;
import cn.skcks.docking.gb28181.core.sip.message.event.custom.DeviceNotFoundEvent;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.Data;

import javax.sip.DialogTerminatedEvent;
import javax.sip.ResponseEvent;
import javax.sip.TimeoutEvent;
import java.util.EventObject;

@Data
public class SipEventItem {
    private int statusCode;
    private SipEventType type;
    private String msg;
    private String callId;
    private final EventObject event;

    public SipEventItem(EventObject eventObject) {
        event = eventObject;
        msg = ResponseStatus.UNDEFINED.getMessage();
        statusCode = ResponseStatus.UNDEFINED.getCode();
        if(eventObject instanceof ResponseEvent responseEvent){
            SIPResponse response = (SIPResponse)responseEvent.getResponse();
            type = SipEventType.Response;
            if (response != null) {
                msg = response.getReasonPhrase();
                statusCode = response.getStatusCode();
                callId = response.getCallIdHeader().getCallId();
            }
        } else if(eventObject instanceof TimeoutEvent timeoutEvent){
            type = SipEventType.TimeOut;
            msg = "消息超时未回复";
            statusCode = ResponseStatus.REQUEST_TIMEOUT.getCode();

            SIPRequest request;
            if (timeoutEvent.isServerTransaction()) {
                request = ((SIPRequest)timeoutEvent.getServerTransaction().getRequest());
            } else {
                request = ((SIPRequest)timeoutEvent.getClientTransaction().getRequest());
            }
            callId = request.getCallIdHeader().getCallId();
        } else if(eventObject instanceof DialogTerminatedEvent dialogTerminatedEvent){
            type = SipEventType.End;
            msg = "会话已结束";
            statusCode = ResponseStatus.GONE.getCode();
            callId = dialogTerminatedEvent.getDialog().getCallId().getCallId();
        } else if(eventObject instanceof DeviceNotFoundEvent deviceNotFoundEvent){
            type = SipEventType.DeviceNotFound;
            msg = "设备未找到";
            statusCode = ResponseStatus.NOT_FOUND.getCode();
            callId = deviceNotFoundEvent.getCallId();
        }
    }
}
