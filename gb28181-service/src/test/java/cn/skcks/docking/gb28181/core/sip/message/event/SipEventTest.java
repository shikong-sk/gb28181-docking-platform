package cn.skcks.docking.gb28181.core.sip.message.event;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SipEventTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(512);

        int threadNum = Runtime.getRuntime().availableProcessors() * 2;

        int taskNum = 1000;

        ExecutorService executor = new ThreadPoolExecutor(threadNum, threadNum,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(taskNum), // 使用有界队列，避免OOM
                new ThreadPoolExecutor.DiscardPolicy());

        SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
        submissionPublisher.subscribe(new Flow.Subscriber<>() {
            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                log.info("建立订阅");
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                log.info("接收发送者消息 {}", item);
                countDownLatch.countDown();
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                log.info("订阅结束");
            }
        });


        AtomicInteger finalI = new AtomicInteger(1);
        for (int i = 0; i < 128; i++) {
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
            new Thread(() -> submissionPublisher.submit(String.valueOf(finalI.getAndIncrement()))).start();
        }

        countDownLatch.await();
        submissionPublisher.close();
        executor.shutdown();
    }
}
