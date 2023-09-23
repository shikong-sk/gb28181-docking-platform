package cn.skcks.docking.gb28181.sdp;

import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.SDPField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SsrcField extends SDPField {
    private static final String SSRC_FIELD = "y=";
    public SsrcField() {
        super(SSRC_FIELD);
    }

    private String ssrc;

    @Override
    public String encode() {
        return SSRC_FIELD + ssrc + Separators.NEWLINE;
    }

    public String toString(){
        return encode();
    }
}
