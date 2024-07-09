package com.lvxc.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author caoyq
 * @Date 2023/9/22 13:40
 * @PackageName:com.example.bean.dto
 * @ClassName: EmailContact
 * @Version 1.0
 */
@ApiModel(value = "收件人实体类")
@Data
public class EmailContact {
    /**
     * 姓名
     */
    @ApiModelProperty(value = "收件人姓名")
    private String name;
    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "收件人的地址")
    private String address;
}

