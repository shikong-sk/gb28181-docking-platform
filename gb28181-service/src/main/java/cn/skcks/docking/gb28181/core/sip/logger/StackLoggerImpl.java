package cn.skcks.docking.gb28181.core.sip.logger;

import cn.skcks.docking.gb28181.config.sip.SipConfig;
import gov.nist.core.StackLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class StackLoggerImpl implements StackLogger {

    private final static Logger logger = LoggerFactory.getLogger(StackLoggerImpl.class);

    @Override
    public void logStackTrace() {}

    @Override
    public void logStackTrace(int traceLevel) {
        System.out.println("traceLevel: "  + traceLevel);
    }

    @Override
    public int getLineCount() {
        return 0;
    }

    @Override
    public void logException(Throwable ex) {

    }

    @Override
    public void logDebug(String message) {
    }

    @Override
    public void logDebug(String message, Exception ex) {
    }

    @Override
    public void logTrace(String message) {
        logger.trace(message);
    }

    @Override
    public void logFatalError(String message) {
    }

    @Override
    public void logError(String message) {
    }

    @Override
    public boolean isLoggingEnabled() {
        return true;
    }

    @Override
    public boolean isLoggingEnabled(int logLevel) {
        return true;
    }

    @Override
    public void logError(String message, Exception ex) {
       logger.error(message);
    }

    @Override
    public void logWarning(String message) {
        logger.warn(message);
    }

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void disableLogging() {

    }

    @Override
    public void enableLogging() {

    }

    @Override
    public void setBuildTimeStamp(String buildTimeStamp) {

    }

    @Override
    public void setStackProperties(Properties stackProperties) {

    }

    @Override
    public String getLoggerName() {
        return null;
    }
}
