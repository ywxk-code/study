package com.woniuxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.woniuxy.domain.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
public interface RoleService extends IService<Role> {
    List<String> findRolesByUid(Integer uid);
    void setPermission(Integer rid,List<Integer> pids);
}
