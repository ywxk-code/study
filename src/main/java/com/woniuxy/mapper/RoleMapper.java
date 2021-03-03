package com.woniuxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniuxy.domain.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select r.rolename " +
            "from t_user u " +
            "join t_user_role ur " +
            "on u.id=ur.uid " +
            "join t_role r " +
            "on ur.rid=r.id " +
            "where u.id=#{uid}")
    List<String> findRolesByUid(Integer uid);
    @Delete("delete from t_role_permission where rid=#{rid}")
    void deletePermissionByRid(Integer rid);
    @Insert("insert into t_role_permission(rid,pid) values(#{rid},#{pid})")
    void insertPermissionByRidAndPid(@RequestParam Integer rid,@RequestParam Integer pid);
    @Select("select r.* " +
            "from t_user u " +
            "join t_user_role ur " +
            "on u.id=ur.uid " +
            "join t_role r " +
            "on ur.rid=r.id " +
            "where u.username=#{username}")
    List<Role> getRolesByUsername(String username);
}
