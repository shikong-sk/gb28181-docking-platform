package cn.skcks.docking.gb28181.sip.header;

import javax.sip.header.Header;

public interface XGBVerHeader extends Header {
    public void setVersion(int m, int n);

    public String getVersion();

    public final static String NAME = "X-GB-Ver";
}
