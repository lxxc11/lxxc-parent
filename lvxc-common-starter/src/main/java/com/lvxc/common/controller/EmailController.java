package com.lvxc.common.controller;

import com.lvxc.common.dto.EmailDto;
import com.lvxc.common.utils.EmailUtil;
import com.lvxc.web.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;


/**
 * @Author caoyq
 * @Date 2023/9/14 17:37
 * @PackageName:com.lvxc.common.controller
 * @ClassName: EmailController
 * @Version 1.0
 */
@Log4j2
@Api(tags = "通用接口-发送邮件")
@RestController
@RequestMapping("/common/email")
public class EmailController {


    @ApiOperation(value = "发送邮件")
    @GetMapping("/send")
    public ResponseResult send(@RequestBody EmailDto emailDto) {
        EmailUtil.send(emailDto);
        return ResponseResult.success();
    }
}
