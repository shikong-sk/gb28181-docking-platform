package cn.skcks.docking.gb28181.core.sip.message.event;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class SipSubscribe {
    @Qualifier(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    private final Executor executor;

    private SubmissionPublisher<SipEventItem> publisher;

    @PostConstruct
    private void init(){
        publisher = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
    }

    @PreDestroy
    private void destroy(){
        publisher.close();
    }
}
