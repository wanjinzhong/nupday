package com.nupday;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import com.nupday.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class NupdayApplicationTests {

    @Test
    public void generatePasswordAndSalt() {
        String password = "jiaqi";
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String securityPwd = new SimpleHash("MD5", password, salt, Constants.HASH_ITERATIONS).toHex();
        // System.out.println(salt);
        // System.out.println(securityPwd);
    }

    @Test
    public void timeTest() {
        LocalDate date = LocalDate.of(2018, 5,13);
        LocalDate now = LocalDate.now();
        // System.out.println(now.toEpochDay() - date.toEpochDay());

        Period period = Period.between(date, now);
        // System.out.println(period.getYears());
        // System.out.println(period.getMonths());
        // System.out.println(period.getDays());
        StringBuilder builder = new StringBuilder();
        if (period.getYears() > 0) {
            builder.append(period.getYears()).append("年");
        }
        if (period.getMonths() > 0) {
            if (StringUtils.isNotBlank(builder)) {
                builder.append("零");
            }
            builder.append(period.getMonths()).append("个月");
        }
        if (period.getDays() > 0) {
            if (StringUtils.isNotBlank(builder)) {
                builder.append("零");
            }
            builder.append(period.getDays()).append("天");
        }
        System.out.println(builder);
    }
}
