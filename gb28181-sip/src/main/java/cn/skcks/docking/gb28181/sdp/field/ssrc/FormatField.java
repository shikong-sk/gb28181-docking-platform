package cn.skcks.docking.gb28181.sdp.field.ssrc;

import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.SDPField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FormatField extends SDPField {
    public static final String FORMAT_FIELD_NAME = "f";
    private static final String FORMAT_FIELD = FORMAT_FIELD_NAME + "=";
    public FormatField() {
        super(FORMAT_FIELD);
    }

    /**
     * 视频编码格式
     */
    private String videoFormat = "";
    /**
     * 视频分辨率
     */
    private String videoRatio = "";
    /**
     * 视频帧率
     */
    private String videoFrame = "";
    /**
     * 视频码率类型
     */
    private String videoRateType = "";
    /**
     * 视频码率大小
     */
    private String videoRateNum = "";
    /**
     * 音频编码格式
     */
    private String audioFormat = "";
    /**
     * 音频码率大小
     */
    private String audioRateNum = "";
    /**
     * 音频采样率
     */
    private String audioSampling = "";

    @SneakyThrows
    @Override
    public String encode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FORMAT_FIELD);
        if(!StringUtils.isAllBlank(videoFormat, videoRatio, videoFrame, videoRateType, videoRateNum,audioFormat, audioRateNum, audioSampling)){
            String video = StringUtils.joinWith("/", "v", videoFormat, videoRatio, videoFrame, videoRateType, videoRateNum);
            String audio = StringUtils.joinWith("/", "a", audioFormat, audioRateNum, audioSampling);
            stringBuilder.append(StringUtils.joinWith("/",video,audio));
        }
        stringBuilder.append(Separators.NEWLINE);
        return stringBuilder.toString();
    }

    public String toString(){
        return encode();
    }
}
