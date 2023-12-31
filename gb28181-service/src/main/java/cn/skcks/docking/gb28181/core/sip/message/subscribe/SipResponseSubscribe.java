package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import gov.nist.javax.sip.message.SIPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
public class SipResponseSubscribe implements GenericTimeoutSubscribe<SIPResponse>, Closeable {
    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledFutureManager = new ConcurrentHashMap<>(0);
    private static final Map<String, SubmissionPublisher<SIPResponse>> publishers = new ConcurrentHashMap<>();

    public void close() {
        Helper.close(publishers);
    }

    public void addPublisher(String key) {
        Helper.addPublisher(executor, publishers, key);
    }

    public SubmissionPublisher<SIPResponse> getPublisher(String key) {
        return Helper.getPublisher(publishers, key);
    }

    public void addSubscribe(String key, Flow.Subscriber<SIPResponse> subscribe) {
        Helper.addSubscribe(publishers, key, subscribe);
    }

    public void complete(String key){
        delPublisher(key);
    }

    @Override
    public void delPublisher(String key) {
        ScheduledFuture<?> schedule = scheduledFutureManager.remove(key);
        Optional.ofNullable(schedule).ifPresent(scheduledFuture->scheduledFuture.cancel(true));
        Helper.delPublisher(publishers, key);
    }

    @Override
    public void addPublisher(String key, long time, TimeUnit timeUnit) {
        addPublisher(key);
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> {
            scheduledFutureManager.remove(key);
            delPublisher(key);
            log.debug("清理超时 响应 订阅器 {}", key);
        }, time, timeUnit);
        scheduledFutureManager.put(key,schedule);
    }

    @Override
    public void refreshPublisher(String key, long time, TimeUnit timeUnit) {
        ScheduledFuture<?> schedule = scheduledFutureManager.remove(key);
        Optional.ofNullable(schedule).ifPresent(scheduledFuture->scheduledFuture.cancel(true));
        schedule = scheduledExecutorService.schedule(() -> {
            scheduledFutureManager.remove(key);
            delPublisher(key);
            log.debug("清理超时 响应 订阅器 {}", key);
        }, time, timeUnit);
        scheduledFutureManager.put(key,schedule);
    }
}
