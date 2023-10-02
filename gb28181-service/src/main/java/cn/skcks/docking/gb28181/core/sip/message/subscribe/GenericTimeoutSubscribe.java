package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public interface GenericTimeoutSubscribe<T> extends GenericSubscribe<T>{
    void addPublisher(String key, long time, TimeUnit timeUnit);

    void refreshPublisher(String key, long time, TimeUnit timeUnit);
}
