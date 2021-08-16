package com.doter.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.doter.common.datasource.entity.user.DtsUser;
import com.doter.common.datasource.param.DtsUserParam;

/**
 *
 */
public interface DtsUserService extends IService<DtsUser> {

    boolean updatePassword(Long userId,String password);

    DtsUserParam selectByUserId(String userId);

}
