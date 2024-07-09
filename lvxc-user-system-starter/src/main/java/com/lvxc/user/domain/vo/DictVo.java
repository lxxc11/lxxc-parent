package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DictVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * key
     */
    @ApiModelProperty(value = "key")
    private String key;

    /**
     * label
     */
    @ApiModelProperty(value = "label")
    private String label;

    /**
     * 字典
     */
    @ApiModelProperty(value = "字典")
    private List<SysDictListVo> dictList;

}
