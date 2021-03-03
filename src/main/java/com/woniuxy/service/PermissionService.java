package com.woniuxy.service;

import com.woniuxy.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
public interface PermissionService extends IService<Permission> {
    List<Integer> getPermissionsIdByRid(Integer rid);
    List<Permission> getAllPermissions();
}
