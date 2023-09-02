package cn.skcks.docking.gb28181.core.sip.dto;

import gov.nist.javax.sip.message.SIPResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SipTransactionInfo {

    private String callId;
    private String fromTag;
    private String toTag;
    private String viaBranch;
    private String ssrc;

    public SipTransactionInfo(SIPResponse response) {
        this.callId = response.getCallIdHeader().getCallId();
        this.fromTag = response.getFromTag();
        this.toTag = response.getToTag();
        this.viaBranch = response.getTopmostViaHeader().getBranch();
    }

    public SipTransactionInfo(SIPResponse response, String ssrc) {
        this(response);
        this.ssrc = ssrc;
    }
}
