package com.litl.www.cases;

import com.litl.www.config.TestConfig;
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
import java.util.List;

public class GetUserListTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取sex为0的用户接口")
    public void getUserList() throws IOException, InterruptedException {
        SqlSession session = DataBaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase", 1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);

        JSONArray resultJson = getResult(getUserListCase);
        Thread.sleep(2000);
        List<User> userList = session.selectList(getUserListCase.getExpected(), getUserListCase);
        for (User u : userList) {
            System.out.println("list获取的用户" + u.toString());
        }
        JSONArray userListJson = new JSONArray(userList);

        Assert.assertEquals(userListJson.length(), resultJson.length());
        for (int i = 0; i < resultJson.length(); i++) {
            JSONObject expect = (JSONObject) resultJson.get(i);
            JSONObject actual = (JSONObject) userListJson.get(i);
            Assert.assertEquals(expect.toString(), actual.toString());
        }

    }

    private JSONArray getResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        param.put("userName", getUserListCase.getUserName());
        param.put("age", getUserListCase.getAge());
        param.put("sex", getUserListCase.getSex());

        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("接口返回的用户" + result.toString());
        JSONArray jsonArray = new JSONArray(result);
        return jsonArray;
    }
}
