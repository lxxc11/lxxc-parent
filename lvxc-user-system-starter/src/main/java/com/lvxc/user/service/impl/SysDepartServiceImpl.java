package com.lvxc.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.KeyUtils;
import com.lvxc.user.common.RedisUtil;
import com.lvxc.user.common.Sm2Util;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysDepartConverter;
import com.lvxc.user.domain.dto.SysDepartDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysDepartUserVo;
import com.lvxc.user.domain.vo.SysDepartVo;
import com.lvxc.user.entity.Entity;
import com.lvxc.user.entity.SysDepart;
import com.lvxc.user.entity.SysUser;
import com.lvxc.user.entity.SysUserDepart;
import com.lvxc.user.mapper.flw.SysDepartMapper;
import com.lvxc.user.service.ISysDepartService;
import com.lvxc.user.service.ISysUserDepartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

    @Autowired
    private SysDepartConverter sysDepartConverter;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResponseResult getList(String keyWord) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, false);
        if (!StringUtils.isEmpty(keyWord)) {
            query.like(SysDepart::getDepartName, keyWord);
        }
        query.orderByDesc(SysDepart::getCreateTime);
        List<SysDepart> allList = list(query);
        List<SysDepartVo> list = sysDepartConverter.SysDepartListDaoToVo(allList);
        Map allUserMap = redisUtil.get("_allUserMap", Map.class);
        for (SysDepartVo sysDepartVo : list) {
            if (!StringUtils.isEmpty(sysDepartVo.getCreateBy()) && allUserMap.containsKey(sysDepartVo.getCreateBy())) {
                SysUser sysUser = JSONObject.parseObject(allUserMap.get(sysDepartVo.getCreateBy()).toString(), SysUser.class);
                sysDepartVo.setCreateName(sysUser.getRealName());
            }
        }
        Map<String, List<SysDepartVo>> allMap = list.stream().collect(Collectors.groupingBy(SysDepartVo::getParentId));
        List<SysDepartVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), "0")).collect(Collectors.toList());
        getTreeDepartList(allMap, treeMenuList);
        if (CollectionUtils.isEmpty(treeMenuList)) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.success(treeMenuList);
        }
    }

    /**
     * 获取部门树结构
     *
     * @param allMap
     * @param treeMenuList
     */
    public void getTreeDepartList(Map<String, List<SysDepartVo>> allMap, List<SysDepartVo> treeMenuList) {
        for (SysDepartVo sysDepartVo : treeMenuList) {
            String id = sysDepartVo.getId();
            if (allMap.containsKey(id)) {
                List<SysDepartVo> children = allMap.get(id);
                sysDepartVo.setChildren(children);
                getTreeDepartList(allMap, children);
            }
        }
    }

    @Override
    @Transactional
    public ResponseResult add(SysDepartDto dto) {
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        dto.setParentId(!StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        SysDepart sysDepart = sysDepartConverter.sysDepartDtoToDao(dto);
        save(sysDepart);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysDepartDto dto) {
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysDepart sysDepart = getById(dto.getId());
        sysDepart.setParentId(!StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        sysDepart.setDepartName(dto.getDepartName());
        sysDepart.setType(dto.getType());
        sysDepart.setDistrictLevel(dto.getDistrictLevel());
        updateById(sysDepart);
        return ResponseResult.success("编辑成功！");
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<>();
        departQuery.eq(SysDepart::getDelFlag, false);
        departQuery.eq(SysDepart::getParentId, id);
        List<SysDepart> children = list(departQuery);
        if (!CollectionUtils.isEmpty(children)) {
            return ResponseResult.error("部门下存在子部门，不允许删除！");
        }
        LambdaQueryWrapper<SysUserDepart> userDepartQuery = new LambdaQueryWrapper<>();
        userDepartQuery.eq(SysUserDepart::getDelFlag, false);
        userDepartQuery.eq(SysUserDepart::getDepartId, id);
        List<SysUserDepart> userDepartList = sysUserDepartService.list(userDepartQuery);
        if (!CollectionUtils.isEmpty(userDepartList)) {
            return ResponseResult.error("部门下存在用户，不允许删除！");
        }
        SysDepart sysDepart = getById(id);
        sysDepart.setDelFlag(true);
        updateById(sysDepart);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public List<SysDepart> getDepartByUserId(String userId, Integer type) {
        return baseMapper.getDepartByUserId(userId, type);
    }

    @Override
    public ResponseResult getDepartUser() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, false);
        query.orderByDesc(SysDepart::getCreateTime);
        List<SysDepart> allList = list(query);
        List<SysDepartVo> list = sysDepartConverter.SysDepartListDaoToVo(allList);
        List<String> departIds = list.stream().map(SysDepartVo::getId).collect(Collectors.toList());
        List<SysDepartUserVo> userList = baseMapper.getUserByDepartIds(departIds);
        for (SysDepartUserVo userVo : userList) {
            PrivateKey privateKey = KeyUtils.createPrivateKey(userVo.getPrivateKey());
            if (!StringUtils.isEmpty(userVo.getRealName())) {
                String realName = new String(Sm2Util.decrypt(Base64.getDecoder().decode(userVo.getRealName()), privateKey));
                userVo.setRealName(realName);
            }
        }
        Map<String, List<SysDepartUserVo>> userMap = userList.stream().collect(Collectors.groupingBy(SysDepartUserVo::getDepartId));
        list.forEach(e -> e.setUserList(userMap.containsKey(e.getId()) ? userMap.get(e.getId()) : new ArrayList<>()));
        Map<String, List<SysDepartVo>> allMap = list.stream().collect(Collectors.groupingBy(SysDepartVo::getParentId));
        List<SysDepartVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), "0")).collect(Collectors.toList());
        getTreeDepartList(allMap, treeMenuList);
        if (CollectionUtils.isEmpty(treeMenuList)) {
            return ResponseResult.success(list);
        } else {
            return ResponseResult.success(treeMenuList);
        }
    }

    @Override
    public void setRegion(SysDepart sysDepart, LoginUser loginUser) {
        List<SysDepart> list = lambdaQuery()
                .eq(SysDepart::getDelFlag, false)
                .list();
        Map<String, SysDepart> idMap = list.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
        Map<String, List<SysDepart>> parentIdMap = list.stream().collect(Collectors.groupingBy(SysDepart::getParentId));
        while (true) {
            if (ObjectUtil.isNotEmpty(idMap.get(sysDepart.getParentId()))) {
                sysDepart = idMap.get(sysDepart.getParentId());
                if (sysDepart.getType().intValue() == 1) {
                    loginUser.setRegion(sysDepart);
                    break;
                }
            } else {
                break;
            }
        }
        List<SysDepart> childRegion = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(parentIdMap.get(sysDepart.getId()))) {
            setChildRegion(sysDepart, parentIdMap, childRegion);
        }
        loginUser.setChildRegion(childRegion);

    }

    private static void setChildRegion(SysDepart sysDepart, Map<String, List<SysDepart>> parentIdMap, List<SysDepart> childRegion) {
        if (ObjectUtil.isNotEmpty(parentIdMap.get(sysDepart.getId()))) {
            List<SysDepart> sysDeparts = parentIdMap.get(sysDepart.getId());
            for (SysDepart depart : sysDeparts) {
                if (depart.getType() == 1) {
                    childRegion.add(depart);
                }
                setChildRegion(depart, parentIdMap, childRegion);
            }
        }
    }

    public ResponseResult check(SysDepartDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getDepartName())) {
            return ResponseResult.error("部门名称不能为空！");
        }
        if (dto.getType() == null) {
            return ResponseResult.error("类型不能为空！");
        }
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDelFlag, false);
        query.eq(SysDepart::getDepartName, dto.getDepartName());
        query.eq(SysDepart::getParentId, !StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        SysDepart depart = getOne(query);
        if (isAdd) {
            if (depart != null) {
                return ResponseResult.error("部门已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空！");
            }
            SysDepart sysDepart = getById(dto.getId());
            if (sysDepart == null) {
                return ResponseResult.error("部门不存在！");
            } else {
                if (depart != null && !StringUtils.equals(sysDepart.getDepartName(), depart.getDepartName())) {
                    return ResponseResult.error("部门已存在！");
                }
            }
        }
        return ResponseResult.success("检验成功！");
    }

}
