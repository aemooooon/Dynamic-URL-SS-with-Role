package wang.aemon.ss.conf;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import wang.aemon.ss.dao.MenuMapper;
import wang.aemon.ss.domain.Menu;
import wang.aemon.ss.domain.Role;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private MenuMapper menuMapper;

    // 创建一个AntPathMatcher，实现Ant风格的URL匹配。
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 从参数中提取出当前请求的URL
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        // 从数据库中获取所有的资源信息
        List<Menu> allMenus = menuMapper.getAllMenus();

        // 遍历资源信息，遍历过程中获取当前请求的URL所需要的角色信息并返回。
        for (Menu menu : allMenus) {
            menu.setRoles(menuMapper.getMenuRolesByMenuId(menu.getId()));
            if (antPathMatcher.match(menu.getPattern(), requestUrl)) {
                List<Role> roles = menu.getRoles();
                String[] roleArray = new String[roles.size()];
                for (int i = 0; i < roleArray.length; i++) {
                    roleArray[i] = roles.get(i).getName();
                }

                System.out.println("menu pattern: " + menu.getPattern());
                System.out.println("request URL: " + requestUrl);
                Arrays.stream(roleArray).forEach(s -> System.out.println("role array: " + s));

                return SecurityConfig.createList(roleArray);
            }
        }

        // 如果当前请求的URL在资源表中不存在相应的模式，就假设该请求登录后即可访问，即直接返回 ROLE_LOGIN.
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    // 该方法用来返回所有定义好的权限资源，Spring Security在启动时会校验相关配置是否正确。
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // 如果不需要校验，那么该方法直接返回null即可
        return null;
    }

    // supports方法返回类对象是否支持校验
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
