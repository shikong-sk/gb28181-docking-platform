package cn.skcks.docking.gb28181.sip.utils;

import cn.skcks.docking.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.*;

import java.nio.charset.Charset;
import java.util.Objects;

@SuppressWarnings("unused")
public class MANSCDPUtils {
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
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, false);
        mapper.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true);
    }

    @JacksonXmlRootElement(localName = "xml")
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class XMLWrapper<T> {
        @Builder.Default
        @JacksonXmlProperty(isAttribute = true, localName = "encoding")
        private String encoding = GB28181Constant.CHARSET;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T query;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T response;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T notify;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T control;
    }


    public static String toXml(Object obj, String inputCharset,String outputCharset) {
        try {
            return new String(Objects.requireNonNull(toByteXml(obj, inputCharset)), Charset.forName(outputCharset));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toXml(Object obj, String charset) {
        if(obj == null){
            return null;
        }
        try {
            return new String(toByteXml(obj, GB28181Constant.CHARSET), Charset.forName(charset));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toXml(Object obj) {
        if(obj == null){
            return null;
        }
        try {
            return new String(toByteXml(obj, GB28181Constant.CHARSET), GB28181Constant.CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toByteXml(Object obj) {
        try {
            return toByteXml(obj, GB28181Constant.CHARSET);
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
        if(obj == null){
            return null;
        }
        try {
            return mapper.writeValueAsString(obj).getBytes(Charset.forName(charset));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public static <T> T parse(byte[] xml, Class<T> clazz) {
        return parse(xml, clazz, GB28181Constant.CHARSET);
    }

    public static <T> T parse(byte[] xml, Class<T> clazz, String charset) {
        return parse(xml, clazz, Charset.forName(charset));
    }

    public static <T> T parse(byte[] xml, Class<T> clazz, Charset charset) {
        return parse(new String(xml, charset), clazz);
    }

    public static <T> T parse(String xml, Class<T> clazz) {
        try {
            return mapper.readValue(xml, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public static <T> T parse(byte[] xml, TypeReference<T> clazz) {
        return parse(xml, clazz, GB28181Constant.CHARSET);
    }

    public static <T> T parse(byte[] xml, TypeReference<T> clazz, String charset) {
        return parse(xml, clazz, Charset.forName(charset));
    }

    public static <T> T parse(byte[] xml, TypeReference<T> clazz, Charset charset) {
        return parse(new String(xml, charset), clazz);
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
        return MANSCDPUtils.convert(object, clazz, GB28181Constant.CHARSET);
    }

    @SneakyThrows
    public static <T> T convert(Object object, Class<T> clazz, String charset) {
        return MANSCDPUtils.convert(object, clazz, Charset.forName(charset));
    }

    @SneakyThrows
    public static <T> T convert(Object object, Class<T> clazz, Charset charset) {
        if(object == null){
            return null;
        }
        return MANSCDPUtils.parse(MANSCDPUtils.toXml(object).getBytes(charset), clazz, charset);
    }
}
