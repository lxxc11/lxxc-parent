package com.lvxc.enc.annotation;

import com.lvxc.enc.condition.OnMissingBeanCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Similar to Spring's condition but supports placeholders.
 * It only works with explicit {@link org.springframework.context.annotation.Bean} name attribute specified.
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingBeanCondition.class)
public @interface ConditionalOnMissingBean {
}
