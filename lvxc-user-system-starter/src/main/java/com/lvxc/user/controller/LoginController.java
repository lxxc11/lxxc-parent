package com.lvxc.user.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lvxc.user.common.*;
import com.lvxc.user.common.*;
import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.common.enums.DateEnum;
import com.lvxc.user.common.shiro.constants.ShiroConstants;
import com.lvxc.user.common.shiro.utils.JwtUtil;
import com.lvxc.user.domain.dto.SysLoginModel;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysUser;
import com.lvxc.user.service.ISysLogService;
import com.lvxc.user.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "用户登录-登出")
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysLogService sysLogService;

    // 登录失败次数
    @Value(value = "${user.loginFailureNum:5}")
    private Integer loginFailureNum;

    @Value(value = "${user.loginFailureSmsNum:3}")
    private Integer loginFailureSmsNum;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("登录接口")
    @PostMapping(value = "/login")
    public ResponseResult login(@RequestBody SysLoginModel sysLoginModel) {
        long beginTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(sysLoginModel.getUserName())) {
            return ResponseResult.error("用户名不能为空！");
        }
        if (StringUtils.isEmpty(sysLoginModel.getPassword())) {
            return ResponseResult.error("密码不能为空！");
        }
        if (StringUtils.isEmpty(sysLoginModel.getPlatformId())) {
            return ResponseResult.error("平台id不能为空！");
        }
        // 登录失败次数校验
        String loginFailKey = "login_failure:" + sysLoginModel.getUserName();
//        if (redisUtil.hasKey(loginFailKey)) {
//            Integer failureNum = redisUtil.get(loginFailKey, Integer.class);
//            if (failureNum >= loginFailureNum) {
//                return ResponseResult.error("当前账号今日已连续登录失败5次，请第二天再登录！");
//            }
//            if (failureNum >= loginFailureSmsNum) {
//                String platformId = sysLoginModel.getPlatformId();
//                String smsCode = sysLoginModel.getSmsCode();
//                String smsMobile = sysLoginModel.getSmsMobile();
//                if(StringUtils.isEmpty(smsCode) || StringUtils.isEmpty(smsMobile)){
//                    // 查询用户
//                    LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//                    queryWrapper.eq(SysUser::getDelFlag, false);
//                    queryWrapper.eq(SysUser::getUserName, sysLoginModel.getUserName());
//                    SysUser sysUser = sysUserService.getOne(queryWrapper);
//                    PrivateKey privateKey = KeyUtils.createPrivateKey(sysUser.getPrivateKey());
//                    String contact = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getContact()), privateKey));
////                return ResponseResult.error(LOGIN_ERROR_SMS, DesensitizedUtil.mobilePhone(contact));
//                    return ResponseResult.error(LOGIN_ERROR_SMS, contact);
//                }
//                String smsKey ="smsKey:" + platformId + ":" + smsMobile;
//                String redisSmsCode = redisUtil.get(smsKey, String.class);
//                if(!smsCode.equals(redisSmsCode)){
//                    return ResponseResult.error("短信验证码错误！");
//                }
//            }
//        }
        // 校验验证码
        String captcha = sysLoginModel.getCaptcha();
        if (StringUtils.isEmpty(captcha)) {
            return ResponseResult.error("验证码无效！");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + sysLoginModel.getCheckKey(), "utf-8");
        String checkCode = redisUtil.get(realKey, String.class);
        // 当进入登录页时，有一定几率出现验证码错误 #1714
        if (StringUtils.isEmpty(checkCode) || !StringUtils.equals(checkCode, lowerCaseCaptcha)){
            log.warn("验证码错误，key= {} , Ui checkCode= {}, Redis checkCode = {}", sysLoginModel.getCheckKey(), lowerCaseCaptcha, checkCode);
            redisUtil.delete(realKey);
            return ResponseResult.error("验证码错误！");
        }
        // 查询用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDelFlag, false);
        queryWrapper.eq(SysUser::getUserName, sysLoginModel.getUserName());
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        //校验用户是否有效
        ResponseResult result = sysUserService.checkUserIsEffective(sysUser, sysLoginModel.getPlatformId());
        if (!result.isSuccess()) {
            redisUtil.delete(realKey);
            return result;
        }
        // 解析密码
        String password = new Md5Hash(sysLoginModel.getPassword(), sysUser.getSalt(), ShiroConstants.HASH_ITERATORS).toHex();
        if (!StringUtils.equals(password, sysUser.getPassword())) {
            // 登录失败次数记录
            long expireTime = DateUtil.betweenTheTwoDate(new Date(), DateUtil.getAfterDate(new Date(), 1, DateEnum.DAY.getCode()));
            if (redisUtil.hasKey(loginFailKey)) {
                Integer loginFailureNum = redisUtil.get(loginFailKey, Integer.class) + 1;
                redisUtil.putWithExpireTime(loginFailKey, loginFailureNum, expireTime);
            } else {
                redisUtil.putWithExpireTime(loginFailKey, 1, expireTime);
            }
            redisUtil.delete(realKey);
//            if(redisUtil.get(loginFailKey, Integer.class) >= loginFailureSmsNum) { //大于等于三次需要手机验证码
//                PrivateKey privateKey = KeyUtils.createPrivateKey(sysUser.getPrivateKey());
//                String contact = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getContact()), privateKey));
//                return ResponseResult.error(LOGIN_ERROR_SMS, contact);
//            } else {
                return ResponseResult.error("用户名或密码错误！");
//            }
        }
        // 获取登录数据
        LoginUser loginUser = sysUserService.getLoginUser(sysUser, sysLoginModel.getPlatformId());
        // 生成token
        String token = JwtUtil.sign(sysLoginModel.getUserName(), password, sysLoginModel.getPlatformId());
        redisUtil.putWithExpireTime(ShiroConstants.PREFIX_USER_TOKEN + token, token, JwtUtil.EXPIRE_TIME);
        loginUser.setToken(token);
        redisUtil.putWithExpireTime(ShiroConstants.PREFIX_USER_INFO + token, loginUser, JwtUtil.EXPIRE_TIME);
        // token的key存入redis中,用于判断同一个平台只允许同一账户单个登录
        String loginOnlyOne = "login_only_one:" + sysLoginModel.getPlatformId() + "_" + sysLoginModel.getUserName();
        if (redisUtil.hasKey(loginOnlyOne)) {
            List<String> tokenList = redisUtil.getList(loginOnlyOne, String.class);
            tokenList.add(ShiroConstants.PREFIX_USER_TOKEN + token);
            redisUtil.putListWithExpireTime(loginOnlyOne, tokenList, JwtUtil.EXPIRE_TIME);
        } else {
            redisUtil.putListWithExpireTime(loginOnlyOne, Arrays.asList(new String[]{ShiroConstants.PREFIX_USER_TOKEN + token}), JwtUtil.EXPIRE_TIME);
        }
        // 所有用户查询放入redis中，异步操作
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            System.out.println("异步线程 =====> 开始 =====> " + new Date());
            LambdaQueryWrapper<SysUser> allQuery = new LambdaQueryWrapper<>();
            allQuery.eq(SysUser::getDelFlag, false);
            List<SysUser> allUserList = sysUserService.list(allQuery);
            for (SysUser user : allUserList) {
                PrivateKey privateKey = KeyUtils.createPrivateKey(user.getPrivateKey());
                if (!StringUtils.isEmpty(user.getRealName())) {
                    user.setRealName(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getRealName()), privateKey)));
                }
                if (!StringUtils.isEmpty(user.getContact())) {
                    user.setContact(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getContact()), privateKey)));
                }
                if (!StringUtils.isEmpty(user.getEmail())) {
                    user.setEmail(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getEmail()), privateKey)));
                }
                if (!StringUtils.isEmpty(user.getPosition())) {
                    user.setPosition(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getPosition()), privateKey)));
                }
            }
            Map<String, SysUser> allUserMap = allUserList.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
            redisUtil.put("_allUserMap", allUserMap);
            System.out.println("异步线程 =====> 结束 =====> " + new Date());
        }, executorService);
        executorService.shutdown(); // 回收线程池
        // 清空登录失败次数数据
        if (redisUtil.hasKey(loginFailKey)) {
            redisUtil.delete(loginFailKey);
        }
        // 添加日志
        sysLogService.addLog("登录", sysLoginModel.getPlatformId(), CommonConstant.LOG_TYPE_1, CommonConstant.OPERATE_TYPE_7,
                "com.lvxc.user.controller.login()", loginUser, sysLoginModel, System.currentTimeMillis() - beginTime);
        return ResponseResult.success("登录成功！", loginUser);
    }

    /**
     * 获取验证码
     *
     * @param key
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/randomImage", name = "发送登陆验证码")
    @ApiOperation("发送图片验证码")
    public ResponseResult<?> sendPictureCaptcha(@RequestParam(name = "key") String key, HttpServletResponse response) throws IOException {
        try {
            //生成验证码(去除辨识度不强的O与0、l与1)
            final String BASE_CHECK_CODES = "qwertyuipkjhgfdsazxcvbnmQWERTYUPKJHGFDSAZXCVBNM23456789";
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);

            //存到redis中
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            log.info("获取验证码，Redis checkCode = {}，key = {}", code, key);
            redisUtil.putWithExpireTime(realKey, lowerCaseCode, 300);

            //返回前端
            String base64 = RandImageUtil.generate(code);
            return ResponseResult.success(base64);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("获取验证码出错" + e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("退出登录")
    @PostMapping(value = "/logout")
    public ResponseResult<?> logout(HttpServletRequest request, HttpServletResponse response, String platformId) {
        long beginTime = System.currentTimeMillis();
        // 用户退出逻辑
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return ResponseResult.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDelFlag, false);
        queryWrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (sysUser != null) {
            // 清空用户登录Token缓存
            redisUtil.delete(ShiroConstants.PREFIX_USER_TOKEN + token);
            redisUtil.delete(ShiroConstants.PREFIX_USER_INFO + token);
            // 保存日志
            sysLogService.addLog("退出", platformId, CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_8,
                    "com.lvxc.user.controller.logout()", null, platformId, System.currentTimeMillis() - beginTime);
            // 调用shiro的logout
            SecurityUtils.getSubject().logout();
            return ResponseResult.success("退出登录成功！");
        } else {
            return ResponseResult.error("Token无效!");
        }
    }

    /**
     * 发送手机验证码
     *
     * @return
     */
    @AutoLog(value = "发送手机验证码")
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    @GetMapping(value = "/send/sms")
    public ResponseResult<?> sendSms(@RequestParam String contact, @RequestParam(required = false) Integer type) {
        String captcha = RandomUtil.randomNumbers(6);
        redisUtil.putWithExpireTime(contact + type, captcha, 300);
        return ResponseResult.success("发送成功", captcha);
    }

}
