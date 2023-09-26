package cn.skcks.docking.gb28181.sip.method.register;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.sip.message.Request;

@SuperBuilder
@Data
public class RegisterBuilder {
    @Getter
    private static final String method = Request.REGISTER;
    private String localIp;
    private int localPort;
    private String localId;
    private String targetIp;
    private int targetPort;
    private String targetId;
    private String transport;
}
