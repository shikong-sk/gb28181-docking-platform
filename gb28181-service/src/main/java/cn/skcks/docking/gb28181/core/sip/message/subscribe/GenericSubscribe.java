package cn.skcks.docking.gb28181.core.sip.message.subscribe;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public interface GenericSubscribe<T> {
    void close();

    void addPublisher(String key);

    SubmissionPublisher<T> getPublisher(String key);

    void addSubscribe(String key,Flow.Subscriber<T> subscribe);
    void delPublisher(String key);

    class Helper {
        public final static String SEPARATOR = ":";
        public static String getKey(String prefix,String... ids){
            return StringUtils.joinWith(SEPARATOR, (Object[]) ArrayUtils.addFirst(ids,prefix));
        }

        public static <T> void close(Map<String,SubmissionPublisher<T>> publishers){
            publishers.values().forEach(SubmissionPublisher::close);
            publishers.clear();
        }

        public static <T> void delPublisher(Map<String, SubmissionPublisher<T>> publishers, String key){
            SubmissionPublisher<T> publisher = publishers.remove(key);
            Optional.ofNullable(publisher).ifPresent(SubmissionPublisher::close);
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
