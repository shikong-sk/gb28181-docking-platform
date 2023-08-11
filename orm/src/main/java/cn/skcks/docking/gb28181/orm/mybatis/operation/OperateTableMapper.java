package cn.skcks.docking.gb28181.orm.mybatis.operation;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateTableMapper {
    // int createNewTable(@Param("tableName")String tableName);
    void createDeviceTable();
}
