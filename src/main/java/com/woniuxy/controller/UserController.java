package com.woniuxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.woniuxy.domain.Permission;
import com.woniuxy.domain.User;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.service.UserService;
import com.woniuxy.utils.JWTUtil;
import com.woniuxy.utils.SaltUtils;
import com.woniuxy.vo.PageVO;
import com.woniuxy.vo.SetRoleVO;
import com.woniuxy.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("register")
    public Result register(@RequestBody UserVO userVO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userVO.getUsername());
        User userDB = userService.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(userDB)) {
            //加密注册
            userDB=new User();
            String salt = SaltUtils.getSalt(8);
            Md5Hash hash = new Md5Hash(userVO.getPassword(), salt,1024);
            userDB.setUsername(userVO.getUsername());
            userDB.setPassword(hash.toHex());
            userDB.setSalt(salt);
            userService.save(userDB);
            return new Result(true, StatusCode.OK,"注册成功");
        }else {
            return new Result(false, StatusCode.ACCOUNTEXISTS,"当前用户已存在");
        }
    }
    @PostMapping("login")
    public Result login(@RequestBody UserVO userVO) {
        //用shiro的登录方式登录
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(userVO.getUsername(), userVO.getPassword(),userVO.getChecked());
//        subject.login(token);
//        return new Result(true,StatusCode.OK,"登录成功",userVO);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVO.getUsername());
        User userDB = userService.getOne(wrapper);
        if (!ObjectUtils.isEmpty(userDB)){
            Md5Hash md5Hash = new Md5Hash(userVO.getPassword(),userDB.getSalt(),1024);
            String md5Password = md5Hash.toHex();
            if (md5Password.equals(userDB.getPassword())) {
                //创建jwt并返回
                HashMap<String, String> map = new HashMap<>();
                map.put("username",userVO.getUsername());
                String jwtToken = JWTUtil.createToken(map);
                return new Result(true,StatusCode.OK,"登录成功",jwtToken);
            }else {
                return new Result(false,StatusCode.INCORRECTCREDENTIALS,"密码错误");
            }
        }else {
            return new Result(false,StatusCode.UNKNOWNACCOUNT,"当前用户未注册");
        }
    }

    @GetMapping("getMenus")
//    @RequiresRoles(value = {"董事长","人事总监"},logical = Logical.OR)
    public Result getMenus(){
        //根据登录的用户身份去查询能看到的菜单
        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        DecodedJWT decodedJWT = JWTUtil.decodedToken(token);
//        String username = decodedJWT.getClaim("username").asString();
        //根据用户id查询所能看到的菜单
        List<Permission> menus = userService.findMenusByUid(user.getId());
        return new Result(true,StatusCode.OK,"查询菜单成功",menus);
    }
    //get请求不需要requestmapping
    @GetMapping("getAll")
    public Result fand2page(PageVO pageVO){
        Page<User> userPage = new Page<>(pageVO.getCurrent(),pageVO.getSize());
        IPage<User> page = userService.page(userPage,null);
        return new Result(true, StatusCode.OK,"用户信息分页查询成功",page);
    }
    @GetMapping("getButtons")
    public Result getButtons(){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Permission> buttons=userService.findButtonsByUid(user.getId());
        return  new Result(true,StatusCode.OK,"查询按钮成功",buttons);
    }
    @PostMapping("setRole")
    public Result setRole(@RequestBody SetRoleVO setRoleVO){
        //处理修改角色流程
        List<String> roleNames = setRoleVO.getCheckList();
        Integer uid = setRoleVO.getUid();
        userService.setRole(uid,roleNames);
        return new Result();
    }
}

