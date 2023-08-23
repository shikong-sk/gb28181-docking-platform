package cn.skcks.docking.gb28181.core.sip.gb28181.sdp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StreamMode {
    UDP("UDP"),
    TCP_ACTIVE("TCP-ACTIVE"),
    TCP_PASSIVE("TCP-PASSIVE");

    @JsonValue
    private final String mode;

    @JsonCreator
    public static StreamMode of(String mode) {
        for (StreamMode m : values()) {
            if (m.getMode().equalsIgnoreCase(mode)) {
                return m;
            }
        }
        return null;
    }
}
