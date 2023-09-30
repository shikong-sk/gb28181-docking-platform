package cn.skcks.docking.gb28181.sdp.parser;

import cn.skcks.docking.gb28181.sdp.field.ssrc.FormatField;
import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import cn.skcks.docking.gb28181.sdp.field.ssrc.parser.FormatFieldParser;
import cn.skcks.docking.gb28181.sdp.field.ssrc.parser.SsrcFieldParser;
import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.ParserFactory;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;

public class GB28181DescriptionParserFactory {
    public static SDPParser createParser(String field) throws ParseException {
        String fieldName = Lexer.getFieldName(field);
        if(fieldName.equalsIgnoreCase(SsrcField.SSRC_FIELD_NAME)){
            return new SsrcFieldParser(field);
        }
        if(fieldName.equalsIgnoreCase(FormatField.FORMAT_FIELD_NAME)){
            return new FormatFieldParser(field);
        }
        return ParserFactory.createParser(field);
    }
}
