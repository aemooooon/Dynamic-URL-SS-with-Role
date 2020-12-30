package wang.aemon.ss.conf;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 如果具备权限，则不做任何事情即可
        for (ConfigAttribute configAttribute : collection) {
            // 如果需要的角色是ROLE_LOGIN，说明当前请求的URL用户登录后即可访问
            // 如果auth是UsernamePasswordAuthenticationToken的实例，说明当前用户已登录，该方法到此结束
            if ("ROLE_LOGIN".equals(configAttribute.getAttribute()) &&
                    authentication instanceof UsernamePasswordAuthenticationToken) {
                return;
            }

            // 否则进入正常的判断流程
            for (GrantedAuthority authority : authorities) {
                // 如果当前用户具备当前请求需要的角色，那么方法结束
                if (configAttribute.getAttribute().equals(authority.getAuthority())) {
                    return;
                }
            }

        }

        // 如果不具备权限，就抛出AccessDeniedException异常
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
