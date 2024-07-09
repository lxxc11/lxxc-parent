package com.lvxc.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author caoyq
 * @Date 2023/9/22 13:36
 * @PackageName:com.example.bean.dto
 * @ClassName: EmailDto
 * @Version 1.0
 */
@ApiModel(value = "发送邮件实体类")
@Data
public class EmailDto {
    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    private String subject;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 收件人
     */
    @ApiModelProperty(value = "收件人")
    private List<EmailContact> to;

    /**
     * 抄送人
     */
    @ApiModelProperty(value = "抄送人")
    private List<EmailContact> cc;

    /**
     * 密送人
     */
    @ApiModelProperty(value = "密送人")
    private List<EmailContact> bcc;

    /**
     * 附件网络路径
     */
    @ApiModelProperty(value = "附件网络路径")
    private List<String> netUrl;

    /**
     * 附件本地路径
     */
    @ApiModelProperty(value = "附件本地路径")
    private List<String> localUrl;
}
