package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sip.*;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
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
        log.debug("传入请求 method => {}",method);
        Optional.ofNullable(processor.get(method)).ifPresent(processor -> {
            processor.process(requestEvent);
        });
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        int status = response.getStatusCode();

        // Success
        if (((status >= Response.OK) && (status < Response.MULTIPLE_CHOICES)) || status == Response.UNAUTHORIZED) {
            CSeqHeader cseqHeader = (CSeqHeader) responseEvent.getResponse().getHeader(CSeqHeader.NAME);
            String method = cseqHeader.getMethod();
            // ISIPResponseProcessor sipRequestProcessor = responseProcessorMap.get(method);
            // if (sipRequestProcessor != null) {
            //     sipRequestProcessor.process(responseEvent);
            // }

            // if (status != Response.UNAUTHORIZED && responseEvent.getResponse() != null && sipSubscribe.getOkSubscribesSize() > 0 ) {
            //     CallIdHeader callIdHeader = (CallIdHeader)responseEvent.getResponse().getHeader(CallIdHeader.NAME);
            //     if (callIdHeader != null) {
            //         SipSubscribe.Event subscribe = sipSubscribe.getOkSubscribe(callIdHeader.getCallId());
            //         if (subscribe != null) {
            //             SipSubscribe.EventResult eventResult = new SipSubscribe.EventResult(responseEvent);
            //             sipSubscribe.removeOkSubscribe(callIdHeader.getCallId());
            //             subscribe.response(eventResult);
            //         }
            //     }
            // }
        } else if ((status >= Response.TRYING) && (status < Response.OK)) {
            // 增加其它无需回复的响应，如101、180等
        } else {
            log.warn("接收到失败的response响应！status：" + status + ",message:" + response.getReasonPhrase());
            // if (responseEvent.getResponse() != null && sipSubscribe.getErrorSubscribesSize() > 0 ) {
            //     CallIdHeader callIdHeader = (CallIdHeader)responseEvent.getResponse().getHeader(CallIdHeader.NAME);
            //     if (callIdHeader != null) {
            //         SipSubscribe.Event subscribe = sipSubscribe.getErrorSubscribe(callIdHeader.getCallId());
            //         if (subscribe != null) {
            //             SipSubscribe.EventResult<?> eventResult = new SipSubscribe.EventResult(responseEvent);
            //             subscribe.response(eventResult);
            //             sipSubscribe.removeErrorSubscribe(callIdHeader.getCallId());
            //         }
            //     }
            // }
            if (responseEvent.getDialog() != null) {
                responseEvent.getDialog().delete();
            }
        }
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
