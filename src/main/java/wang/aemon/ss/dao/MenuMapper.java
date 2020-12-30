package wang.aemon.ss.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import wang.aemon.ss.domain.Menu;
import wang.aemon.ss.domain.Role;

import java.util.List;

@Mapper
public interface MenuMapper {
    @Select("SELECT m.*,r.id AS roleId,r.name AS roleName,r.name_Zh AS roleNameZh FROM menu m LEFT JOIN menu_role mr ON m.`id`=mr.`menu_id` LEFT JOIN role r ON mr.`role_id`=r.`id`")
    List<Menu> getAllMenus();

    @Select("select * from role r,menu_role mr where r.id=mr.role_id and mr.menu_id=#{id}")
    List<Role> getMenuRolesByMenuId(Long id);
}
