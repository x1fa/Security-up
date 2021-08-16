package com.doter.common.datasource.param;


import com.doter.common.datasource.entity.user.DtsRole;
import com.doter.common.datasource.entity.user.DtsUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName DtsUserParam
 * @Description TODO
 * @Author HanTP
 * @Date 2021/5/19 8:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DtsUserParam extends DtsUser {
    private static final long serialVersionUID = -3558298339206549742L;

    private List<DtsRole> roleList;

    private List<DtsMenuParam> menuList;
}
