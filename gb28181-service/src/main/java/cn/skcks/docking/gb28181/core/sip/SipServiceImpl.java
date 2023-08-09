package cn.skcks.docking.gb28181.core.sip;

import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.properties.DefaultProperties;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sip.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

@Slf4j
@RequiredArgsConstructor
@Data
@Service
public class SipServiceImpl implements SipService{
    private final SipFactory sipFactory = SipFactory.getInstance();
    private final SipConfig sipConfig;
    private final List<SipProviderImpl> pool = new ArrayList<>(2);
    private SipStackImpl sipStack;

    @Override
    public void run() {
        sipFactory.setPathName("gov.nist");
        sipConfig.getIp().parallelStream().forEach(ip->{
            listen(ip, sipConfig.getPort());
        });
    }

    @Override
    public void stop() {
        if(sipStack == null){
            return;
        }

        sipStack.closeAllSockets();
        pool.parallelStream().forEach(sipProvider -> {
            ListeningPoint listen = sipProvider.getListeningPoint();
            log.debug("移除监听 {}://{}:{}",listen.getTransport(),listen.getIPAddress(),listen.getPort());
            sipProvider.removeListeningPoints();

            try{
                sipStack.deleteListeningPoint(listen);
                sipStack.deleteSipProvider(sipProvider);
            } catch (Exception ignore){
            }
        });
        pool.clear();
    }

    public void listen(String ip, int port){
        try{
            sipStack = (SipStackImpl)sipFactory.createSipStack(DefaultProperties.getProperties("GB28181_SIP_LOG",true));

            try {
                ListeningPoint tcpListen = sipStack.createListeningPoint(ip, port, "TCP");
                SipProviderImpl tcpSipProvider = (SipProviderImpl) sipStack.createSipProvider(tcpListen);
                tcpSipProvider.setDialogErrorsAutomaticallyHandled();
                pool.add(tcpSipProvider);
                log.info("[sip] 监听 tcp://{}:{}", ip, port);
            } catch (TransportNotSupportedException
                     | ObjectInUseException
                     | InvalidArgumentException e) {
                log.error("[sip] tcp://{}:{} 监听失败, 请检查端口是否被占用, 错误信息 => {}", ip, port, e.getMessage());
            }

            try {
                ListeningPoint udpListen = sipStack.createListeningPoint(ip, port, "UDP");
                SipProviderImpl udpSipProvider = (SipProviderImpl) sipStack.createSipProvider(udpListen);
                pool.add(udpSipProvider);
                log.info("[sip] 监听 udp://{}:{}", ip, port);
            } catch (TransportNotSupportedException
                     | ObjectInUseException
                     | InvalidArgumentException e) {
                log.error("[sip] udp://{}:{} 监听失败, 请检查端口是否被占用, 错误信息 => {}", ip, port, e.getMessage());
            }
        } catch (Exception e){
            log.error("[sip] {}:{} 监听失败, 请检查端口是否被占用, 错误信息 => {}",ip,port, e.getMessage());
        }

    }
}
