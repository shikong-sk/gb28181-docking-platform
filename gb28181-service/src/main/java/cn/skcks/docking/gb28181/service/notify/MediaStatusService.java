package cn.skcks.docking.gb28181.service.notify;

import cn.hutool.core.collection.CollectionUtil;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.sdp.GB28181SDPBuilder;
import cn.skcks.docking.gb28181.sip.manscdp.mediastatus.notify.MediaStatusRequestDTO;
import cn.skcks.docking.gb28181.sip.method.invite.request.InviteRequestBuilder;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sip.address.SipURI;
import javax.sip.message.Request;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class MediaStatusService {
    private final SipConfig sipConfig;
    private final SipMessageSender sender;
    private final SipSubscribe subscribe;
    public void process(SIPRequest request, MediaStatusRequestDTO dto){
        String senderIp = request.getLocalAddress().getHostAddress();
        String deviceId = ((SipURI)request.getFromHeader().getAddress().getURI()).getUser();
        if(StringUtils.equalsIgnoreCase(dto.getNotifyType(),"121")){
            InviteRequestBuilder inviteRequestBuilder = InviteRequestBuilder.builder()
                    .localIp(request.getLocalAddress().getHostAddress())
                    .localPort(sipConfig.getPort())
                    .localId(((SipURI)request.getToHeader().getAddress().getURI()).getUser())
                    .targetIp(request.getRemoteAddress().getHostAddress())
                    .targetPort(request.getRemotePort())
                    .targetId(((SipURI)request.getFromHeader().getAddress().getURI()).getUser())
                    .transport(request.getTopmostViaHeader().getTransport())
                    .build();

            String keyPattern = CacheUtil.getKey(GB28181SDPBuilder.Action.PLAY_BACK.getAction(), deviceId,"*");
            Set<String> keys = RedisUtil.KeyOps.keys(keyPattern);
            if (CollectionUtil.isEmpty(keys)){
                // 实在找不到就原样发回去 ╮(╯▽╰)╭
                sender.send(senderIp, inviteRequestBuilder.createByeRequest(request.getCallId().getCallId(), request.getCSeq().getSeqNumber() + 1));
            } else {
                keys.forEach(key -> {
                    String json = RedisUtil.StringOps.get(key);
                    if(StringUtils.isNotBlank(json)){
                        log.debug("{} {}",key,json);
                        SipTransactionInfo transactionInfo = JsonUtils.parse(json, SipTransactionInfo.class);
                        String callId = transactionInfo.getCallId();
                        String subscribeKey = GenericSubscribe.Helper.getKey(Request.BYE, callId);
                        log.debug("{} {}",callId,subscribeKey);
                        subscribe.getSipRequestSubscribe().getPublisher(subscribeKey).submit(request);
                    }
                });
            }
        }
    }
}
