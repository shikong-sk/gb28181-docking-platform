package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import cn.skcks.docking.gb28181.core.sip.executor.DefaultSipExecutor;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class SipSubscribe {
    @Qualifier(DefaultSipExecutor.EXECUTOR_BEAN_NAME)
    private final Executor executor;
    private GenericSubscribe<RecordInfoResponseDTO> recordInfoSubscribe;
    private GenericSubscribe<SIPResponse> inviteSubscribe;

    @PostConstruct
    private void init() {
        recordInfoSubscribe = new RecordInfoSubscribe(executor);
        inviteSubscribe = new InviteSubscribe(executor);
    }

    @PreDestroy
    private void destroy() {
        inviteSubscribe.close();
        recordInfoSubscribe.close();
    }
}
