package com.doter.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doter.common.tools.AESTool;
import com.doter.common.datasource.entity.user.DtaUser;
import com.doter.server.mapper.DtaUserMapper;
import com.doter.server.service.DtaUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
  *@CLassName DtaUserServiceImpl.java
  *@Description TDD
  *@Author HTP-韩天鹏
  *@Date 2021/8/7 9:54
  *@Version 1.0
  **/
@Service
public class DtaUserServiceImpl extends ServiceImpl<DtaUserMapper, DtaUser>
implements DtaUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean updatePassword(Long userId, String password) {
        UpdateWrapper<DtaUser> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DtaUser::getId,userId)
                .set(DtaUser::getPassWord, passwordEncoder.encode(AESTool.decrypt(password)));
        return super.update(wrapper);
    }



}




