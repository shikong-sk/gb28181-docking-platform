package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class SipSubscribe {
    @Qualifier(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    private final Executor executor;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private GenericTimeoutSubscribe<SIPResponse> sipResponseSubscribe;
    private GenericTimeoutSubscribe<SIPRequest> sipRequestSubscribe;

    @PostConstruct
    private void init() {
        // 通用订阅器
        sipResponseSubscribe = new SipResponseSubscribe(executor, scheduledExecutorService);
        sipRequestSubscribe = new SipRequestSubscribe(executor, scheduledExecutorService);
    }

    @PreDestroy
    private void destroy() {
        sipResponseSubscribe.close();
        sipRequestSubscribe.close();
    }
}
