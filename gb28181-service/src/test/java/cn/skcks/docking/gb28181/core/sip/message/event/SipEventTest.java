package cn.skcks.docking.gb28181.core.sip.message.event;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SipEventTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        int taskNum = 1000;

        ExecutorService executor = new ThreadPoolExecutor(threadNum, threadNum,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(taskNum), // 使用有界队列，避免OOM
                new ThreadPoolExecutor.DiscardPolicy());

        SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
        List<String> list = new ArrayList<>();
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("建立订阅");
                subscription.request(5);
            }

            @Override
            public void onNext(String item) {
                list.add(item);
                subscription.request(5);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscription.cancel();
                countDownLatch.countDown();
                log.info("订阅结束");
            }
        };
        submissionPublisher.subscribe(subscriber);

        AtomicInteger finalI = new AtomicInteger(1);

        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            subscriber.onComplete();
        }).start();

        for (int i = 0; i < 128; i++) {
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            Thread.sleep(10);
        }

        countDownLatch.await();
        submissionPublisher.close();

        list.parallelStream().forEach(item -> log.info("接收发送者消息 {}", item));

        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        submissionPublisher.subscribe(new Flow.Subscriber<>() {
            Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("建立订阅");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String item) {
                log.info("{}", item);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscription.cancel();
                countDownLatch2.countDown();
                log.info("订阅结束");
            }
        });
        countDownLatch2.await();
        executor.shutdown();
    }
}
