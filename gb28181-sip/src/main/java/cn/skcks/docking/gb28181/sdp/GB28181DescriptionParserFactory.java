package cn.skcks.docking.gb28181.sdp;

import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.ParserFactory;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;

public class GB28181DescriptionParserFactory {
    public static SDPParser createParser(String field) throws ParseException {
        String fieldName = Lexer.getFieldName(field);
        if(fieldName.equalsIgnoreCase("y")){
            return new SsrcFieldParser(field);
        }
        return ParserFactory.createParser(field);
    }
}
