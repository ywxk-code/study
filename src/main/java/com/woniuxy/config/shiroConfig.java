package com.woniuxy.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.woniuxy.component.CustomerRealm;
import com.woniuxy.filter.JwtFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class shiroConfig {
    @Bean
    public Realm realm(){
        CustomerRealm customerRealm = new CustomerRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(matcher);
        return customerRealm;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm());
        //将cookieRememberMeManager保存到defaultWebSecurityManager
        manager.setRememberMeManager(cookieRememberMeManager());
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager());
        //创建filters
        Map<String, Filter> filters = bean.getFilters();
        filters.put("jwt",new JwtFilter());
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //设置白名单
        map.put("/user/login","anon");
        map.put("/user/register","anon");
        map.put("/user/index","anon");
        map.put("/register.html","anon");
        map.put("/login.html","anon");
        map.put("/user/logout","logout");
        map.put("/js/**","anon");
        map.put("/css/**","anon");
        map.put("/**","user");
        map.put("/**","jwt");
        bean.setFilterChainDefinitionMap(map);
        //bean.setLoginUrl("/login.html");
        return bean;
    }
    //cookieRememberMeManager
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //创建cookie
        SimpleCookie rememberMe = new SimpleCookie("rememberMe");
        //设置有效期
        rememberMe.setMaxAge(7*24*60*60);
        //将cookie保存到cookieRememberMeManager中
        cookieRememberMeManager.setCookie(rememberMe);
        cookieRememberMeManager.setCipherKey(Base64.decode("a1b2c3d4e5f6h7j8k9l10m=="));
        return cookieRememberMeManager;
    }
    //分页组件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
