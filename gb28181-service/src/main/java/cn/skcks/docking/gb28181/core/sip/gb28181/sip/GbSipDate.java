package cn.skcks.docking.gb28181.core.sip.gb28181.sip;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.format.FastDateFormat;
import gov.nist.javax.sip.header.SIPDate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * GB28181 SIP 日期
 */
public class GbSipDate extends SIPDate {
    private final Calendar javaCal;

    public GbSipDate(long timeMillis) {
        super(timeMillis);
        this.javaCal = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    }

    @Override
    public StringBuilder encode(StringBuilder encoding) {
        return encoding.append(FastDateFormat.getInstance(DatePattern.UTC_SIMPLE_MS_PATTERN, TimeZone.getTimeZone("Asia/Shanghai")).format(javaCal));
    }
}
