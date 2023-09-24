package cn.skcks.docking.gb28181.sip.header.impl;

import cn.skcks.docking.gb28181.sip.header.XGBVerHeader;
import gov.nist.javax.sip.header.SIPHeader;

public class XGBVerHeaderImpl extends SIPHeader implements XGBVerHeader {
    /**
     * GB/T 28181-2011
     */
    public static XGBVerHeaderImpl GB28181_2011 = new XGBVerHeaderImpl(1, 0);

    /**
     * GB/T 28181-2011 补充文件
     */
    public static XGBVerHeaderImpl GB28181_2011_V2 = new XGBVerHeaderImpl(1, 1);

    /**
     * GB/T 28181-2016
     */
    public static XGBVerHeaderImpl GB28181_2016 = new XGBVerHeaderImpl(2, 0);

    /**
     * GB/T 28181-2022
     */
    public static XGBVerHeaderImpl GB28181_2022 = new XGBVerHeaderImpl(3, 0);

    private String version;

    @Override
    public void setVersion(int m, int n) {
        this.version = String.format("%d.%d", m, n);
    }

    @Override
    public String getVersion() {
        return version;
    }

    public XGBVerHeaderImpl(int m, int n) {
        super(NAME);
        setVersion(m, n);
    }

    public XGBVerHeaderImpl() {
        super(NAME);
    }

    protected StringBuilder encodeBody(StringBuilder buffer) {
        return buffer.append(version);
    }
}
