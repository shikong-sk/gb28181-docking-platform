package cn.skcks.docking.gb28181.service.record.convertor;

import cn.skcks.docking.gb28181.service.record.vo.RecordInfoItemVO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class RecordConvertor {
    public static final RecordConvertor INSTANCE = Mappers.getMapper(RecordConvertor.class);
    abstract public List<RecordInfoItemVO> dto2Vo(List<RecordInfoItemDTO> dto);

    abstract public RecordInfoItemVO dto2Vo(RecordInfoItemDTO dto);
}
