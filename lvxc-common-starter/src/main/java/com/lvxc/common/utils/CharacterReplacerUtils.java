package com.lvxc.common.utils;

import com.lvxc.web.common.base.HsServerException;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lvxc.web.common.base.ResultEnum.EXECUTE_FAIL;

/**
 * @Author caoyq
 * @Date 2023/12/27 15:53
 * @PackageName:com.example.utils
 * @ClassName: CharacterReplacerUtils
 * @Describe 用于替换特殊字符 之前在shell注入时碰到
 * @Version 1.0
 */
public class CharacterReplacerUtils {
    public static void replaceCharacters(Object object) {
        Class<?> objClass = object.getClass();
        Field[] fields = objClass.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                Object value = field.get(object);
                if (value != null) {
                    if (value instanceof String) {
                        String replacedValue = replaceSpecialCharacters((String) value);
                        field.set(object, replacedValue);
                    } else if (value instanceof StringBuilder) {
                        StringBuilder stringBuilder = (StringBuilder) value;
                        String replacedValue = replaceSpecialCharacters(stringBuilder.toString());
                        stringBuilder.replace(0, stringBuilder.length(), replacedValue);
                    } else if (value.getClass().isArray()) {
                        if (value instanceof String[]) {
                            String[] array = (String[]) value;
                            for (int i = 0; i < array.length; i++) {
                                array[i] = replaceSpecialCharacters(array[i]);
                            }
                        } else if (value instanceof Object[]) {
                            Object[] objectArray = (Object[]) value;
                            for (int i = 0; i < objectArray.length; i++) {
                                Object arrayElement = objectArray[i];
                                if (arrayElement instanceof String) {
                                    objectArray[i] = replaceSpecialCharacters((String) arrayElement);
                                } else if (arrayElement instanceof StringBuilder) {
                                    StringBuilder sb = (StringBuilder) arrayElement;
                                    objectArray[i] = new StringBuilder(replaceSpecialCharacters(sb.toString()));
                                }

                            }
                        }

                    } else if (value instanceof Collection<?>) {
                        Collection<?> collection = (Collection<?>) value;
                        Collection<Object> replacedCollection = new ArrayList<>();
                        for (Object element : collection) {
                            if (element instanceof String) {
                                replacedCollection.add(replaceSpecialCharacters((String) element));
                            } else if (element instanceof StringBuilder) {
                                StringBuilder sb = (StringBuilder) element;
                                replacedCollection.add(new StringBuilder(replaceSpecialCharacters(sb.toString())));
                            }

                        }
                        field.set(object, replacedCollection);
                    }

                }
            }
        } catch (Exception e) {
            throw new HsServerException(EXECUTE_FAIL);
        }
    }


    /**
     * 可以传入字符串然后返回替换后的新的字符串结果
     *
     * @param value
     * @return
     */
    private static String replaceSpecialCharacters(String value) {
        Pattern pattern = Pattern.compile("[.$()/]|sleep|bin");
        Matcher matcher = pattern.matcher(value);
        return matcher.replaceAll("");
    }


    public static void main(String[] args) {
        // demo示例
        SupplyRequestData supplyRequestData = new SupplyRequestData();
        supplyRequestData.setSrc_reg(Arrays.asList("$/binsleep..//\\]]]\\\\\\....哈哈哈哈哈"));
        supplyRequestData.setAgg_name("$/binsleep..//\\\\]]]\\\\\\\\\\\\....哈哈哈哈哈");
        replaceCharacters(supplyRequestData);
        System.out.println(supplyRequestData);

        String a = "bin/sleep/() $ 11111hahahh ";
        a = replaceSpecialCharacters(a);
        System.out.println(a);
    }
}

class SupplyRequestData{
    @ApiModelProperty("加总规则名称")
    private String agg_name;
    @ApiModelProperty("调整比例")
    private Double shock_rate;
    @ApiModelProperty("数据源")
    private String data_name;
    @ApiModelProperty("年份")
    private Integer year;
    @ApiModelProperty("发起国家")
    private List<String> src_reg;
    @ApiModelProperty("影响国家")
    private List<String> dst_reg;
    @ApiModelProperty("发起国家产业")
    private List<String> src_reg_inds;
    @ApiModelProperty("影响国家产业")
    private List<String> dst_reg_inds;

    public SupplyRequestData() {
    }

    public void setAgg_name(String agg_name) {
        this.agg_name = agg_name;
    }

    public void setShock_rate(Double shock_rate) {
        this.shock_rate = shock_rate;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setSrc_reg(List<String> src_reg) {
        this.src_reg = src_reg;
    }

    public void setDst_reg(List<String> dst_reg) {
        this.dst_reg = dst_reg;
    }

    public void setSrc_reg_inds(List<String> src_reg_inds) {
        this.src_reg_inds = src_reg_inds;
    }

    public void setDst_reg_inds(List<String> dst_reg_inds) {
        this.dst_reg_inds = dst_reg_inds;
    }

    @Override
    public String toString() {
        return "SupplyRequestData{" +
                "agg_name='" + agg_name + '\'' +
                ", shock_rate=" + shock_rate +
                ", data_name='" + data_name + '\'' +
                ", year=" + year +
                ", src_reg=" + src_reg +
                ", dst_reg=" + dst_reg +
                ", src_reg_inds=" + src_reg_inds +
                ", dst_reg_inds=" + dst_reg_inds +
                '}';
    }
}
