package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@RequiredArgsConstructor
public class RecordInfoSubscribe implements GenericSubscribe<RecordInfoResponseDTO> {
    private final Executor executor;
    private static final Map<String, SubmissionPublisher<RecordInfoResponseDTO>> publishers = new ConcurrentHashMap<>();

    public void close() {
        Helper.close(publishers);
    }

    public void addPublisher(String key) {
        Helper.addPublisher(executor, publishers, key);
    }

    public SubmissionPublisher<RecordInfoResponseDTO> getPublisher(String key) {
        return Helper.getPublisher(publishers, key);
    }

    public void addSubscribe(String key, Flow.Subscriber<RecordInfoResponseDTO> subscribe) {
        Helper.addSubscribe(publishers, key, subscribe);
    }

    public void compile(String key){
        delPublisher(key);
    }

    @Override
    public void delPublisher(String key) {
        Helper.delPublisher(publishers, key);
    }
}
