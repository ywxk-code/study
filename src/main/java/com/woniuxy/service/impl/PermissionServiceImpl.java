package com.woniuxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniuxy.domain.Permission;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Integer> getPermissionsIdByRid(Integer rid) {
        return permissionMapper.getPermissionsIdByRid(rid);
    }

    @Override
    public List<Permission> getAllPermissions() {
        //查询得到的所有菜单
        List<Permission> permissions = permissionMapper.selectList(null);
        //实现一个功能，将菜单进行组合，每个二级菜单都对应自己的一级菜单
        //创建一个所有一级菜单的集合
        ArrayList<Permission> rootMenus = new ArrayList<>();
        //进行遍历
        permissions.forEach(permission -> {
            //如果是一级菜单
            if(permission.getLevel()==1){
                //给一级菜单设置二级菜单集合
                permission.setChildren(new ArrayList<Permission>());
                rootMenus.add(permission);
                return;
            }
        });
        permissions.forEach(permission -> {
            rootMenus.forEach(root ->{
                //如果某个菜单的pid跟一级菜单的pid相同，则其一定是它的二级菜单
                //此时将该菜单放到一级菜单的子菜单中
                if(permission.getPid()==root.getId()){
                    permission.setChildren(new ArrayList<Permission>());
                    root.getChildren().add(permission);
                    return;
                }
            });
        });
        permissions.forEach(permission -> {//遍历所有权限
            if(permission.getLevel()==3){
                rootMenus.forEach(level1->{//遍历一级菜单
                    List<Permission> level2s = level1.getChildren();//得到一级菜单下的所有二级菜单
                    level2s.forEach(level2->{//遍历二级菜单
                        if (permission.getPid()==level2.getId()) {
                            //将三级菜单存放到对应的二级菜单中去
                            level2.getChildren().add(permission);
                        }
                    });
                });
            }
        });
        return rootMenus;
    }
}
