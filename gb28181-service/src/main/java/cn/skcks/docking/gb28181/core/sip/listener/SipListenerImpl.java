package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sip.*;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class SipListenerImpl implements SipListener {
    private final ConcurrentMap<String, MessageProcessor> processor = new ConcurrentHashMap<>();
    public void addProcessor(String method,MessageProcessor messageProcessor){
        log.debug("[SipListener] 注册 {} 处理器", method);
        processor.put(method, messageProcessor);
    }

    @Override
    @Async(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    public void processRequest(RequestEvent requestEvent) {
        String method = requestEvent.getRequest().getMethod();
        log.debug("method => {}",method);
        Optional.ofNullable(processor.get(method)).ifPresent(processor -> {
            processor.process(requestEvent);
        });
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {

    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        ClientTransaction clientTransaction = timeoutEvent.getClientTransaction();
        if (clientTransaction != null) {
            Request request = clientTransaction.getRequest();
            if (request != null) {
                CallIdHeader callIdHeader = (CallIdHeader) request.getHeader(CallIdHeader.NAME);
                if (callIdHeader != null) {
                    log.debug("会话超时 callId => {}", callIdHeader.getCallId());
                }
            }
        }
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        CallIdHeader callIdHeader = dialogTerminatedEvent.getDialog().getCallId();
        log.debug("会话终止 callId => {}", callIdHeader.getCallId());
    }
}
