package com.lvxc.user.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.common.config.enums.DySmsEnum;
import com.lvxc.common.utils.DySmsHelper;
import com.lvxc.user.common.RedisUtil;
import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysSendSmsDto;
import com.lvxc.user.domain.dto.SysUserDto;
import com.lvxc.user.domain.dto.SysUserPageDto;
import com.lvxc.user.domain.dto.UpdatePasswordDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysUserPageVo;
import com.lvxc.user.entity.SysUser;
import com.lvxc.user.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.Assert;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 用户中心-用户
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param dto
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "用户中心-用户表-分页列表查询")
    @ApiOperation(value = "用户中心-用户表-分页列表查询", notes = "用户中心-用户表-分页列表查询")
    @GetMapping(value = "/pageList")
    public ResponseResult<?> getPageList(SysUserPageDto dto,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysUserPageVo> page = new Page<>(pageNo, pageSize);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        dto.setPlatformIds(loginUser.getPlatformIds());
        IPage<SysUserPageVo> pageList = sysUserService.getPageList(page, dto);
        return ResponseResult.success(pageList);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-用户表-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "用户中心-用户表-添加", notes = "用户中心-用户表-添加")
    @RequiresPermissions("user:add")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysUserDto dto) throws Exception {
        return sysUserService.add(dto);
    }

    /**
     * 用户中心-用户表-编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-用户表-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-用户表-编辑", notes = "用户中心-用户表-编辑")
    @PostMapping(value = "/edit")
    @RequiresPermissions("user:update")
    public ResponseResult<?> edit(@RequestBody SysUserDto dto) throws Exception {
        return sysUserService.edit(dto);
    }

    /**
     * 用户中心-用户表-通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-用户表-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "用户中心-用户表-通过id删除", notes = "用户中心-用户表-通过id删除")
    @GetMapping(value = "/delete")
    @RequiresPermissions("user:del")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysUserService.delete(id);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-用户表-通过id查询")
    @ApiOperation(value = "用户中心-用户表-通过id查询", notes = "用户中心-用户表-通过id查询")
    @GetMapping(value = "/queryById")
    public ResponseResult<?> queryById(@RequestParam(name = "id") String id) {
        return sysUserService.queryById(id);
    }

    /**
     * 用户中心-用户表-重置密码
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-用户表-重置密码", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-用户表-重置密码", notes = "用户中心-用户表-重置密码")
    @GetMapping(value = "/resetPassword")
    public ResponseResult<?> resetPassword(@RequestParam(name = "id") String id) {
        return sysUserService.resetPassword(id);
    }

    /**
     * 用户中心-用户表-修改密码
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-用户表-修改密码", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-用户表-修改密码", notes = "用户中心-用户表-修改密码")
    @PostMapping(value = "/updatePassword")
    public ResponseResult<?> updatePassword(@RequestBody UpdatePasswordDto dto) {
        return sysUserService.updatePassword(dto);
    }

    /**
     * 用户中心-用户表-修改用户状态
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-用户表-修改用户状态", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-用户表-修改用户状态", notes = "用户中心-用户表-修改用户状态")
    @GetMapping(value = "/updateStatus")
    public ResponseResult<?> updateStatus(@RequestParam(name = "id") String id) {
        return sysUserService.updateStatus(id);
    }

    /**
     * 用户中心-用户表-校验密码
     *
     * @return
     */
    @AutoLog(value = "用户中心-用户表-校验密码是否过期")
    @ApiOperation(value = "用户中心-用户表-校验密码是否过期", notes = "用户中心-用户表-校验密码")
    @GetMapping(value = "/checkPassword")
    public ResponseResult<?> checkPassword() {
        return sysUserService.checkPassword();
    }

    /**
     * 根据token获取用户的登录信息(第三方接口)
     *
     * @param tokenStr
     * @return
     */
    @AutoLog(value = "用户中心-用户表-根据token获取用户的登录信息(第三方接口)")
    @ApiOperation(value = "用户中心-用户表-根据token获取用户的登录信息(第三方接口)", notes = "用户中心-用户表-根据token获取用户的登录信息(第三方接口)")
    @GetMapping(value = "/getLoginUserByName")
    public ResponseResult<?> getPageList(@RequestParam(name = "tokenStr") String tokenStr) {
        return sysUserService.getLoginUserByName(tokenStr);
    }

    @AutoLog(value = "用户中心-用户表-根据token获取用户的登录信息(第三方接口-针对长寿企业用户)")
    @ApiOperation(value = "用户中心-用户表-根据token获取用户的登录信息(第三方接口-针对长寿企业用户)", notes = "用户中心-用户表-根据token获取用户的登录信息(第三方接口-针对长寿企业用户)")
    @GetMapping(value = "/getLoginUserByNameNoCheck")
    public ResponseResult<?> getPageListNoCheck(@RequestParam(name = "tokenStr") String tokenStr) {
        return sysUserService.getLoginUserByNameNoCheck(tokenStr);
    }


    @ApiOperation(value = "根据用户id批量获取用户信息", notes = "根据用户id批量获取用户信息")
    @GetMapping(value = "/getUserByIds")
    public ResponseResult<?> getUserByIds(@RequestParam(name = "ids") String ids) {
        List<SysUser> list  = sysUserService.getUserByIds(ids);
        return ResponseResult.success(list);
    }

    /**
     * 用户中心-用户表-获取当前登录人最新信息
     *
     * @return
     */
    @AutoLog(value = "用户中心-用户表-获取当前登录人最新信息")
    @ApiOperation(value = "用户中心-用户表-获取当前登录人最新信息", notes = "用户中心-用户表-获取当前登录人最新信息")
    @GetMapping(value = "/getLoginUserInfo")
    public ResponseResult<?> getLoginUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return ResponseResult.success(loginUser);
    }



    @GetMapping(value = "/cas/validate", name = "帐号登录")
    @ApiOperation("CAS 票据验证")
    public ResponseResult<?> casValidate(@RequestParam String casServerUrlPrefix, @RequestParam String ticket, @RequestParam String service) throws TicketValidationException {
//        Cas20ServiceTicketValidator cas20ServiceTicketValidator = new Cas20ServiceTicketValidator("https://cas.aihuoshi.net/cas");
//        Assertion validate = cas20ServiceTicketValidator.validate("ST-300-vjKqXOarsj7FvYH030iatLflv2A-cas-547497bffb-6wz5l", "https://www.baidu.com");
        Cas20ServiceTicketValidator cas20ServiceTicketValidator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
        Assertion validate = cas20ServiceTicketValidator.validate(ticket, service);
//        用户名信息
        String name = validate.getPrincipal().getName();
        return ResponseResult.success(name);
    }


    @ApiOperation("发送短信验证码")
    @PostMapping(value = "/sendSms")
    public ResponseResult<String> sms(@RequestBody SysSendSmsDto userDto) {
        Assert.notNull(userDto.getSmsMobile(),"手机号不能为空!");
        Assert.notNull(userDto.getPlatformId(),"平台id不能为空!");
        ResponseResult<String> result = new ResponseResult<>();
        String smsMobile = userDto.getSmsMobile();
        String platformId = userDto.getPlatformId();
        String smsCodeKey ="smsKey:" + platformId + ":" + smsMobile;
        String smsNumKey ="smsNum:" + platformId + ":" + smsMobile;
        Integer smsCount = redisUtil.get(smsNumKey,Integer.class);
        if(smsCount!=null && smsCount>=3){
            result.setMessage("发送短信验证码过于频繁，请稍后再试");
            result.setCode(-1);
            return result;
        }
        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("code",captcha);
            DySmsHelper.sendSms(smsMobile,jsonObject, DySmsEnum.SMS_CODE);
        } catch (Exception e) {
            log.error("短信验证码发送失败", e);
            result.setMessage("短信验证码发送失败,请稍后重试");
            result.setCode(-1);
            return result;
        }
        //验证码15分钟内有效
        redisUtil.putWithExpireTime(smsCodeKey,captcha,15*60);
        redisUtil.incr(smsNumKey);
        redisUtil.putWithExpireTime(smsNumKey,redisUtil.get(smsNumKey,Integer.class), 15*60);
        return ResponseResult.success();
    }

    @ApiOperation(value = "根据用户名获取用户列表", notes = "根据用户名获取用户列表")
    @GetMapping(value = "/ext/getUserByFilter")
    public ResponseResult<?> getUserByFilter(@RequestParam(name = "filter") String filter) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDelFlag, false);
        if(StringUtils.isNotBlank(filter)) {
            wrapper.like(SysUser::getUserName, filter);
        }
        List<SysUser> list = sysUserService.list(wrapper);
        return ResponseResult.success(list);
    }

}
