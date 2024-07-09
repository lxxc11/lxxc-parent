package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.domain.vo.LoginMenu;
import com.lvxc.user.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 校验菜单
     *
     * @param userId
     * @param platformId
     * @param menuName
     * @return
     */
    SysMenu checkMenu(@Param("userId") String userId, @Param("platformId") String platformId, @Param("menuName") String menuName);

    /**
     * 根据用户角色获取菜单权限
     *
     * @param roleIds
     * @return
     */
    List<LoginMenu> getMenuListByRoleIds(@Param("list") List<String> roleIds, @Param("platformId") String platformId);


}
