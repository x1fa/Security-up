package com.doter.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doter.common.datasource.entity.user.DtsRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *
 */
@Mapper
public interface DtsRoleMapper extends BaseMapper<DtsRole> {

    List<DtsRole> selectListByUserId( @Param("userId") String userId);

}




