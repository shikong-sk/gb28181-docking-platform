package cn.skcks.docking.gb28181.annotation.web.methods;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * @author Shikong
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JsonMapping(method = RequestMethod.DELETE)
public @interface DeleteJson {
    /**
     * Alias for {@link JsonMapping#name}.
     */
    @AliasFor(annotation = JsonMapping.class)
    String name() default "";

    /**
     * Alias for {@link JsonMapping#value}.
     */
    @AliasFor(annotation = JsonMapping.class)
    String[] value() default {};

    /**
     * Alias for {@link JsonMapping#path}.
     */
    @AliasFor(annotation = JsonMapping.class)
    String[] path() default {};

    /**
     * Alias for {@link JsonMapping#params}.
     */
    @AliasFor(annotation = JsonMapping.class)
    String[] params() default {};

    /**
     * Alias for {@link JsonMapping#headers}.
     */
    @AliasFor(annotation = JsonMapping.class)
    String[] headers() default {};

    /**
     * Alias for {@link JsonMapping#consumes}.
     * @since 4.3.5
     */
    @AliasFor(annotation = JsonMapping.class)
    String[] consumes() default {};
}
