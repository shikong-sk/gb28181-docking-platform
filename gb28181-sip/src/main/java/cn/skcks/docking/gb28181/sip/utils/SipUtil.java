package cn.skcks.docking.gb28181.sip.utils;

import cn.hutool.core.lang.id.NanoId;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Subject;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sip.SipFactory;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.UserAgentHeader;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused"})
@Slf4j
public class SipUtil {
    @Setter
    public static String UserAgent = "GB28181-Docking-Platform";
    @Setter
    public static String UserAgentVersion = "";

    private static final char[] DEFAULT_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static String nanoId(){
        return nanoId(32);
    }

    public static String nanoId(int size){
        return NanoId.randomNanoId(null,DEFAULT_ALPHABET,size);
    }

    public static String getIdFromFromHeader(FromHeader fromHeader) {
        AddressImpl address = (AddressImpl)fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return uri.getUser();
    }

    public static String getIdFromFromHeader(Request request) {
        FromHeader fromHeader = (FromHeader)request.getHeader(FromHeader.NAME);
        return getIdFromFromHeader(fromHeader);
    }

    /**
     * 从subject读取channelId
     * */
    public static String getChannelIdFromRequest(Request request) {
        Header subject = request.getHeader("subject");
        if (subject == null) {
            // 如果缺失subject
            return null;
        }
        return ((Subject) subject).getSubject().split(":")[0];
    }

    public static String generateViaTag() {
        return "z9hG4bK" + nanoId(10);
    }

    @SneakyThrows
    public static UserAgentHeader getUserAgentHeader(){
        List<String> agentParam = new ArrayList<>(3);
        agentParam.add(UserAgent);
        if(StringUtils.isNotBlank(UserAgentVersion)){
            agentParam.add(" ");
            agentParam.add(StringUtils.replace(UserAgentVersion,"-SNAPSHOT",""));
        }
        return SipFactory.getInstance().createHeaderFactory().createUserAgentHeader(agentParam);
    }

    public static String generateFromTag(){
        return nanoId();
    }

    public static String generateTag(){
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 从请求中获取设备ip地址和端口号
     * @param request 请求
     * @param sipUseSourceIpAsRemoteAddress  false 从via中获取地址， true 直接获取远程地址
     * @return 地址信息
     */
    public static RemoteInfo getRemoteInfoFromRequest(SIPRequest request, boolean sipUseSourceIpAsRemoteAddress) {
        String remoteAddress;
        int remotePort;
        if (sipUseSourceIpAsRemoteAddress) {
            remoteAddress = request.getPeerPacketSourceAddress().getHostAddress();
            remotePort = request.getPeerPacketSourcePort();

        } else {
            // 判断RPort是否改变，改变则说明路由nat信息变化，修改设备信息
            // 获取到通信地址等信息
            remoteAddress = request.getTopmostViaHeader().getReceived();
            remotePort = request.getTopmostViaHeader().getRPort();
            // 解析本地地址替代
            if (ObjectUtils.isEmpty(remoteAddress) || remotePort == -1) {
                remoteAddress = request.getPeerPacketSourceAddress().getHostAddress();
                remotePort = request.getPeerPacketSourcePort();
            }
        }

        return new RemoteInfo(remoteAddress, remotePort);
    }

    /**
     * 云台指令码计算
     *
     * @param leftRight  镜头左移右移 0:停止 1:左移 2:右移
     * @param upDown     镜头上移下移 0:停止 1:上移 2:下移
     * @param inOut      镜头放大缩小 0:停止 1:缩小 2:放大
     * @param moveSpeed  镜头移动速度 默认 0XFF (0-255)
     * @param zoomSpeed  镜头缩放速度 默认 0X1 (0-255)
     */
    public static String cmdString(int leftRight, int upDown, int inOut, int moveSpeed, int zoomSpeed) {
        int cmdCode = 0;
        if (leftRight == 2) {
            cmdCode|=0x01;		// 右移
        } else if(leftRight == 1) {
            cmdCode|=0x02;		// 左移
        }
        if (upDown == 2) {
            cmdCode|=0x04;		// 下移
        } else if(upDown == 1) {
            cmdCode|=0x08;		// 上移
        }
        if (inOut == 2) {
            cmdCode |= 0x10;	// 放大
        } else if(inOut == 1) {
            cmdCode |= 0x20;	// 缩小
        }
        StringBuilder builder = new StringBuilder("A50F01");
        String strTmp;
        strTmp = String.format("%02X", cmdCode);
        builder.append(strTmp, 0, 2);
        strTmp = String.format("%02X", moveSpeed);
        builder.append(strTmp, 0, 2);
        builder.append(strTmp, 0, 2);

        //优化zoom低倍速下的变倍速率
        if ((zoomSpeed > 0) && (zoomSpeed <16))
        {
            zoomSpeed = 16;
        }
        strTmp = String.format("%X", zoomSpeed);
        builder.append(strTmp, 0, 1).append("0");
        //计算校验码
        int checkCode = (0XA5 + 0X0F + 0X01 + cmdCode + moveSpeed + moveSpeed + (zoomSpeed /*<< 4*/ & 0XF0)) % 0X100;
        strTmp = String.format("%02X", checkCode);
        builder.append(strTmp, 0, 2);
        return builder.toString();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoteInfo{
        private String ip;
        private int port;
    }
}
