package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public interface GenericSubscribe<T> {
    void close();

    void addPublisher(String key);

    SubmissionPublisher<T> getPublisher(String key);

    void addSubscribe(String key,Flow.Subscriber<T> subscribe);

    class Helper {
        public static <T> void close(Map<String,SubmissionPublisher<T>> publishers){
            publishers.values().forEach(SubmissionPublisher::close);
            publishers.clear();
        }

        public static <T> void addPublisher(Executor executor, Map<String, SubmissionPublisher<T>> publishers, String key){
            SubmissionPublisher<T> publisher = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
            publishers.put(key, publisher);
        }

        public static <T> void addPublisher(Executor executor, Map<String, SubmissionPublisher<T>> publishers, String key, int bufferSize){
            SubmissionPublisher<T> publisher = new SubmissionPublisher<>(executor, bufferSize);
            publishers.put(key, publisher);
        }

        public static <T> SubmissionPublisher<T> getPublisher(Map<String, SubmissionPublisher<T>> publishers, String key){
            return publishers.get(key);
        }

        public static <T> void addSubscribe(Map<String, SubmissionPublisher<T>> publishers, String key,Flow.Subscriber<T> subscribe){
            SubmissionPublisher<T> publisher = getPublisher(publishers, key);
            if(publisher != null){
                publisher.subscribe(subscribe);
            }
        }
    }
}
