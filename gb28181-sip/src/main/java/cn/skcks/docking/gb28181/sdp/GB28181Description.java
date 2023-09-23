package cn.skcks.docking.gb28181.sdp;

import gov.nist.javax.sdp.SessionDescriptionImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.SdpException;
import javax.sdp.SessionDescription;

@Slf4j
@Setter
@Getter
public class GB28181Description extends SessionDescriptionImpl implements SessionDescription {
    private SsrcField ssrcField;
    private SessionDescriptionImpl sessionDescription;

    public GB28181Description(){
        super();
    }

    public GB28181Description(SessionDescription sessionDescription) throws SdpException {
        super(sessionDescription);
    }

    @SneakyThrows
    public static GB28181Description build(SessionDescription sessionDescription){
        return new GB28181Description(sessionDescription);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(getSsrcField() == null ? "" : getSsrcField().toString());
        // return "+";
        return sb.toString();
    }
}
