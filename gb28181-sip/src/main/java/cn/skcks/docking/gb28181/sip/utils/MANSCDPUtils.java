package cn.skcks.docking.gb28181.sip.utils;

import cn.skcks.docking.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;

@Slf4j
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
        // mapper.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, false);
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class XMLBuilder<T> {
        @Builder.Default
        @JsonIgnore
        private String encoding = GB28181Constant.CHARSET;

        private T query;

        private T response;

        private T notify;

        private T control;
    }

    @SneakyThrows
    public static byte[] writeXmlWithEncoding(Object obj, String charset){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, charset);
        outputStreamWriter.write(MessageFormat.format("<?xml version=\"1.0\" encoding=\"{0}\" ?>\n", charset));
        mapper.writer().writeValue(outputStreamWriter,obj);
        return byteArrayOutputStream.toByteArray();
    }

    public static <T> String toQuery(XMLBuilder<T> wrapper){
        return MANSCDPUtils.toXml(wrapper.getQuery(), wrapper.encoding);
    }

    public static <T> String toResponse(XMLBuilder<T> wrapper){
        return MANSCDPUtils.toXml(wrapper.getResponse(), wrapper.encoding);
    }

    public static <T> String toNotify(XMLBuilder<T> wrapper){
        return MANSCDPUtils.toXml(wrapper.getNotify(), wrapper.encoding);
    }

    public static <T> String toControl(XMLBuilder<T> wrapper){
        return MANSCDPUtils.toXml(wrapper.getControl(), wrapper.encoding);
    }

    public static String toXml(Object obj, String inputCharset,String outputCharset) {
        try {
            return new String(toByteXml(obj, inputCharset), Charset.forName(outputCharset));
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
            return new String(toByteXml(obj, charset), Charset.forName(charset));
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
            return writeXmlWithEncoding(obj, charset.name());
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
            return toByteXml(obj, Charset.forName(charset));
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
        return parse(new String(xml,charset), clazz);
    }

    public static <T> T parse(String xml, TypeReference<T> clazz) {
        return parse(xml, clazz, GB28181Constant.CHARSET);
    }

    @SneakyThrows
    public static <T> T parse(String xml, TypeReference<T> clazz, String charset) {
        return parse(xml, clazz, Charset.forName(charset));
    }

    public static <T> T parse(String xml, TypeReference<T> clazz, Charset charset) {
        try {
            return mapper.readValue(xml.getBytes(charset), clazz);
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
