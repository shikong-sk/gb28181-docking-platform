package cn.skcks.docking.gb28181.sdp;

import lombok.Builder;
import lombok.Data;

import javax.sdp.SessionDescription;

@Builder
@Data
public class Gb28181Sdp {
    private SessionDescription baseSdb;
    private String ssrc;
    private String mediaDescription;
}
