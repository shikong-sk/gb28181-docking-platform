package cn.skcks.docking.gb28181.core.sip.message.event.custom;

import javax.sip.Dialog;
import java.util.EventObject;

public class DeviceNotFoundEvent extends EventObject {
    private String callId;

    public DeviceNotFoundEvent(Dialog dialog) {
        super(dialog);
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
