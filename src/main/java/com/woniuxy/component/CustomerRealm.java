package com.woniuxy.component;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.woniuxy.domain.Role;
import com.woniuxy.mapper.RoleMapper;
import com.woniuxy.mapper.UserMapper;
import com.woniuxy.utils.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CustomerRealm extends AuthorizingRealm {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        User user = (User) principalCollection.getPrimaryPrincipal();
////        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
////        //查询当前用户拥有哪些权限
////        List<String> roles= roleMapper.findRolesByUid(user.getId());
////        if(!CollectionUtils.isEmpty(roles)){//如果查询到的角色集合不为空，开始授予角色
////            roles.forEach(role -> simpleAuthorizationInfo.addRole(role));
////        }
////        //查询当前用户拥有哪些权限
////        List<Permission> permissions = userMapper.findMenusByUid(user.getId());
////        if(!CollectionUtils.isEmpty(permissions)){
////            permissions.forEach(permission -> simpleAuthorizationInfo.addStringPermission(permission.getElement()));
////        }
////        return simpleAuthorizationInfo;
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String token = (String) principalCollection.getPrimaryPrincipal();
        DecodedJWT decodedJWT = JWTUtil.decodedToken(token);
        String username = decodedJWT.getClaim("username").asString();
        List<Role> roles =roleMapper.getRolesByUsername(username);
        roles.forEach(role -> {
            authorizationInfo.addRole(role.getRolename());
        });
        return authorizationInfo;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String username = (String) authenticationToken.getPrincipal();
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("username",username);
//        User userDB = userMapper.selectOne(wrapper);
//        if(!ObjectUtils.isEmpty(userDB)){
//            return new SimpleAuthenticationInfo(userDB,
//                    userDB.getPassword(),
//                    ByteSource.Util.bytes(userDB.getSalt()),
//                    this.getName());
//        }
        String token = (String) authenticationToken.getPrincipal();
        System.out.println("在这里"+token);
        DecodedJWT decodedJWT = JWTUtil.decodedToken(token);
        String username = decodedJWT.getClaim("username").asString();
        if (!StringUtils.hasLength(username)||!JWTUtil.verify(token,username)){
            throw new AuthenticationException("token验证失败");
        }
        return new SimpleAuthenticationInfo(token,token,this.getName());
    }
}
