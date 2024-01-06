package cn.skcks.docking.gb28181.sdp.field.ssrc.parser;

import cn.skcks.docking.gb28181.sdp.field.ssrc.FormatField;
import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import gov.nist.javax.sdp.fields.SDPField;
import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.SDPParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

@Slf4j
public class FormatFieldParser extends SDPParser {
    public FormatFieldParser(String ssrcField) {
        this.lexer = new Lexer("charLexer", ssrcField);
    }

    public FormatField formatField() throws ParseException {
        try {
            this.lexer.match('f');
            this.lexer.SPorHT();
            this.lexer.match('=');
            this.lexer.SPorHT();

            FormatField formatField = new FormatField();
            String rest = lexer.getRest().trim();

            String[] split = StringUtils.split(rest, 'a');
            if(split.length == 0){
                return formatField;
            }

            log.info("{}", (Object) split);
            String video = split[0];
            String[] videoParams = StringUtils.split(video,"/");
            log.info("videoParams {}", (Object) videoParams);
            if(videoParams.length > 1){
                formatField.setVideoFormat(videoParams[1]);
            }
            if(videoParams.length > 2){
                formatField.setVideoRatio(videoParams[2]);
            }
            if(videoParams.length > 3){
                formatField.setVideoFrame(videoParams[3]);
            }
            if(videoParams.length > 4){
                formatField.setVideoRateType(videoParams[4]);
            }
            if(videoParams.length > 5){
                formatField.setVideoRateNum(videoParams[5]);
            }
            if(split.length < 2){
                return formatField;
            }
            String audio = split[1];
            String[] audioParams = audio.split("/");
            log.info("audioParams {}", (Object) audioParams);
            if(audioParams.length > 0){
                formatField.setAudioFormat(audioParams[0]);
            }
            if(audioParams.length > 1){
                formatField.setAudioRateNum(audioParams[1]);
            }
            if(audioParams.length > 2){
                formatField.setAudioSampling(audioParams[2]);
            }
            return formatField;
        } catch (Exception e) {
            e.printStackTrace();
            throw lexer.createParseException();
        }
    }

    public SDPField parse() throws ParseException {
        return this.formatField();
    }
}
