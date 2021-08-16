package com.doter.common.datasource.param;


import com.doter.common.datasource.entity.user.DtsMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName DtsMenuParam
 * @Description TODO
 * @Author HanTP
 * @Date 2021/5/19 8:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DtsMenuParam extends DtsMenu {

    private static final long serialVersionUID = -2989094100226526269L;

    private List<DtsMenuParam> children;
}
