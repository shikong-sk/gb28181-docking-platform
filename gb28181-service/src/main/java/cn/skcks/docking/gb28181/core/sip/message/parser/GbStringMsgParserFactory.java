package cn.skcks.docking.gb28181.core.sip.message.parser;

import gov.nist.javax.sip.parser.MessageParser;
import gov.nist.javax.sip.parser.MessageParserFactory;
import gov.nist.javax.sip.stack.SIPTransactionStack;

public class GbStringMsgParserFactory implements MessageParserFactory {

    /**
     * msg parser is completely stateless, reuse instance for the whole stack
     * fixes https://github.com/RestComm/jain-sip/issues/92
     */
    private static final GBStringMsgParser msgParser = new GBStringMsgParser();
    /*
     * (non-Javadoc)
     * @see gov.nist.javax.sip.parser.MessageParserFactory#createMessageParser(gov.nist.javax.sip.stack.SIPTransactionStack)
     */
    public MessageParser createMessageParser(SIPTransactionStack stack) {
        return msgParser;
    }
}
