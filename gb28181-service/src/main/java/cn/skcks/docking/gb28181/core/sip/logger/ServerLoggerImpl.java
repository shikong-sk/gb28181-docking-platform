package cn.skcks.docking.gb28181.core.sip.logger;

import gov.nist.core.CommonLogger;
import gov.nist.core.ServerLogger;
import gov.nist.core.StackLogger;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.stack.SIPTransactionStack;

import javax.sip.SipStack;
import java.util.Properties;

public class ServerLoggerImpl implements ServerLogger {

    private boolean showLog = true;

    protected StackLogger stackLogger;

    @Override
    public void closeLogFile() {

    }

    @Override
    public void logMessage(SIPMessage message, String from, String to, boolean sender, long time) {
        if (!showLog) {
            return;
        }
        String log = (sender ? "发送: 目标 =>" + to : "接收: 来自 => " + from) + "\r\n" + message;
        this.stackLogger.logInfo(log);
    }

    @Override
    public void logMessage(SIPMessage message, String from, String to, String status, boolean sender, long time) {
        if (!showLog) {
            return;
        }
        String log = (sender ? "发送: 目标 =>" + to : "接收: 来自 => " + from) + "\r\n" + message;
        this.stackLogger.logInfo(log);
    }

    @Override
    public void logMessage(SIPMessage message, String from, String to, String status, boolean sender) {
        if (!showLog) {
            return;
        }
        String log = (sender ? "发送: 目标 =>" + to : "接收: 来自 => " + from) + "\r\n" + message;
        this.stackLogger.logInfo(log);
    }

    @Override
    public void logException(Exception ex) {
        if (!showLog) {
            return;
        }
        this.stackLogger.logException(ex);
    }

    @Override
    public void setStackProperties(Properties stackProperties) {
        if (!showLog) {
            return;
        }
        String TRACE_LEVEL = stackProperties.getProperty("gov.nist.javax.sip.TRACE_LEVEL");
        if (TRACE_LEVEL != null) {
            showLog = true;
        }
    }

    @Override
    public void setSipStack(SipStack sipStack) {
        if (!showLog) {
            return;
        }
        if(sipStack instanceof SIPTransactionStack st) {
            this.stackLogger = CommonLogger.getLogger(st.getClass());
        }
    }
}
