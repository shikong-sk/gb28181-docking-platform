package cn.skcks.docking.gb28181.common.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.nio.charset.Charset;

@SuppressWarnings({"unused"})
public class XmlUtils {
    private static final XmlMapper mapper = new XmlMapper();
    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        // 允许出现特殊字符和转义符
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        // 允许出现单引号
        mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        // 大驼峰 (首字母大写)
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.UpperCamelCaseStrategy());
        // 添加 xml 头部声明
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    public static String toXml(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toByteXml(Object obj) {
        try {
            return mapper.writeValueAsString(obj).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toByteXml(Object obj, Charset charset) {
        try {
            return mapper.writeValueAsString(obj).getBytes(charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toByteXml(Object obj, String charset) {
        try {
            return mapper.writeValueAsString(obj).getBytes(Charset.forName(charset));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parse(byte[] xml, Class<T> clazz) {
        return parse(new String(xml), clazz);
    }

    public static <T> T parse(byte[] xml, Class<T> clazz, Charset charset) {
        return parse(new String(xml, charset), clazz);
    }

    public static <T> T parse(byte[] xml, Class<T> clazz, String charset) {
        return parse(new String(xml, Charset.forName(charset)), clazz);
    }

    public static <T> T parse(String xml, Class<T> clazz) {
        try {
            return mapper.readValue(xml, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parse(byte[] xml, TypeReference<T> clazz) {
        return parse(new String(xml), clazz);
    }

    public static <T> T parse(byte[] xml, TypeReference<T> clazz, Charset charset) {
        return parse(new String(xml, charset), clazz);
    }

    public static <T> T parse(byte[] xml, TypeReference<T> clazz, String charset) {
        return parse(new String(xml, Charset.forName(charset)), clazz);
    }

    public static <T> T parse(String xml, TypeReference<T> clazz) {
        try {
            return mapper.readValue(xml, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        return XmlUtils.parse(XmlUtils.toXml(object), clazz);
    }
}
