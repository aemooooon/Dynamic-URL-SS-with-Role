package wang.aemon.ss.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import wang.aemon.ss.service.UserService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserService userService;

    // 指定密码的加密方式
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    // 指定密码的加密方式
    @SuppressWarnings("deprecation")
    @Bean
    PasswordEncoder passwordEncoder() {
        // 不对密码进行加密
        return NoOpPasswordEncoder.getInstance();
    }

    // 配置用户及其对应的角色
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // configure AuthenticationManager so that it knows from where to load
//        // user for matching credentials
//        // Use BCryptPasswordEncoder
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }


    @Bean
    MyFilterInvocationSecurityMetadataSource cfisms() {
        return new MyFilterInvocationSecurityMetadataSource();
    }

    @Bean
    MyAccessDecisionManager cadm() {
        return new MyAccessDecisionManager();
    }

    // 配置 URL 访问权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // 开启 HttpSecurity 配置
                .withObjectPostProcessor(
                        new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                                object.setSecurityMetadataSource(cfisms());
                                object.setAccessDecisionManager(cadm());
                                return object;
                            }
                        }
                )
                .and().formLogin().loginProcessingUrl("/login").permitAll()
                .and().csrf().disable(); // 关闭csrf
    }

}
