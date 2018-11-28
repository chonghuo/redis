package com.tcredit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private Jedis jedis;

    @Before
    public void before() {
        jedis = new Jedis("172.19.163.86", 6379);
//        jedis.auth("111");
    }

    @Test
    public void test01() {
        System.out.println(jedis.ping());
    }

    @Test
    public void test02() {
        jedis.set("city", "beijing");
        System.out.println(jedis.get("city"));
    }

    @Test
    public void test03() {
        System.out.println(jedis.keys("*"));
    }
}
