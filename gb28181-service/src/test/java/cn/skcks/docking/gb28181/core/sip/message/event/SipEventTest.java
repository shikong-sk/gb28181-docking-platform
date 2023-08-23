package cn.skcks.docking.gb28181.core.sip.message.event;

import cn.hutool.core.util.RandomUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.MediaSdpHelper;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.SsrcField;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.StreamMode;
import gov.nist.core.NameValue;
import gov.nist.core.Separators;
import gov.nist.javax.sdp.MediaDescriptionImpl;
import gov.nist.javax.sdp.SessionDescriptionImpl;
import gov.nist.javax.sdp.fields.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.sdp.*;
import java.net.URL;
import java.util.*;
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

        new Thread(() -> {
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

    @Test
    @SneakyThrows
    public void sdpTest() {
        GB28181Description description = GB28181Description.Convertor.convert((SessionDescriptionImpl) SdpFactory.getInstance().createSessionDescription("Play"));

        Version version = SdpFactory.getInstance().createVersion(0);
        description.setVersion(version);

        Connection connectionField = SdpFactory.getInstance().createConnection(ConnectionField.IN, Connection.IP4, "10.10.10.20");
        description.setConnection(connectionField);

        MediaDescription mediaDescription = SdpFactory.getInstance().createMediaDescription("video", 6666, 0, SdpConstants.RTP_AVP, MediaSdpHelper.RTPMAP.keySet().toArray(new String[0]));
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("recvonly",null));
        MediaSdpHelper.RTPMAP.forEach((k, v)->{
            Optional.ofNullable(MediaSdpHelper.FMTP.get(k)).ifPresent((f)->{
                mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.FMTP.toLowerCase(), StringUtils.joinWith(Separators.SP,k,f)));
            });
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.RTPMAP, StringUtils.joinWith(Separators.SP,k,v)));
        });

        // TCP-PASSIVE
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("setup","passive"));
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("connection","new"));

        // TCP-ACTIVE
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("setup","active"));
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("connection","new"));

        description.setMediaDescriptions(new Vector<>() {{
            add(mediaDescription);
        }});

        TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription();
        description.setTimeDescriptions(new Vector<>(){{add(timeDescription);}});

        // channelId
        Origin origin = SdpFactory.getInstance().createOrigin("44050100001310000006", 0, 0, ConnectionField.IN, Connection.IP4, "10.10.10.20");
        description.setOrigin(origin);
        // mediaDescription.setPreconditionFields();

        URIField uriField = new URIField();
        uriField.setURI("44050100001310000006:0");
        description.setURI(uriField);


        // GB28181Description description = (GB28181Description) description;
        description.setSsrcField(new SsrcField(12345678));
        SessionDescription sessionDescription = description;
        sessionDescription.setSessionName(SdpFactory.getInstance().createSessionName("PlayBack"));
        log.info("\n{}", sessionDescription);
    }

    @Test
    @SneakyThrows
    void mediaSdpHelperTest(){
        String deviceId = "44050100001110000006";
        String channel = "44050100001310000006";
        int rtpPort = 5080;
        String rtpIp = "10.10.10.20";
        long ssrc = RandomUtil.randomLong(10000000,100000000);
        GB28181Description description = MediaSdpHelper.build(MediaSdpHelper.Action.PLAY, deviceId, channel, Connection.IP4, rtpIp, rtpPort, ssrc, StreamMode.UDP, SdpFactory.getInstance().createTimeDescription());
        log.info("\n{}", description);
    }
}
