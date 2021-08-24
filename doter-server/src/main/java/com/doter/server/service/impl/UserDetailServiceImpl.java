package com.doter.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doter.common.core.magic.UserMagic;
import com.doter.common.tools.UserTool;
import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.datasource.entity.user.DtaUser;
import com.doter.common.datasource.entity.user.DtsRole;
import com.doter.common.datasource.entity.user.DtsUser;
import com.doter.server.mapper.DtaUserMapper;
import com.doter.server.mapper.DtsMenuMapper;
import com.doter.server.mapper.DtsRoleMapper;
import com.doter.server.mapper.DtsUserMapper;
import com.doter.server.service.UserDetailService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Description TODO
 * @Author HanTP
 * @Date 2021/7/12 10:03
 */
@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Resource
    private DtaUserMapper dtaUserMapper;

    @Resource
    private DtsUserMapper dtsUserMapper;

    @Resource
    private DtsRoleMapper dtsRoleMapper;

    @Resource
    private DtsMenuMapper dtsMenuMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private HttpServletRequest httpServletRequest;

    private final String USER_TYPE = "type";

    private final String USER_MEMBER_ROLE = "ROLE_MEMBER";

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String userType = httpServletRequest.getParameter(USER_TYPE);
        //是不是 admin
        if (UserMagic.USER_TYPE_ADMIN.equals(userType)){
            //是 DataTableSystem
            return dtsUser(s);
        }
        //不是
        return dtaUser(s);
    }

    @Override
    public UserDetails loadUserByPhone(String phone) {
        String userType = httpServletRequest.getParameter(USER_TYPE);
        //是不是 member
        if (UserMagic.USER_TYPE_MEMBER.equals(userType)){
            //查询 member信息 DataTableApplication
            UserDetail userDetail = dtaUser(phone);
            //判断 是否为空
            if (userDetail==null){
                //空，创建新用户
                DtaUser dtaUser = new DtaUser();
                dtaUser.setUserName(UserTool.createUserName()).setPassWord(passwordEncoder.encode(phone))
                        .setPhone(phone).setNickName(UserTool.createNickName())
                        .setStatus((byte) 1);
                //放入数据库
                dtaUserMapper.insert(dtaUser);
                return dtaUser(phone);
            }
            return userDetail;
        }
        return dtsUser(phone);
    }


    private UserDetail dtaUser(String s){
        QueryWrapper<DtaUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DtaUser::getUserName,s)
                .or().eq(DtaUser::getPhone,s);
        DtaUser dtaUser =  dtaUserMapper.selectOne(queryWrapper);
        if (dtaUser==null){
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_MEMBER_ROLE));
        if(StrUtil.isNotBlank(dtaUser.getRoles())) {
            authorities.add(new SimpleGrantedAuthority(dtaUser.getRoles()));
        }
        return new UserDetail(dtaUser.getId(),dtaUser.getUserName(),dtaUser.getPhone(),
                dtaUser.getPassWord(),dtaUser.getStatus(),authorities);
    }

    private UserDetail dtsUser(String s){
        QueryWrapper<DtsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DtsUser::getUserName,s)
                .or().eq(DtsUser::getPhone,s);
        DtsUser dtsUser =  dtsUserMapper.selectOne(queryWrapper);
        if (dtsUser==null){
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<DtsRole> dtsRoles = dtsRoleMapper.selectListByUserId(dtsUser.getId().toString());
        List<String> roleIds = new ArrayList<>();
        dtsRoles.forEach(dtsRole -> {
            authorities.add(new SimpleGrantedAuthority(dtsRole.getRoleScope()));
            roleIds.add(String.valueOf(dtsRole.getId()));
        });
        List<String> auths = dtsMenuMapper.selectAuthByRoles(roleIds);
        auths.forEach(auth ->{
            if (StrUtil.isNotBlank(auth)){
                authorities.add(new SimpleGrantedAuthority(auth));
            }
        });
        return new UserDetail(dtsUser.getId(),dtsUser.getUserName(),dtsUser.getPhone(),
                dtsUser.getPassWord(),dtsUser.getStatus(),authorities);
    }
}
