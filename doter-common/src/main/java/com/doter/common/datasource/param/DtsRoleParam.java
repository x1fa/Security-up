package com.doter.common.datasource.param;


import com.doter.common.datasource.entity.user.DtsRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName DtsRoleParam
 * @Description TODO
 * @Author HanTP
 * @Date 2021/5/19 8:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DtsRoleParam extends DtsRole {
    private static final long serialVersionUID = -2491125660473031536L;

    List<DtsMenuParam> menuList;
}
