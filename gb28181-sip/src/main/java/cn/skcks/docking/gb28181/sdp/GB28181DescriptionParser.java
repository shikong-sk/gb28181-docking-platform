package cn.skcks.docking.gb28181.sdp;

import gov.nist.core.ParserCore;
import gov.nist.javax.sdp.SessionDescriptionImpl;
import gov.nist.javax.sdp.fields.SDPField;
import gov.nist.javax.sdp.parser.Lexer;
import gov.nist.javax.sdp.parser.SDPParser;

import java.text.ParseException;
import java.util.Vector;

@SuppressWarnings("all")
public class GB28181DescriptionParser extends ParserCore {
    protected Lexer lexer;
    protected Vector sdpMessage;

    public GB28181DescriptionParser(Vector sdpMessage) {
        this.sdpMessage = sdpMessage;
    }

    public GB28181DescriptionParser(String message) {
        int start = 0;
        String line = null;
        // Return trivially if there is no sdp announce message
        // to be parsed. Bruno Konik noticed this bug.
        if (message == null) return;
        sdpMessage = new Vector();
        // Strip off leading and trailing junk.
        String sdpAnnounce = message.trim() + "\r\n";
        // Bug fix by Andreas Bystrom.
        while (start < sdpAnnounce.length()) {
            // Major re-write by Ricardo Borba.
            int lfPos = sdpAnnounce.indexOf("\n", start);
            int crPos = sdpAnnounce.indexOf("\r", start);

            if (lfPos >= 0 && crPos < 0) {
                // there are only "\n" separators
                line = sdpAnnounce.substring(start, lfPos);
                start = lfPos + 1;
            } else if (lfPos < 0 && crPos >= 0) {
                //bug fix: there are only "\r" separators
                line = sdpAnnounce.substring(start, crPos);
                start = crPos + 1;
            } else if (lfPos >= 0 && crPos >= 0) {
                // there are "\r\n" or "\n\r" (if exists) separators
                if (lfPos > crPos) {
                    // assume "\r\n" for now
                    line = sdpAnnounce.substring(start, crPos);
                    // Check if the "\r" and "\n" are close together
                    if (lfPos == crPos + 1) {
                        start = lfPos + 1; // "\r\n"
                    } else {
                        start = crPos + 1; // "\r" followed by the next record and a "\n" further away
                    }
                } else {
                    // assume "\n\r" for now
                    line = sdpAnnounce.substring(start, lfPos);
                    // Check if the "\n" and "\r" are close together
                    if (crPos == lfPos + 1) {
                        start = crPos + 1; // "\n\r"
                    } else {
                        start = lfPos + 1; // "\n" followed by the next record and a "\r" further away
                    }
                }
            } else if (lfPos < 0 && crPos < 0) { // end
                break;
            }
            sdpMessage.addElement(line);
        }
    }

    public GB28181Description parse() throws ParseException {
        GB28181Description retval = new GB28181Description();
        for (int i = 0; i < sdpMessage.size(); i++) {
            String field = (String) sdpMessage.elementAt(i);
            SDPParser sdpParser = GB28181DescriptionParserFactory.createParser(field);
            SDPField sdpField = null;
            if (sdpParser != null) {
                sdpField = sdpParser.parse();
            }
            retval.addField(sdpField);
            if (sdpField instanceof SsrcField ssrc) {
                retval.setSsrcField(ssrc);
            }
        }
        return retval;
    }
}
