package cn.skcks.docking.gb28181.service.ssrc;

import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SsrcService {
    private static final String PREFIX = "SSRC";
    private static final int MAX_STREAM_COUNT = 10000;
    private final SipConfig sipConfig;
    private final ZlmMediaConfig mediaConfig;

    private String ssrcKey;

    @PostConstruct
    private void init(){
        String ssrcPrefix = sipConfig.getDomain().substring(3, 8);
        ssrcKey = CacheUtil.getKey(PREFIX,sipConfig.getId(),mediaConfig.getId());
        List<String> ssrcList = new ArrayList<>(MAX_STREAM_COUNT);
        for (int i = 1; i < MAX_STREAM_COUNT; i++) {
            String ssrc = String.format("%s%04d", ssrcPrefix, i);
            ssrcList.add(ssrc);
        }

        RedisUtil.KeyOps.delete(ssrcKey);
        RedisUtil.SetOps.sAdd(ssrcKey, ssrcList.toArray(String[]::new));
    }

    @PreDestroy
    private void destroy(){
        if(ssrcKey != null){
            RedisUtil.KeyOps.delete(ssrcKey);
        }
    }

    private String getSN() {
        String sn;
        long size = RedisUtil.SetOps.sSize(ssrcKey);
        if (size == 0) {
            throw new RuntimeException("ssrc已经用完");
        } else {
            // 在集合中移除并返回一个随机成员。
            sn = RedisUtil.SetOps.sPop(ssrcKey);
            RedisUtil.SetOps.sRemove(ssrcKey,sn);
        }
        return sn;
    }

    /**
     * 重置一个流媒体服务的所有ssrc
     *
     * @param mediaServerId 流媒体服务ID
     */
    public void reset(String mediaServerId) {
        init();
    }

    /**
     * 获取视频预览的SSRC值,第一位固定为0
     *
     * @return ssrc
     */
    public String getPlaySsrc() {
        return "0" + getSN();
    }

    /**
     * 获取录像回放的SSRC值,第一位固定为1
     */
    public String getPlayBackSsrc(String mediaServerI) {
        return "1" + getSN();
    }

    /**
     * 释放ssrc，主要用完的ssrc一定要释放，否则会耗尽
     *
     * @param ssrc 需要重置的ssrc
     */
    public void releaseSsrc(String mediaServerId, String ssrc) {
        if (ssrc == null) {
            return;
        }
        String sn = ssrc.substring(1);
        RedisUtil.SetOps.sAdd(ssrcKey, sn);
    }
}
