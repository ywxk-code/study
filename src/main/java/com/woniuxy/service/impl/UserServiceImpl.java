package com.woniuxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniuxy.domain.Permission;
import com.woniuxy.domain.Role;
import com.woniuxy.domain.User;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Override
    public List<Permission> findMenusByUid(Integer uid) {
        //查询得到的所有菜单
        List<Permission> permissions = userMapper.findMenusByUid(uid);
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
                  root.getChildren().add(permission);
                  return;
              }
           });
        });
        return rootMenus;
    }

    @Override
    public List<Permission> findButtonsByUid(Integer uid) {
        return userMapper.findButtonsByUid(uid);
    }

    @Override
    public void setRole(Integer uid, List<String> roleNames) {
        //1.删除之前的所有角色
        userMapper.deleteByUid(uid);
        //2.设置所有选中的角色
        ArrayList<Integer> roleIds=new ArrayList<>();
        roleNames.forEach(rolename->{
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq("rolename",rolename);
            Role role = roleMapper.selectOne(wrapper);
            roleIds.add(role.getId());

        });
        roleIds.forEach(rid->{
            userMapper.insertRoleByUidAndRid(uid,rid);
        });
    }
}
