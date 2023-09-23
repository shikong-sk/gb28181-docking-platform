package cn.skcks.docking.gb28181.sip.generic;

import javax.sip.header.ContentTypeHeader;

public class SipContentType {
    public static final ContentTypeHeader XML = SipBuilder.createContentTypeHeader("Application", "MANSCDP+xml");
    public static final ContentTypeHeader SDP = SipBuilder.createContentTypeHeader("Application", "SDP");
    public static final ContentTypeHeader RTSP = SipBuilder.createContentTypeHeader("Application", "MANSRTSP");
}
