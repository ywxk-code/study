package com.woniuxy.controller;


import com.woniuxy.domain.Permission;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;
    @GetMapping
    public Result getAllPermissions(){
        List<Permission> permissions = permissionService.getAllPermissions();
        return new Result(true, StatusCode.OK,"查询所有权限成功",permissions);
    }
    @GetMapping("{rid}")
    public Result getPermissionsByRid(@PathVariable Integer rid){
        List<Integer> permissionsIds = permissionService.getPermissionsIdByRid(rid);
        return new Result(true, StatusCode.OK,"根据角色id查询角色对应的权限成功",permissionsIds);
    }
}

