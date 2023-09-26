package cn.skcks.docking.gb28181.sip.method.register;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.sip.message.Request;

@SuperBuilder
@Data
public class RegisterBuilder {
    @Getter
    private static final String method = Request.REGISTER;
}
