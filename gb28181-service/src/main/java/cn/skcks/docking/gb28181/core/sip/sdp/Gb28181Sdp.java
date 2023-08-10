package cn.skcks.docking.gb28181.core.sip.sdp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sdp.SessionDescription;

@Builder
@Data
public class Gb28181Sdp {
    private SessionDescription baseSdb;
    private String ssrc;
    private String mediaDescription;
}
