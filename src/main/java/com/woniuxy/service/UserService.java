package com.woniuxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.woniuxy.domain.Permission;
import com.woniuxy.domain.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
public interface UserService extends IService<User> {
    public List<Permission> findMenusByUid(Integer uid);

    List<Permission> findButtonsByUid(Integer uid);

    void setRole(Integer uid, List<String> roleNames);
}
