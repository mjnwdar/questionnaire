package com.zzq.graduationproject;

import com.zzq.graduationproject.model.User;
import com.zzq.graduationproject.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author zzq
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

  @Autowired
  private UserService userService;

  @Test
  public void login() {

    Map<String, String> map = userService.loginVerify("gold", "123");

    for (String key : map.keySet()) {
      System.out.println(map.get(key));
    }

  }


  @Test
  public void register() {
    User user = new User("gold14", "123", "zhangsan");
    Map<String, String> map = userService.register(user);

    for (String key : map.keySet()) {
      System.out.println(map.get(key));
    }
  }

//  @Test
//  public void deleteUser() {
//    Map<String, String> map = userService.deleteUserByUsername("gold5");
//
//    for (String key : map.keySet()) {
//      System.out.println(map.get(key));
//    }
//  }

}
