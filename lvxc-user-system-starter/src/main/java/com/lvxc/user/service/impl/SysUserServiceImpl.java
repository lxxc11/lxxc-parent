package com.lvxc.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.RedisUtil;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.enums.DateEnum;
import com.lvxc.user.common.shiro.constants.ShiroConstants;
import com.lvxc.user.common.DateUtil;
import com.lvxc.user.common.KeyUtils;
import com.lvxc.user.common.SaltUtil;
import com.lvxc.user.common.Sm2Util;
import com.lvxc.user.common.shiro.utils.JwtUtil;
import com.lvxc.user.converter.*;
import com.lvxc.user.domain.dto.SysUserDto;
import com.lvxc.user.domain.dto.SysUserPageDto;
import com.lvxc.user.domain.dto.UpdatePasswordDto;
import com.lvxc.user.domain.dto.third.EditPasswordDto;
import com.lvxc.user.domain.dto.third.UserEditDto;
import com.lvxc.user.domain.vo.*;
import com.lvxc.user.entity.*;
import com.lvxc.user.mapper.flw.SysUserMapper;
import com.lvxc.user.service.*;
import com.lvxc.user.converter.SysDepartConverter;
import com.lvxc.user.converter.SysRoleConverter;
import com.lvxc.user.converter.SysUserConverter;
import com.lvxc.user.domain.vo.*;
import com.lvxc.user.entity.*;
import com.lvxc.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRolePlatformService sysRolePlatformService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysPlatformService sysPlatformService;

    @Autowired
    private ISysFunctionService sysFunctionService;

    @Autowired
    private ISysDataSetService sysDataSetService;

    @Autowired
    private ISysDataSetIndexService sysDataSetIndexService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private SysUserConverter sysUserConverter;

    @Autowired
    private SysDepartConverter sysDepartConverter;

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Autowired
    private RedisUtil redisUtil;

    // 初始密码 Guest@5678
    @Value(value = "${user.initialPassword:b11ada868c10bfdcac031842a3b94122}")
    private String initialPassword;

    // 密码可重复次数
    @Value(value = "${user.historyPasswordNum:5}")
    private Integer historyPasswordNum;

    // 密码过期时间（月）
    @Value(value = "${user.PasswordOverdueTime:3}")
    private Integer passwordOverdueTime;

    @Override
    public IPage<SysUserPageVo> getPageList(Page<SysUserPageVo> page, SysUserPageDto dto) {
        IPage<SysUserPageVo> pageList = baseMapper.getPageList(page, dto);
        if (!CollectionUtils.isEmpty(pageList.getRecords())) {
            for (SysUserPageVo record : pageList.getRecords()) {
                PrivateKey privateKey = KeyUtils.createPrivateKey(record.getPrivateKey());
                if (!StringUtils.isEmpty(record.getRealName())) {
                    String realName = new String(Sm2Util.decrypt(Base64.getDecoder().decode(record.getRealName()), privateKey));
                    record.setRealName(realName);
                }
                if (!StringUtils.isEmpty(record.getContact())) {
                    String contact = new String(Sm2Util.decrypt(Base64.getDecoder().decode(record.getContact()), privateKey));
                    record.setContact(contact);
                }
                if (!StringUtils.isEmpty(record.getEmail())) {
                    String email = new String(Sm2Util.decrypt(Base64.getDecoder().decode(record.getEmail()), privateKey));
                    record.setEmail(email);
                }
                if (!StringUtils.isEmpty(record.getPosition())) {
                    String position = new String(Sm2Util.decrypt(Base64.getDecoder().decode(record.getPosition()), privateKey));
                    record.setPosition(position);
                }
            }
        }
        return pageList;
    }

    @Override
    @Transactional
    public ResponseResult add(SysUserDto dto) throws Exception {
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysUser sysUser = sysUserConverter.sysUserDtoToDao(dto);
        sysUser.setPassword(initialPassword);
        // 密码加密
        passwordEncrypt(sysUser);
        // 隐私数据SM2加密
        sm2Encrypt(sysUser);
        // 记录历史密码
        sysUser.setHistoryPassword(sysUser.getPassword());
        // 记录修改密码时间
        sysUser.setPasswordUpdateTime(new Date());
        // 添加用户
        sysUser.setSuperFlag(false);
        save(sysUser);
        // 添加用户部门表、添加用户角色表
        addUserRelation(sysUser.getId(), dto);
        // Redis用户更新
        updateRedisUser(sysUser);
        return ResponseResult.success("新增成功！");
    }

    /**
     * 添加用户部门表、用户角色表
     *
     * @param userId
     * @param dto
     */
    public void addUserRelation(String userId, SysUserDto dto) {
        // 添加用户部门表
        List<SysUserDepart> sysUserDepartList = new ArrayList<>();
        SysUserDepart userDepart = new SysUserDepart();
        userDepart.setUserId(userId);
        userDepart.setDepartId(dto.getDepartId());
        userDepart.setType(0);
        sysUserDepartList.add(userDepart);
        if (!CollectionUtils.isEmpty(dto.getPartTimeDepartIds())) {
            for (String partTimeDepartId : dto.getPartTimeDepartIds()) {
                SysUserDepart partTimeDepart = new SysUserDepart();
                partTimeDepart.setUserId(userId);
                partTimeDepart.setDepartId(partTimeDepartId);
                partTimeDepart.setType(1);
                sysUserDepartList.add(partTimeDepart);
            }
        }
        sysUserDepartService.saveBatch(sysUserDepartList);
        // 添加用户角色表
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (String roleId : dto.getRoleIds()) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        sysUserRoleService.saveBatch(userRoleList);
    }

    @Override
    @Transactional
    public ResponseResult edit(SysUserDto dto) throws Exception {
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysUser sysUser = getById(dto.getId());
        sysUser.setUserName(dto.getUserName());
        sysUser.setRealName(dto.getRealName());
        sysUser.setContact(dto.getContact());
        sysUser.setEmail(dto.getEmail());
        sysUser.setPosition(dto.getPosition());
        sysUser.setEffectiveTime(dto.getEffectiveTime());
        sysUser.setStatus(dto.getStatus());
        sysUser.setHeadPicture(dto.getHeadPicture());
        // 隐私数据SM2加密
        sm2Encrypt(sysUser);
        // 修改用户
        updateById(sysUser);
        // 删除用户部门表、用户角色表
        deleteUserRelation(sysUser.getId());
        // 添加用户部门表、用户角色表
        addUserRelation(sysUser.getId(), dto);
        // redis用户修改
        updateRedisUser(sysUser);
        return ResponseResult.success("修改成功！");
    }

    /**
     * 删除用户部门表、用户角色表
     *
     * @param userId
     */
    public void deleteUserRelation(String userId) {
        // 删除用户部门表
        LambdaQueryWrapper<SysUserDepart> userDepartQuery = new LambdaQueryWrapper<>();
        userDepartQuery.eq(SysUserDepart::getDelFlag, false);
        userDepartQuery.eq(SysUserDepart::getUserId, userId);
        sysUserDepartService.remove(userDepartQuery);
        // 删除用户角色表
        LambdaQueryWrapper<SysUserRole> userRoleQuery = new LambdaQueryWrapper<>();
        userRoleQuery.eq(SysUserRole::getDelFlag, false);
        userRoleQuery.eq(SysUserRole::getUserId, userId);
        sysUserRoleService.remove(userRoleQuery);
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        SysUser sysUser = getById(id);
        sysUser.setDelFlag(true);
        updateById(sysUser);
        // 删除用户部门表、用户角色表
        deleteUserRelation(sysUser.getId());
        // redis用户修改
        Map allUserMap = redisUtil.get("_allUserMap", Map.class);
        allUserMap.keySet().removeIf(key -> StringUtils.equals(key.toString(), id));
        redisUtil.put("_allUserMap", allUserMap);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public ResponseResult queryById(String id) {
        SysUser sysUser = getById(id);
        // 隐私数据SM2解密
        sm2Decrypt(sysUser);
        SysUserVo sysUserVo = sysUserConverter.sysUserDaoToVo(sysUser);
        // 查询用户部门表
        LambdaQueryWrapper<SysUserDepart> userDepartQuery = new LambdaQueryWrapper<>();
        userDepartQuery.eq(SysUserDepart::getDelFlag, false);
        userDepartQuery.eq(SysUserDepart::getUserId, id);
        List<SysUserDepart> sysUserDepart = sysUserDepartService.list(userDepartQuery);
        if (!CollectionUtils.isEmpty(sysUserDepart)) {
            List<SysUserDepart> userDepart = sysUserDepart.stream().filter(e -> e.getType() == 0).collect(Collectors.toList());
            List<String> partTimeDepartIds = sysUserDepart.stream().filter(e -> e.getType() == 1).map(SysUserDepart::getDepartId).collect(Collectors.toList());
            sysUserVo.setDepartId(userDepart.get(0).getDepartId());
            sysUserVo.setPartTimeDepartIds(partTimeDepartIds);
        }
        // 查询用户角色表
        LambdaQueryWrapper<SysUserRole> userRoleQuery = new LambdaQueryWrapper<>();
        userRoleQuery.eq(SysUserRole::getDelFlag, false);
        userRoleQuery.eq(SysUserRole::getUserId, id);
        List<SysUserRole> list = sysUserRoleService.list(userRoleQuery);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> roleIds = list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            sysUserVo.setRoleIds(roleIds);
        }
        return ResponseResult.success(sysUserVo);
    }

    @Override
    @Transactional
    public ResponseResult resetPassword(String id) {
        SysUser sysUser = getById(id);
        // 重置密码为 Guest@5678
        sysUser.setPassword(initialPassword);
        // 密码加密
        passwordEncrypt(sysUser);
        // 更新历史密码
        List<String> historyPasswordList = Arrays.asList(sysUser.getHistoryPassword().split(","));
        updateHistoryPassword(historyPasswordList, sysUser);
        // 修改更新密码时间
        sysUser.setPasswordUpdateTime(new Date());
        updateById(sysUser);
        // redis用户修改
        updateRedisUser(sysUser);
        return ResponseResult.success("重置成功！");
    }

    @Override
    @Transactional
    public ResponseResult updatePassword(UpdatePasswordDto dto) {
        if (StringUtils.isEmpty(dto.getOldPassword())) {
            return ResponseResult.error("旧密码不能为空！");
        }
        if (StringUtils.isEmpty(dto.getNewPassword())) {
            return ResponseResult.error("新密码不能为空！");
        }
        if (StringUtils.isEmpty(dto.getConfirmPassword())) {
            return ResponseResult.error("确认密码不能为空！");
        }
        if (!StringUtils.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            return ResponseResult.error("确认密码错误！");
        }
        if (StringUtils.equals(dto.getOldPassword(), dto.getNewPassword())) {
            return ResponseResult.error("新密码与原密码相同！");
        }
        SysUser sysUser;
        if (!StringUtils.isEmpty(dto.getId())) {
            sysUser = getById(dto.getId());
        } else {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            sysUser = getById(loginUser.getId());
        }
        String password = new Md5Hash(dto.getOldPassword(), sysUser.getSalt(), ShiroConstants.HASH_ITERATORS).toHex();
        if (!StringUtils.equals(password, sysUser.getPassword())) {
            return ResponseResult.error("旧密码错误！");
        }
        // 校验新密码是否使用了历史密码
        String newPassword = new Md5Hash(dto.getNewPassword(), sysUser.getSalt(), ShiroConstants.HASH_ITERATORS).toHex();
        List<String> historyPasswordList = Arrays.asList(sysUser.getHistoryPassword().split(","));
        if (historyPasswordList.contains(newPassword)) {
            return ResponseResult.error("新密码不能使用最近" + historyPasswordNum + "次的密码！");
        }
        // 设置新密码
        sysUser.setPassword(dto.getNewPassword());
        // 密码加密
        passwordEncrypt(sysUser);
        // 更新历史密码
        updateHistoryPassword(historyPasswordList, sysUser);
        // 修改更新密码时间
        sysUser.setPasswordUpdateTime(new Date());
        updateById(sysUser);
        // redis用户修改
        updateRedisUser(sysUser);
        return ResponseResult.success("密码修改成功！");
    }

    @Override
    public ResponseResult updateStatus(String id) {
        SysUser user = getById(id);
        if (user == null) {
            return ResponseResult.error("用户不存在！");
        }
        user.setStatus(!user.getStatus());
        updateById(user);
        return ResponseResult.success("操作成功！");
    }

    @Override
    public ResponseResult checkPassword() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date passwordUpdateTime = loginUser.getPasswordUpdateTime();
        Date afterDate = DateUtil.getAfterDate(passwordUpdateTime, passwordOverdueTime, DateEnum.MONTH.getCode());
        if (new Date().after(afterDate)) {
            return ResponseResult.success("密码已过期", true);
        } else {
            return ResponseResult.success("密码未过期", false);
        }
    }

    @Override
    public ResponseResult checkUserIsEffective(SysUser sysUser, String platformId) {
        if (sysUser == null) {
            return ResponseResult.error("该用户不存在，请注册！");
        }
        if (sysUser.getDelFlag() == null || sysUser.getDelFlag()) {
            return ResponseResult.error("该用户已注销！");
        }
        if (sysUser.getStatus() == null || !sysUser.getStatus()) {
            return ResponseResult.error("该用户已冻结！");
        }
        if (sysUser.getEffectiveTime() != null) {
            if (new Date().after(sysUser.getEffectiveTime())) {
                return ResponseResult.error("该用户已过期！");
            }
        }
        SysPlatform platform = sysPlatformService.getById(platformId);
        if (platform == null) {
            return ResponseResult.error("平台不存在！");
        }
        if (!sysUser.getSuperFlag()) {
            List<String> platformIds = sysRolePlatformService.getPlatformIdsByUserId(sysUser.getId());
            if (!platformIds.contains(platformId)) {
                return ResponseResult.error("无登录权限！");
            }
        }
        if (!platform.getStatus()) {
            return ResponseResult.error("平台已停用！");
        }
        return ResponseResult.success("校验通过");
    }

    public ResponseResult checkUserIsEffectiveNoPlatform(SysUser sysUser, String platformId) {
        if (sysUser == null) {
            return ResponseResult.error("该用户不存在，请注册！");
        }
        if (sysUser.getDelFlag() == null || sysUser.getDelFlag()) {
            return ResponseResult.error("该用户已注销！");
        }
        if (sysUser.getStatus() == null || !sysUser.getStatus()) {
            return ResponseResult.error("该用户已冻结！");
        }
        if (sysUser.getEffectiveTime() != null) {
            if (new Date().after(sysUser.getEffectiveTime())) {
                return ResponseResult.error("该用户已过期！");
            }
        }
        SysPlatform platform = sysPlatformService.getById(platformId);
        if (platform == null) {
            return ResponseResult.error("平台不存在！");
        }
        if (!platform.getStatus()) {
            return ResponseResult.error("平台已停用！");
        }
        return ResponseResult.success("校验通过");
    }

    @Override
    public ResponseResult<?> getLoginUserByNameNoCheck(String tokenStr) {
        // 解密获得username和platformId
        String userName = JwtUtil.getUsername(tokenStr);
        String platformId = JwtUtil.getPlatformId(tokenStr);
        // 以下为同一个平台只允许同一账户单独登录的判断
        String tokenKey = ShiroConstants.PREFIX_USER_TOKEN + tokenStr;
        String infoKey = ShiroConstants.PREFIX_USER_INFO + tokenStr;
        String loginOnlyOne = "login_only_one:" + platformId + "_" + userName;
        if (redisUtil.hasKey(loginOnlyOne)) {
            List<String> existTokenKey = redisUtil.getList(loginOnlyOne, String.class);
            if (existTokenKey.size() > 1) {
                List<String> errorTokenKey = existTokenKey.stream().filter(e -> !StringUtils.equals(tokenKey, e)).collect(Collectors.toList());
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(errorTokenKey)) {
                    errorTokenKey.forEach(e -> redisUtil.delete(e));
                    existTokenKey.removeIf(e -> errorTokenKey.contains(e));
                }
                redisUtil.putListWithExpireTime(loginOnlyOne, existTokenKey, JwtUtil.EXPIRE_TIME);
            }
        } else {
            List<String> tokenKeyList = Arrays.asList(tokenKey);
            redisUtil.putListWithExpireTime(loginOnlyOne, tokenKeyList, JwtUtil.EXPIRE_TIME);
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDelFlag, false);
        queryWrapper.eq(SysUser::getUserName, userName);
        SysUser sysUser = getOne(queryWrapper);
        ResponseResult result = checkUserIsEffectiveNoPlatform(sysUser, platformId);
        if (!result.isSuccess()) {
            return result;
        }
        LoginUser loginUser = getLoginUser(platformId, infoKey, sysUser);
        // JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
        String cacheToken = redisUtil.get(tokenKey, String.class);
        if (!StringUtils.isEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, loginUser.getPassword())) {
                String newAuthorization = JwtUtil.sign(userName, loginUser.getPassword(), platformId);
                // 设置超时时间
                redisUtil.putWithExpireTime(tokenKey, newAuthorization, JwtUtil.EXPIRE_TIME);
                redisUtil.putWithExpireTime(infoKey, loginUser, JwtUtil.EXPIRE_TIME);
                log.debug("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— " + tokenKey);
            }
        } else {
            return ResponseResult.error(ShiroConstants.TOKEN_IS_INVALID_MSG);
        }
        return ResponseResult.success(loginUser);
    }

    @Override
    public LoginUser getLoginUser(SysUser sysUser, String platformId) {
        // 隐私数据SM2解密
        sm2Decrypt(sysUser);
        LoginUser loginUser = sysUserConverter.sysToLogin(sysUser);
        // 查询平台
        SysPlatform sysPlatform = sysPlatformService.getById(platformId);
        loginUser.setPlatformId(platformId);
        loginUser.setPlatformName(sysPlatform.getSystemName());
        // 查询用户所在部门
        List<SysDepart> depart = sysDepartService.getDepartByUserId(loginUser.getId(), 0);
        if (!CollectionUtils.isEmpty(depart)) {
            loginUser.setDepartId(depart.get(0).getId());
            loginUser.setDepartName(depart.get(0).getDepartName());
            // 设置用户最近一级区域，以及子区域
            if (ObjectUtil.isNotEmpty(loginUser.getDepartId())) {
                sysDepartService.setRegion(depart.get(0), loginUser);
            }
        }
        // 查询用户兼职部门
        List<SysDepart> partTimeDeparts = sysDepartService.getDepartByUserId(loginUser.getId(), 1);
        if (!CollectionUtils.isEmpty(partTimeDeparts)) {
            List<PartTimeDepartVo> partTimeDepartVos = sysDepartConverter.sysDepartDaoToPartTimeDepartsVo(partTimeDeparts);
            loginUser.setPartTimeDeparts(partTimeDepartVos);
        }
        // 查询用户角色
        List<SysRole> roles = sysRoleService.getRoleByUserId(loginUser.getId());
        List<LoginRoleVo> roleList = sysRoleConverter.sysRoleDaoToLoginVo(roles);
        loginUser.setRoleList(roleList);
        // 查询用户平台权限
        List<String> platformIds = sysRolePlatformService.getPlatformIdsByUserId(sysUser.getId());
        loginUser.setPlatformIds(platformIds);
        // 查询用户菜单权限
        List<String> roleIds = roleList.stream().map(LoginRoleVo::getId).collect(Collectors.toList());
        List<LoginMenu> loginMenuList = sysMenuService.getMenuListByRoleIds(roleIds, platformId);
        if (!CollectionUtils.isEmpty(loginMenuList)) {
            // 查询功能点
            List<LoginFunction> loginFunctions = sysFunctionService.getFunctionListByRoleIds(roleIds);
            loginUser.setFunList(loginFunctions.stream().map(LoginFunction::getFunctionTag).collect(Collectors.toSet()));
            Map<String, List<LoginFunction>> loginFunctionMap = loginFunctions.stream().collect(Collectors.groupingBy(LoginFunction::getMenuId));
            // 查询数据集
            List<LoginDataSet> loginDataSetList = sysDataSetService.getDataSetByRoleIds(roleIds);
            Set<String> contentIds = loginDataSetList.stream().map(LoginDataSet::getContentId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(contentIds)) {
                List<SysDict> sysDicts = sysDictService.listByIds(contentIds);
                Map<String, SysDict> sysDictMap = sysDicts.stream().collect(Collectors.toMap(SysDict::getId, Function.identity()));
                for (LoginDataSet loginDataSet : loginDataSetList) {
                    if (!sysDictMap.containsKey(loginDataSet.getContentId())) continue;
                    loginDataSet.setKey(sysDictMap.get(loginDataSet.getContentId()).getKey());
                    loginDataSet.setLabel(sysDictMap.get(loginDataSet.getContentId()).getLabel());
                    loginDataSet.setValue(sysDictMap.get(loginDataSet.getContentId()).getValue());
                }
            }
            List<LoginDataSetIndex> loginDataSetIndexList = sysDataSetIndexService.getDataSetIndexByRoleIds(roleIds);
            Map<String, List<LoginDataSetIndex>> loginDataSetIndexMap = loginDataSetIndexList.stream().collect(Collectors.groupingBy(LoginDataSetIndex::getDataSetId));
            for (LoginDataSet loginDataSet : loginDataSetList) {
                if (loginDataSetIndexMap.containsKey(loginDataSet.getId())) {
                    List<LoginDataSetIndex> indexList = loginDataSetIndexMap.get(loginDataSet.getId());
                    Map<String, List<LoginDataSetIndex>> indexMap = indexList.stream().collect(Collectors.groupingBy(LoginDataSetIndex::getParentId));
                    List<LoginDataSetIndex> treeMenuList = indexList.stream().filter(e -> StringUtils.equals(loginDataSet.getContentId(), "0") ? StringUtils.equals(e.getParentId(), "0") : StringUtils.equals(e.getParentId(), loginDataSet.getContentId())).collect(Collectors.toList());
                    getTreeIndexList(indexMap, treeMenuList, StringUtils.equals(loginDataSet.getContentId(), "0") ? false : true);
                    loginDataSet.setIndexList(treeMenuList);
                }
            }
            Map<String, List<LoginDataSet>> loginDataSetMap = loginDataSetList.stream().collect(Collectors.groupingBy(LoginDataSet::getMenuId));
            for (LoginMenu loginMenu : loginMenuList) {
                loginMenu.setFunctionList(loginFunctionMap.containsKey(loginMenu.getId()) ? loginFunctionMap.get(loginMenu.getId()) : null);
                loginMenu.setDataSetList(loginDataSetMap.containsKey(loginMenu.getId()) ? loginDataSetMap.get(loginMenu.getId()) : null);
            }
        }
        Map<String, List<LoginMenu>> allMap = loginMenuList.stream().collect(Collectors.groupingBy(LoginMenu::getParentId));
        List<LoginMenu> menuList = loginMenuList.stream().filter(e -> StringUtils.equals(e.getParentId(), "0")).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(menuList)) {
            getTreeMenuList(allMap, menuList);
        }
        loginUser.setMenuList(menuList);
        return loginUser;
    }

    @Override
    public ResponseResult getLoginUserByName(String tokenStr) {
        // 解密获得username和platformId
        String userName = JwtUtil.getUsername(tokenStr);
        String platformId = JwtUtil.getPlatformId(tokenStr);
        // 以下为同一个平台只允许同一账户单独登录的判断
        String tokenKey = ShiroConstants.PREFIX_USER_TOKEN + tokenStr;
        String infoKey = ShiroConstants.PREFIX_USER_INFO + tokenStr;
        String loginOnlyOne = "login_only_one:" + platformId + "_" + userName;
        if (redisUtil.hasKey(loginOnlyOne)) {
            List<String> existTokenKey = redisUtil.getList(loginOnlyOne, String.class);
            if (existTokenKey.size() > 1) {
                List<String> errorTokenKey = existTokenKey.stream().filter(e -> !StringUtils.equals(tokenKey, e)).collect(Collectors.toList());
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(errorTokenKey)) {
                    errorTokenKey.forEach(e -> redisUtil.delete(e));
                    existTokenKey.removeIf(e -> errorTokenKey.contains(e));
                }
                redisUtil.putListWithExpireTime(loginOnlyOne, existTokenKey, JwtUtil.EXPIRE_TIME);
            }
        } else {
            List<String> tokenKeyList = Arrays.asList(tokenKey);
            redisUtil.putListWithExpireTime(loginOnlyOne, tokenKeyList, JwtUtil.EXPIRE_TIME);
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDelFlag, false);
        queryWrapper.eq(SysUser::getUserName, userName);
        SysUser sysUser = getOne(queryWrapper);
        ResponseResult result = checkUserIsEffective(sysUser, platformId);
        if (!result.isSuccess()) {
            return result;
        }
        LoginUser loginUser = getLoginUser(platformId, infoKey, sysUser);
        // JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
        String cacheToken = redisUtil.get(tokenKey, String.class);
        if (!StringUtils.isEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, loginUser.getPassword())) {
                String newAuthorization = JwtUtil.sign(userName, loginUser.getPassword(), platformId);
                // 设置超时时间
                redisUtil.putWithExpireTime(tokenKey, newAuthorization, JwtUtil.EXPIRE_TIME);
                redisUtil.putWithExpireTime(infoKey, loginUser, JwtUtil.EXPIRE_TIME);
                log.debug("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— " + tokenKey);
            }
        } else {
            return ResponseResult.error(ShiroConstants.TOKEN_IS_INVALID_MSG);
        }
        return ResponseResult.success(loginUser);
    }

    @Nullable
    private LoginUser getLoginUser(String platformId, String infoKey, SysUser sysUser) {
        LoginUser loginUser = redisUtil.get(infoKey, LoginUser.class);
        if (loginUser == null){
            loginUser = getLoginUser(sysUser, platformId);
            redisUtil.putWithExpireTime(infoKey, loginUser, JwtUtil.EXPIRE_TIME);
        }
        return loginUser;
    }

    @Override
    @Transactional
    public ResponseResult userEdit(UserEditDto dto) throws Exception {
        if (StringUtils.isEmpty(dto.getRealName())) {
            return ResponseResult.error("用户姓名不能为空！");
        }
        if (StringUtils.isEmpty(dto.getContact())) {
            return ResponseResult.error("联系电话不能为空！");
        }

        SysUser sysUser = getById(dto.getId());
        sysUser.setRealName(dto.getRealName());
        sysUser.setContact(dto.getContact());
        sysUser.setPosition(dto.getPosition());
        sysUser.setHeadPicture(dto.getHeadPicture());
        // 隐私数据SM2加密
        sm2Encrypt(sysUser);
        // 修改用户
        updateById(sysUser);
        // redis用户修改
        updateRedisUser(sysUser);
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult editPassword(EditPasswordDto dto) {
        if (StringUtils.isEmpty(dto.getContact())) {
            return ResponseResult.error("手机号不能为空！");
        }
        if (StringUtils.isEmpty(dto.getSmsCode())) {
            return ResponseResult.error("验证码为空！");
        }
        String cacheCaptcha = redisUtil.get(dto.getContact() + "2", String.class);
        if (!StringUtils.equals(cacheCaptcha, dto.getSmsCode())) {
            return ResponseResult.error("验证码错误！");
        }
        if (StringUtils.isEmpty(dto.getPassword())) {
            return ResponseResult.error("密码不能为空！");
        }
        SysUser sysUser = getById(dto.getId());
        // 校验新密码是否使用了历史密码
        String newPassword = new Md5Hash(dto.getPassword(), sysUser.getSalt(), ShiroConstants.HASH_ITERATORS).toHex();
        List<String> historyPasswordList = Arrays.asList(sysUser.getHistoryPassword().split(","));
        if (historyPasswordList.contains(newPassword)) {
            return ResponseResult.error("新密码不能使用最近" + historyPasswordNum + "次的密码！");
        }
        // 设置新密码
        sysUser.setPassword(dto.getPassword());
        // 密码加密
        passwordEncrypt(sysUser);
        // 更新历史密码
        updateHistoryPassword(historyPasswordList, sysUser);
        // 修改更新密码时间
        sysUser.setPasswordUpdateTime(new Date());
        updateById(sysUser);
        // redis用户修改
        updateRedisUser(sysUser);
        return ResponseResult.success("密码修改成功！");
    }

    public static void main(String[] args) {
//        String newPassword = new Md5Hash("cf0e3f4e8c67cd1db5e2e8dc11e0bb20", "*@bato%w", ShiroConstants.HASH_ITERATORS).toHex();
//        System.out.println(newPassword);

        String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQg7cxadrQG5/1c+tw2QmKqHhzunwEuj7Wd+qWoYsjFTwqgCgYIKoEcz1UBgi2hRANCAAStEOTy1Tx/1sNA5FNS+xTpW3OB3g32gDyxVRPUnnvxKG9Sn40urOkXWdD5zJpEnQ41K/3gGO2RxOS2KU6ShWMc";
        String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAErRDk8tU8f9bDQORTUvsU6Vtzgd4N9oA8sVUT1J578ShvUp+NLqzpF1nQ+cyaRJ0ONSv94BjtkcTktilOkoVjHA==";
        PublicKey publicKey1 = KeyUtils.createPublicKey(publicKey);
        String realName = Base64.getEncoder().encodeToString("123456".getBytes());
        System.out.println(realName);
    }

    /**
     * 获取指标树结构
     *
     * @param allMap
     * @param treeMenuList
     * @param isDict
     */
    public void getTreeIndexList(Map<String, List<LoginDataSetIndex>> allMap, List<LoginDataSetIndex> treeMenuList, boolean isDict) {
        for (LoginDataSetIndex loginDataSetIndex : treeMenuList) {
            String childrenId = isDict ? loginDataSetIndex.getIndexId() : loginDataSetIndex.getId();
            if (allMap.containsKey(childrenId)) {
                List<LoginDataSetIndex> children = allMap.get(childrenId);
                loginDataSetIndex.setChildren(children);
                getTreeIndexList(allMap, children, isDict);
            }
        }
    }

    /**
     * 获取菜单树结构
     *
     * @param allMap
     * @param treeMenuList
     */
    public void getTreeMenuList(Map<String, List<LoginMenu>> allMap, List<LoginMenu> treeMenuList) {
        for (LoginMenu loginMenu : treeMenuList) {
            String id = loginMenu.getId();
            if (allMap.containsKey(id)) {
                List<LoginMenu> children = allMap.get(id);
                loginMenu.setChildren(children);
                getTreeMenuList(allMap, children);
            }
        }
    }

    /**
     * 新增修改校验
     *
     * @param dto
     * @param isAdd
     * @return
     */
    public ResponseResult check(SysUserDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getUserName())) {
            return ResponseResult.error("登录账号不能为空！");
        }
        if (StringUtils.isEmpty(dto.getRealName())) {
            return ResponseResult.error("用户姓名不能为空！");
        }
        if (StringUtils.isEmpty(dto.getContact())) {
            return ResponseResult.error("联系电话不能为空！");
        }
        if (StringUtils.isEmpty(dto.getPosition())) {
            return ResponseResult.error("职务不能为空！");
        }
        if (dto.getEffectiveTime() == null) {
            return ResponseResult.error("账号有效期不能为空！");
        }
        if (dto.getStatus() == null) {
            return ResponseResult.error("账号状态不能为空！");
        }
        if (StringUtils.isEmpty(dto.getDepartId())) {
            return ResponseResult.error("所属部门不能为空！");
        } else {
            LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<>();
            departQuery.eq(SysDepart::getDelFlag, false);
            departQuery.eq(SysDepart::getId, dto.getDepartId());
            SysDepart depart = sysDepartService.getOne(departQuery);
            if (depart == null) {
                return ResponseResult.error("部门不存在！");
            }
        }
        if (!CollectionUtils.isEmpty(dto.getPartTimeDepartIds())) {
            if (dto.getPartTimeDepartIds().contains(dto.getDepartId())) {
                return ResponseResult.error("兼职部门不能与所属部门重复！");
            }
            LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<>();
            departQuery.eq(SysDepart::getDelFlag, false);
            departQuery.in(SysDepart::getId, dto.getPartTimeDepartIds());
            List<SysDepart> partTimeDepartList = sysDepartService.list(departQuery);
            if (CollectionUtils.isEmpty(partTimeDepartList) || partTimeDepartList.size() != dto.getPartTimeDepartIds().size()) {
                return ResponseResult.error("兼职部门不存在！");
            }
        }
        if (CollectionUtils.isEmpty(dto.getRoleIds())) {
            return ResponseResult.error("功能角色不能为空！");
        } else {
            LambdaQueryWrapper<SysRole> roleQuery = new LambdaQueryWrapper<>();
            roleQuery.eq(SysRole::getDelFlag, false);
            roleQuery.in(SysRole::getId, dto.getRoleIds());
            List<SysRole> roleList = sysRoleService.list(roleQuery);
            if (CollectionUtils.isEmpty(roleList) || roleList.size() != dto.getRoleIds().size()) {
                return ResponseResult.error("角色不存在！");
            }
        }
        LambdaQueryWrapper<SysUser> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(SysUser::getDelFlag, false);
        userQuery.eq(SysUser::getUserName, dto.getUserName());
        SysUser user = getOne(userQuery);
        if (isAdd) {
            if (user != null) {
                return ResponseResult.error("用户已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空！");
            }
            SysUser sysUser = getById(dto.getId());
            if (sysUser == null) {
                return ResponseResult.error("用户不存在！");
            } else {
                if (user != null && !StringUtils.equals(sysUser.getUserName(), user.getUserName())) {
                    return ResponseResult.error("用户已存在！");
                }
            }
        }
        return ResponseResult.success("校验成功！");
    }

    /**
     * 用户密码加密
     *
     * @param sysUser
     */
    public void passwordEncrypt(SysUser sysUser) {
        // 设置密码盐
        String salt = StringUtils.isEmpty(sysUser.getSalt()) ? SaltUtil.getSalt(ShiroConstants.SALT_LENGTH) : sysUser.getSalt();
        // 密码加密
        String password = new Md5Hash(sysUser.getPassword(), salt, ShiroConstants.HASH_ITERATORS).toHex();
        sysUser.setSalt(salt);
        sysUser.setPassword(password);
        // 设置性别
        if (sysUser.getSex() == null) {
            sysUser.setSex(0);
        }
    }

    /**
     * 隐私数据SM2加密
     *
     * @param sysUser
     */
    public void sm2Encrypt(SysUser sysUser) throws Exception {
        // 公钥
        PublicKey publicKey = null;
        if (StringUtils.isEmpty(sysUser.getPublicKey()) && StringUtils.isEmpty(sysUser.getPrivateKey())) {
            // 生成公私钥对
            String[] keys = KeyUtils.generateSmKey();
            sysUser.setPublicKey(keys[0]);
            sysUser.setPrivateKey(keys[1]);
            publicKey = KeyUtils.createPublicKey(keys[0]);
        } else {
            publicKey = KeyUtils.createPublicKey(sysUser.getPublicKey());
        }
        // 加密
        if (!StringUtils.isEmpty(sysUser.getRealName())) {
            String realName = Base64.getEncoder().encodeToString(Sm2Util.encrypt(sysUser.getRealName().getBytes(), publicKey));
            sysUser.setRealName(realName);
        }
        if (!StringUtils.isEmpty(sysUser.getContact())) {
            String contact = Base64.getEncoder().encodeToString(Sm2Util.encrypt(sysUser.getContact().getBytes(), publicKey));
            sysUser.setContact(contact);
        }
        if (!StringUtils.isEmpty(sysUser.getEmail())) {
            String email = Base64.getEncoder().encodeToString(Sm2Util.encrypt(sysUser.getEmail().getBytes(), publicKey));
            sysUser.setEmail(email);
        }
        if (!StringUtils.isEmpty(sysUser.getPosition())) {
            String position = Base64.getEncoder().encodeToString(Sm2Util.encrypt(sysUser.getPosition().getBytes(), publicKey));
            sysUser.setPosition(position);
        }
    }

    /**
     * 隐私数据SM2解密
     *
     * @param sysUser
     */
    public void sm2Decrypt(SysUser sysUser) {
        PrivateKey privateKey = KeyUtils.createPrivateKey(sysUser.getPrivateKey());
        if (!StringUtils.isEmpty(sysUser.getRealName())) {
            String realName = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getRealName()), privateKey));
            sysUser.setRealName(realName);
        }
        if (!StringUtils.isEmpty(sysUser.getContact())) {
            String contact = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getContact()), privateKey));
            sysUser.setContact(contact);
        }
        if (!StringUtils.isEmpty(sysUser.getEmail())) {
            String email = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getEmail()), privateKey));
            sysUser.setEmail(email);
        }
        if (!StringUtils.isEmpty(sysUser.getPosition())) {
            String position = new String(Sm2Util.decrypt(Base64.getDecoder().decode(sysUser.getPosition()), privateKey));
            sysUser.setPosition(position);
        }
    }

    /**
     * 更新历史密码
     *
     * @param historyPasswordList
     * @param sysUser
     */
    public void updateHistoryPassword(List<String> historyPasswordList, SysUser sysUser) {
        if (historyPasswordNum != null || historyPasswordNum != 0) {
            if (historyPasswordList.size() >= historyPasswordNum) {
                List<String> historyList = new ArrayList<>();
                for (Integer i = 1; i < historyPasswordNum; i++) {
                    historyList.add(historyPasswordList.get(i));
                }
                sysUser.setHistoryPassword(String.join(",", historyList) + "," + sysUser.getPassword());
            } else {
                sysUser.setHistoryPassword(sysUser.getHistoryPassword() + "," + sysUser.getPassword());
            }
        }
    }

    /**
     * Redis用户更新
     *
     * @param sysUser
     */
    public void updateRedisUser(SysUser sysUser) {
        Map allUserMap = redisUtil.get("_allUserMap", Map.class);
        // 隐私数据SM2解密
        sm2Decrypt(sysUser);
        allUserMap.put(sysUser.getId(), sysUser);
        redisUtil.put("_allUserMap", allUserMap);
    }

    @Override
    public List<SysUser> getUserByIds(String ids) {
//        LambdaQueryWrapper<SysUser> allQuery = new LambdaQueryWrapper<>();
//        allQuery.in(SysUser::getId, ids.split(","));
//        allQuery.eq(SysUser::getDelFlag, false);
//        List<SysUser> allUserList = this.list(allQuery);
//        for (SysUser user : allUserList) {
//            PrivateKey privateKey = KeyUtils.createPrivateKey(user.getPrivateKey());
//            if (!StringUtils.isEmpty(user.getRealName())) {
//                user.setRealName(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getRealName()), privateKey)));
//            }
//            if (!StringUtils.isEmpty(user.getContact())) {
//                user.setContact(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getContact()), privateKey)));
//            }
//            if (!StringUtils.isEmpty(user.getEmail())) {
//                user.setEmail(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getEmail()), privateKey)));
//            }
//            if (!StringUtils.isEmpty(user.getPosition())) {
//                user.setPosition(new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getPosition()), privateKey)));
//            }
//        }
//        return allUserList;
        List<SysUser> users = new ArrayList<>();
        Map allUserMap = redisUtil.get("_allUserMap", Map.class);
        String[] split = ids.split(",");
        for(String id : split) {
            Object o = allUserMap.get(id);
            if(o != null) {
                SysUser sysUser = JSONObject.parseObject(o.toString(), SysUser.class);
                users.add(sysUser);
            }

        }
        return users;
    }
}
