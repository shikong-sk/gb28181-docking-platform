package cn.skcks.docking.gb28181.core.sip.gb28181.sdp;

import gov.nist.javax.sdp.SessionDescriptionImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.sdp.*;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@Data
public class GB28181Description extends SessionDescriptionImpl implements SessionDescription {


    public static class Convertor {
        @SneakyThrows
       public static GB28181Description convert(SessionDescriptionImpl sessionDescription){
            GB28181Description gb28181Description = new GB28181Description();
            SessionName sessionName = sessionDescription.getSessionName();
            if(sessionName != null){
                gb28181Description.setSessionName(sessionName);
            }
            gb28181Description.setMediaDescriptions(sessionDescription.getMediaDescriptions(true));
            gb28181Description.setBandwidths(sessionDescription.getBandwidths(true));

            Connection connection = sessionDescription.getConnection();
            if (connection != null){
                gb28181Description.setConnection(connection);
            }

            gb28181Description.setEmails(sessionDescription.getEmails(true));

            gb28181Description.setTimeDescriptions(sessionDescription.getTimeDescriptions(true));

            Origin origin = sessionDescription.getOrigin();
            if(origin != null){
                gb28181Description.setOrigin(origin);
            }

            gb28181Description.setAttributes(sessionDescription.getAttributes(true));

            URI uri = sessionDescription.getURI();
            if(uri != null){
                gb28181Description.setURI(uri);
            }
            return gb28181Description;
       }
    }

    private SsrcField ssrcField;

    GB28181Description(){
        super();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(getSsrcField() == null ? "" : getSsrcField().toString());
        return sb.toString();
    }
}
