package cn.skcks.docking.gb28181.sdp;

import cn.skcks.docking.gb28181.sdp.field.ssrc.FormatField;
import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import gov.nist.javax.sdp.SessionDescriptionImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    private FormatField formatField = new FormatField();
    private SessionDescriptionImpl sessionDescription;

    public GB28181Description(){
        super();
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public GB28181Description(GB28181Description gb28181Description) throws SdpException {
        super(gb28181Description);
        ssrcField = gb28181Description.getSsrcField();
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
        sb.append(getFormatField() == null ? "" : getFormatField().toString());
        return sb.toString();
    }
}
