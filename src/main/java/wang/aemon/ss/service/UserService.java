package wang.aemon.ss.service;

import cn.hutool.core.lang.Validator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wang.aemon.ss.dao.UserMapper;
import wang.aemon.ss.domain.User;

import javax.annotation.Resource;

@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (Validator.isEmpty(user)) {
            throw new UsernameNotFoundException("账户不存在");
        }
        user.setRoles(userMapper.getUserRolesByUserId(user.getId()));
        return user;
    }
}
