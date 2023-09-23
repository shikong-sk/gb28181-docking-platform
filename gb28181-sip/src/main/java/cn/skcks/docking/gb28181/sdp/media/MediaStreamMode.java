
package cn.skcks.docking.gb28181.sdp.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MediaStreamMode {
    UDP("UDP"),
    TCP_ACTIVE("TCP-ACTIVE"),
    TCP_PASSIVE("TCP-PASSIVE");

    @JsonValue
    private final String mode;

    @JsonCreator
    public static MediaStreamMode of(String mode) {
        for (MediaStreamMode m : values()) {
            if (m.getMode().equalsIgnoreCase(mode)) {
                return m;
            }
        }
        return null;
    }
}
