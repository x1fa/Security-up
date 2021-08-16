package com.doter.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doter.common.datasource.entity.user.DtsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表(DtsMenu)表数据库访问层
 *
 * @author HanTP
 * @since 2021-03-20 10:05:20
 */
@Mapper
public interface DtsMenuMapper extends BaseMapper<DtsMenu> {

    Set<DtsMenu> selectByRoles(@Param("roleIds") List<String> roleIds);

    List<String> selectAuthByRoles(@Param("roleIds") List<String> roleIds);
}