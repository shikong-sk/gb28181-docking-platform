package cn.skcks.docking.gb28181.sdp.field.ssrc.parser;

import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import gov.nist.javax.sdp.fields.SDPField;
import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;

public class SsrcFieldParser extends SDPParser {
    public SsrcFieldParser(String ssrcField) {
        this.lexer = new Lexer("charLexer", ssrcField);
    }

    public SsrcField ssrcField() throws ParseException {
        try {
            this.lexer.match('y');
            this.lexer.SPorHT();
            this.lexer.match('=');
            this.lexer.SPorHT();

            SsrcField ssrcField = new SsrcField();
            String rest = lexer.getRest().trim();
            ssrcField.setSsrc(rest);
            return ssrcField;
        } catch (Exception e) {
            throw lexer.createParseException();
        }
    }

    public SDPField parse() throws ParseException {
        return this.ssrcField();
    }
}
