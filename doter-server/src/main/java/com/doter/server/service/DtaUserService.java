package com.doter.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.doter.common.datasource.entity.user.DtaUser;

/**
 *
 */
public interface DtaUserService extends IService<DtaUser> {

    /**
     * @Description TDD
     * @Author HTP-韩天鹏
     * @Date 2021/8/7 9:46
     * @param userId
     * @param password
     * @return boolean
     **/
    boolean updatePassword(Long userId, String password);

}
