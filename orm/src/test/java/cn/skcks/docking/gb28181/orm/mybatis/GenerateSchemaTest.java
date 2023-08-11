package cn.skcks.docking.gb28181.orm.mybatis;

import cn.skcks.docking.gb28181.orm.mybatis.operation.OperateTableMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// 生成 schema 元数据 表
@ActiveProfiles("pre-dev")
@SpringBootTest(classes = {GenerateSchemaTest.class})
@MapperScans(@MapperScan("cn.skcks.docking.gb28181.orm.mybatis"))
@SpringBootApplication
public class GenerateSchemaTest {
    @Autowired
    private OperateTableMapper operateTableMapper;

    @Test
    void contextLoads(){
        operateTableMapper.createDeviceTable();
    }
}
