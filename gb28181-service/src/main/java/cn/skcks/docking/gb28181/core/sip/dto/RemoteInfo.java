package cn.skcks.docking.gb28181.core.sip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RemoteInfo {
    private String ip;
    private int port;
}
