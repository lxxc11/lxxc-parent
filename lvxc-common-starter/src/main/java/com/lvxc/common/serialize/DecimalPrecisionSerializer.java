package com.lvxc.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lvxc.common.annotation.DecimalPrecision;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @ClassName DecimalPrecisionSerializer
 * @Description 数值序列化处理
 * @Author hux
 * @Date 2023/10/16
 **/
public class DecimalPrecisionSerializer extends JsonSerializer<Number> implements ContextualSerializer {

    private int precision;

    private RoundingMode roundingMode;

    public DecimalPrecisionSerializer() {
    }

    public DecimalPrecisionSerializer(int precision, RoundingMode roundingMode) {
        this.precision = precision;
        this.roundingMode = roundingMode;
    }

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (number instanceof BigDecimal) {
            BigDecimal value = (BigDecimal) number;
            BigDecimal roundedNumber = value.setScale(precision, roundingMode);
            jsonGenerator.writeNumber(roundedNumber);
        }
        if (number instanceof Double || number instanceof Float) {
            Double aDouble = number instanceof Double ? (Double) number : number.doubleValue();
            StringBuilder patternBuilder = new StringBuilder("#.");
            for (int i = 0; i < precision; i++) {
                patternBuilder.append("0");
            }
            String pattern = patternBuilder.toString();
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            double formattedValue = Double.parseDouble(decimalFormat.format(aDouble));
            jsonGenerator.writeNumber(formattedValue);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), BigDecimal.class) || Objects.equals(beanProperty.getType().getRawClass(), Double.class)
                    || Objects.equals(beanProperty.getType().getRawClass(), Float.class)) {
                DecimalPrecision decimalPrecision = beanProperty.getAnnotation(DecimalPrecision.class);
                if (decimalPrecision == null) {
                    decimalPrecision = beanProperty.getContextAnnotation(DecimalPrecision.class);
                }
                if (decimalPrecision != null) {
                    return new DecimalPrecisionSerializer(decimalPrecision.precision(), decimalPrecision.roundingMode());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        } else {
            return serializerProvider.findNullValueSerializer(null);
        }
    }
}
