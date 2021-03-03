package com.woniuxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.woniuxy.domain.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select p.id " +
            "from t_role_permission rp " +
            "join t_permission p " +
            "on rp.pid=p.id " +
            "where rp.rid=#{rid} " +
            "and p.`level`=3")
    List<Integer> getPermissionsIdByRid(Integer rid);
    @Select("select * " +
            "from t_permission " +
            "where id=(" +
            "select p.pid " +
            "from t_permission p " +
            "where p.id=#{pid}" +
            ")")
    Permission getPermissionByPid(Integer pid);
}
