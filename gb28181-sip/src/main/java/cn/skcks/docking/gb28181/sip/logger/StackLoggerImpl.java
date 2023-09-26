package cn.skcks.docking.gb28181.sip.logger;

import gov.nist.core.StackLogger;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@SuppressWarnings("unused")
public class StackLoggerImpl implements StackLogger {
    @Override
    public void logStackTrace() {}

    @Override
    public void logStackTrace(int traceLevel) {
        log.info("traceLevel: "  + traceLevel);
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
        log.trace(message);
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
       log.error(message);
    }

    @Override
    public void logWarning(String message) {
        log.warn(message);
    }

    @Override
    public void logInfo(String message) {
        log.info(message);
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
