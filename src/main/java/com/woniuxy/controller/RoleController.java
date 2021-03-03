package com.woniuxy.controller;


import com.woniuxy.domain.Role;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.mapper.PermissionMapper;
import com.woniuxy.service.RoleService;
import com.woniuxy.vo.SetPermissionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yym
 * @since 2021-02-02
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionMapper permissionMapper;
    @GetMapping
    public Result getAllRole(){
        List<Role> roles = roleService.list(null);
        return new Result(true, StatusCode.OK,"查询所有角色成功",roles);
    }
    @GetMapping("{uid}")
    public Result getRoleByUid(@PathVariable Integer uid){
        List<String> roles = roleService.findRolesByUid(uid);
        return new Result(true, StatusCode.OK,"根据用户id查询所有角色成功",roles);
    }
    @PostMapping("setPermission")
    public  Result setPermission(@RequestBody SetPermissionVO setPermissionVO){
        roleService.setPermission(setPermissionVO.getRid(),setPermissionVO.getPids());
        return new Result(true, StatusCode.OK,"修改权限成功");
    }
}

