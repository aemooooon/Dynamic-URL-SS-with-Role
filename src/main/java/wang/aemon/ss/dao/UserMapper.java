package wang.aemon.ss.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import wang.aemon.ss.domain.Role;
import wang.aemon.ss.domain.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User loadUserByUsername(String username);

    @Select("select * from role r,user_role ur where r.id=ur.role_id and ur.user_id=#{id}")
    List<Role> getUserRolesByUserId(Long id);
}
