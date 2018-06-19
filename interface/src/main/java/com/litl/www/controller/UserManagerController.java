package com.litl.www.controller;

import com.litl.www.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j
@RestController
@Api(value = "UserManager", description = "用户管理系统")
@RequestMapping("UserManager")
public class UserManagerController {
    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user) {
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        System.out.println("查看到的结果是" + i);
        if (i == 1) {
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Boolean addUser(HttpServletRequest request, @RequestBody User user) {
        Boolean i = verifyCookies(request);
        int addUserNum = 0;
        if (i != null) {
            addUserNum = template.insert("addUser", user);
        }
        if (addUserNum > 0) {
            System.out.println("添加用户的数量为" + addUserNum);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user) {
        Boolean i = verifyCookies(request);
        if (i == true) {
            List<User> users = template.selectList("getUserInfo", user);
            System.out.println("getUserInfo获取到的用户数量为" + users.size());
            return users;
        }
        return null;
    }

    @ApiOperation(value = "更新用户接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request, @RequestBody User user) {
        Boolean i = verifyCookies(request);
        int updateUserNum;
        if (i == true) {
            updateUserNum = template.update("updateUser", user);
            System.out.println("更新的用户数量为" + updateUserNum);
            return updateUserNum;
        }
        return 0;
    }

    @ApiOperation(value = "删除用户接口", httpMethod = "POST")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public int deleteUser(HttpServletRequest request, @RequestBody User user) {
        Boolean i = verifyCookies(request);
        int deleteUserNum;
        if (i == true) {
            deleteUserNum = template.delete("deleteUser", user);
            System.out.println("删除的用户数量为" + deleteUserNum);
            return deleteUserNum;
        }
        return 0;
    }

    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            System.out.println("cookies为空");
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                System.out.println("cookies验证通过");
                return true;
            }
        }
        return false;
    }
}
