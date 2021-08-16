package com.doter.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doter.common.tools.AESTool;
import com.doter.common.datasource.entity.user.DtsMenu;
import com.doter.common.datasource.entity.user.DtsRole;
import com.doter.common.datasource.entity.user.DtsUser;
import com.doter.common.datasource.param.DtsMenuParam;
import com.doter.common.datasource.param.DtsUserParam;
import com.doter.server.mapper.DtsMenuMapper;
import com.doter.server.mapper.DtsRoleMapper;
import com.doter.server.mapper.DtsUserMapper;
import com.doter.server.service.DtsUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Service
public class DtsUserServiceImpl extends ServiceImpl<DtsUserMapper, DtsUser>
        implements DtsUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private DtsRoleMapper dtsRoleMapper;

    @Resource
    private DtsMenuMapper dtsMenuMapper;

    @Override
    public boolean updatePassword(Long userId, String password) {
        UpdateWrapper<DtsUser> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DtsUser::getId, userId)
                .set(DtsUser::getPassWord, passwordEncoder.encode(AESTool.decrypt(password)));
        return super.update(wrapper);
    }

    @Override
    public DtsUserParam selectByUserId(String userId) {
        DtsUserParam dtsUserParam = new DtsUserParam();
        DtsUser dtsUser = super.getById(userId);
        List<DtsRole> dtsRoles = dtsRoleMapper.selectListByUserId(userId);
        List<String> roleIds = new ArrayList<>();
        dtsRoles.forEach(dtsRole -> {
            roleIds.add(String.valueOf(dtsRole.getId()));
        });
        Set<DtsMenu> dtsMenus = dtsMenuMapper.selectByRoles(roleIds);
        BeanUtil.copyProperties(dtsUser,dtsUserParam);
        dtsUserParam.setRoleList(dtsRoles);
        dtsUserParam.setMenuList(this.generate(dtsMenus));
        return dtsUserParam;
    }

    private List<DtsMenuParam> generate(Set<DtsMenu> dtsMenus){
        List<DtsMenuParam> dtsMenuParams = new ArrayList<>();
        dtsMenus.forEach(menu->{
            DtsMenuParam dtsMenuParam = new DtsMenuParam();
            if (menu.getPid()==0||menu.getPid()==null){
                BeanUtil.copyProperties(menu,dtsMenuParam);
                dtsMenuParam.setChildren(this.findNextTree(dtsMenus,menu.getId()));
                dtsMenuParams.add(dtsMenuParam);
            }
        });
        return dtsMenuParams;
    }
    private List<DtsMenuParam> findNextTree( Set<DtsMenu> dtsMenus, Long menuId ) {
        List<DtsMenuParam> dtsMenuParams = new ArrayList<>();
        dtsMenus.forEach(menu->{
            DtsMenuParam dtsMenuParam = new DtsMenuParam();
            if(menu.getPid().equals(menuId)){
                BeanUtil.copyProperties(menu,dtsMenuParam);
                dtsMenuParam.setChildren(findNextTree(dtsMenus,menu.getId()));
                dtsMenuParams.add(dtsMenuParam);
            }
        });
        return dtsMenuParams;
    }
}




