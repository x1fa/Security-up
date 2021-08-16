package com.doter.common.tools;


import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.enums.ResultStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ClassName SecurityUtils
 * @Description TODO
 * @Author HanTP
 * @Date 2020/10/26 15:31
 */
public class SecurityTool {
    /**
     * 获取Authentication
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    private static UserDetail getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetail) {
            return (UserDetail) principal;
        }
        throw new UsernameNotFoundException(ResultStatus.TOKEN_PARSE_ERROR.getMessage());
    }

    /**
     * 获取用户
     */
    public static UserDetail getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException(ResultStatus.TOKEN_PARSE_ERROR.getMessage());
        }
        return getUser(authentication);
    }
}
