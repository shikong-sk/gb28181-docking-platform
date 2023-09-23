package cn.skcks.docking.gb28181.sip.header.impl;

import cn.skcks.docking.gb28181.sip.header.XGBVerHeader;
import gov.nist.javax.sip.header.SIPHeader;

public class XGBVerHeaderImpl extends SIPHeader implements XGBVerHeader {
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
