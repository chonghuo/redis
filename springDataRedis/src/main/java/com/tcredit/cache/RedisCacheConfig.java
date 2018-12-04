package com.tcredit.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Auther: chong.huo
 * Create  Date: 2018/12/4
 */
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Override
    public KeyGenerator keyGenerator() {

        //返回key生成器
        return new KeyGenerator() {
            //生成的key规则：注解所在的类名_方法名_参数列表
            //target 当前注解所标注的类的对象
            //method 当前注解所标注的方法
            //params 当前注解所标注的方法的参数
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                String className = o.getClass().getName();
                String methodName = method.getName();
                return className + "_" + methodName + "_" + objects.toString();
            }
        };
    }
}
