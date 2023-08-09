package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sip.*;

@Component
@Slf4j
public class SipObserverImpl implements SipObserver{
    @Override
    @Async(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    public void processRequest(RequestEvent requestEvent) {
        log.debug("method => {}",requestEvent.getRequest().getMethod());
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {

    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {

    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

    }
}
