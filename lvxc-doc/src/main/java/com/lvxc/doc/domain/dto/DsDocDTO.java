package com.lvxc.doc.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel(value = "数据库设计请求参数类")
public class DsDocDTO {
    @NonNull
    private String schemas;
    @NonNull
    private String docName;
    private String username;
    private String password;
    private String url;
}
