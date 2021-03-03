package com.woniuxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniuxy.domain.Permission;
import com.woniuxy.domain.User;
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
public interface UserMapper extends BaseMapper<User> {
    @Select("select p.* " +
            "from t_user u " +
            "join t_user_role ur " +
            "on u.id=ur.uid " +
            "join t_role r " +
            "on ur.rid=r.id " +
            "join t_role_permission rp " +
            "on r.id=rp.rid " +
            "join t_permission p " +
            "on rp.pid=p.id " +
            "where u.id=#{uid}")
    List<Permission> findMenusByUid(Integer uid);

    @Select("select p.* " +
            "from t_user u " +
            "join t_user_role ur " +
            "on u.id=ur.uid " +
            "join t_role r " +
            "on ur.rid=r.id " +
            "join t_role_permission rp " +
            "on r.id=rp.rid " +
            "join t_permission p " +
            "on rp.pid=p.id " +
            "where p.`level`=3 " +
            "and u.id=#{uid}")
    List<Permission> findButtonsByUid(Integer uid);

    @Delete("delete from t_user_role where uid=#{uid}")
    void deleteByUid(Integer uid);

    @Insert("insert into t_user_role(uid,rid) values(#{uid},#{rid})")
    void insertRoleByUidAndRid(@RequestParam Integer uid, @RequestParam Integer rid);

}
