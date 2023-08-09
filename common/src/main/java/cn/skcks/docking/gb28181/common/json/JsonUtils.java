package cn.skcks.docking.gb28181.common.json;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SuppressWarnings("unused")
public class JsonUtils {
    public static final ObjectMapper mapper;
    public static final ObjectMapper compressMapper;

    static {
        mapper = new ObjectMapper();
        compressMapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        compressMapper.disable(SerializationFeature.INDENT_OUTPUT);

        // 返回内容中 不含 值为 null 的字段
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        compressMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        compressMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        compressMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        // 允许出现特殊字符和转义符
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        compressMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        // 允许出现单引号
        mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        compressMapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parse(String json, TypeReference<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toCompressJson(Object obj) {
        try {
            return compressMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        return JsonUtils.parse(JsonUtils.toJson(object), clazz);
    }
}
