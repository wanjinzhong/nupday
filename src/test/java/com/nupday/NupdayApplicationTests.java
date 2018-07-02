package com.nupday;

import java.util.UUID;

import com.nupday.constant.Constants;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class NupdayApplicationTests {

    @Test
    public void contextLoads() {
        String password = "loveyou";
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String securityPwd = new SimpleHash("MD5", password, salt, Constants.HASH_ITERATIONS).toHex();
        System.out.println(salt);
        System.out.println(securityPwd);
    }

}
