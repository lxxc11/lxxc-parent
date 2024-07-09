package com.lvxc.enc.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

/**
 * <p>ClassUtils class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public class ClassUtils {
    /**
     * <p>isAssignable.</p>
     *
     * @param type a {@link ParameterizedTypeReference} object
     * @param clazz a {@link Class} object
     * @return a boolean
     */
    public static boolean isAssignable(ParameterizedTypeReference<?> type, Class<?> clazz) {
        return ResolvableType.forType(type).isAssignableFrom(clazz);
    }
}
