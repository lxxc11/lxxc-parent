package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.domain.vo.SysDepartUserVo;
import com.lvxc.user.entity.SysDepart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户id查询部门
     *
     * @param userId
     * @return
     */
    List<SysDepart> getDepartByUserId(@Param("userId") String userId,
                                      @Param("type") Integer type);


    /**
     * 根据部门查询用户
     *
     * @param list
     * @return
     */
    List<SysDepartUserVo> getUserByDepartIds(@Param("list") List<String> list);

}
