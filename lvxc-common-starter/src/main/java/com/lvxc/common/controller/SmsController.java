package com.lvxc.common.controller;


import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Api(tags = "通用接口-短信验证码")
@RestController
@RequestMapping("/common/sms")
public class SmsController {

}
