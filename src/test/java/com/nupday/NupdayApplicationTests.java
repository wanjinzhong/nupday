package com.nupday;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class NupdayApplicationTests {

    @Test
    public void contextLoads() {
        String filename = "gfdg.tar.gz";
        String prefix="test";
        StringBuilder key = new StringBuilder()
            .append(prefix.trim())
            .append("/")
            .append(UUID.randomUUID().toString().toLowerCase().replace("-",""));
        if (filename != null && filename.indexOf(".") > -1) {
            key.append(filename.substring(filename.lastIndexOf(".")));
        }
        System.out.println(key);
    }

}
