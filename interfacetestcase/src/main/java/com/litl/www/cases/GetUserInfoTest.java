package com.litl.www.cases;

import com.litl.www.config.TestConfig;
import com.litl.www.model.GetUserInfoCase;
import com.litl.www.model.GetUserListCase;
import com.litl.www.model.User;
import com.litl.www.utils.DataBaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取id为1的用户接口")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DataBaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase", 1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        JSONArray resultJson = getResult(getUserInfoCase);
        Thread.sleep(2000);
        User user = session.selectOne(getUserInfoCase.getExpected(), getUserInfoCase);
        System.out.println("getUserInfoCase表查询出的用户" + user.toString());

        List userList = new ArrayList();
        userList.add(user);
        JSONArray jsonArray = new JSONArray(user);
        System.out.println("获取用户信息" + jsonArray.toString());
        System.out.println("调用接口获取用户信息" + resultJson.toString());
        Assert.assertEquals(resultJson.toString(), jsonArray.toString());
    }

    private JSONArray getResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoCase.getUserId());

        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("接口返回的用户" + result);
//        List resultList = Arrays.asList(result);
//        JSONArray array = new JSONArray(resultList);
        JSONArray array = new JSONArray(result);
        System.out.println(array.toString());
        return array;
    }
}
