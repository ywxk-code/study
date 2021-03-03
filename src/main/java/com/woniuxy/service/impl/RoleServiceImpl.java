package com.woniuxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniuxy.domain.Permission;
import com.woniuxy.domain.Role;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<String> findRolesByUid(Integer uid) {
        return roleMapper.findRolesByUid(uid);
    }

    @Override
    public void setPermission(Integer rid, List<Integer> pids) {
        //根据角色id删除该角色拥有的所有权限
        roleMapper.deletePermissionByRid(rid);
        //保存一级菜单id的集合
        HashSet<Integer> level1s = new HashSet<>();
        //保存二级菜单id的集合
        HashSet<Integer> level2s = new HashSet<>();
        //拿到的是二级菜单的id
        pids.forEach(pid->{
            //很确定是三级菜单的id
            Permission level2 = permissionMapper.getPermissionByPid(pid);
            level2s.add(level2.getId());
            //授权三级菜单
            roleMapper.insertPermissionByRidAndPid(rid,pid);
        });
        //拿到的是一级菜单的id
        level2s.forEach(level2Id->{
            Permission level1 = permissionMapper.getPermissionByPid(level2Id);
            level1s.add(level1.getId());
            //授权二级菜单
            roleMapper.insertPermissionByRidAndPid(rid,level2Id);
        });
        //授权一级菜单
        level1s.forEach(level1Id->{
            roleMapper.insertPermissionByRidAndPid(rid,level1Id);
        });
    }
}
