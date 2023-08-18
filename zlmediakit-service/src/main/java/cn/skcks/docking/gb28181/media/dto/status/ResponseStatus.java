package cn.skcks.docking.gb28181.media.dto.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("unused")
@Getter
@AllArgsConstructor
public enum ResponseStatus {
    Exception(-400,"代码抛异常"),
    InvalidArgs(-300, "参数不合法"),
    SqlFailed(-200, "sql执行失败"),
    AuthFailed(-100,"鉴权失败"),
    OtherFailed(-1,"业务代码执行失败"),
    Success(0,"执行成功");

    @JsonValue
    private final int code;
    private final String msg;

    @JsonCreator
    public static ResponseStatus fromCode(int code){
        for (ResponseStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
