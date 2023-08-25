package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Component
@Slf4j
public class SipListenerImpl implements SipListener {
    private final SipSubscribe sipSubscribe;
    private final ConcurrentMap<String, MessageProcessor> requestProcessor = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, MessageProcessor> responseProcessor = new ConcurrentHashMap<>();

    public void addRequestProcessor(String method, MessageProcessor messageProcessor) {
        log.debug("[SipListener] 注册 {} 请求处理器", method);
        requestProcessor.put(method, messageProcessor);
    }

    public void addResponseProcessor(String method, MessageProcessor messageProcessor) {
        log.debug("[SipListener] 注册 {} 响应处理器", method);
        responseProcessor.put(method, messageProcessor);
    }


    @Override
    @Async(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    public void processRequest(RequestEvent requestEvent) {
        String method = requestEvent.getRequest().getMethod();
        log.debug("传入请求 method => {}", method);
        Optional.ofNullable(requestProcessor.get(method)).ifPresent(processor -> {
            processor.process(requestEvent);
        });
    }

    @Override
    @Async(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    public void processResponse(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        int status = response.getStatusCode();
        CSeqHeader cseqHeader = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        String method = cseqHeader.getMethod();
        log.debug("{} {}", method, response);

        // Success
        if (((status >= Response.OK) && (status < Response.MULTIPLE_CHOICES)) || status == Response.UNAUTHORIZED) {
            log.debug("传入响应 method => {}", method);
            Optional.ofNullable(responseProcessor.get(method)).ifPresent(processor -> {
                processor.process(responseEvent);
            });
        } else if ((status >= Response.TRYING) && (status < Response.OK)) {
            // 增加其它无需回复的响应，如101、180等
        } else {
            log.warn("接收到失败的response响应！status：" + status + ",message:" + response.getReasonPhrase());
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
