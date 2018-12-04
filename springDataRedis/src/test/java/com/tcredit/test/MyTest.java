package com.tcredit.test;

import com.tcredit.bean.Student;
import com.tcredit.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Auther: chong.huo
 * Create  Date: 2018/11/29
 */
public class MyTest {


    private StudentService service;

    @Before
    public void before() {
        String conf = "spring-mybatis.xml";
        String conf1 = "spring-redis.xml";
        String conf2 = "spring-service.xml";
        String[] confs = {conf, conf1,conf2};
        ApplicationContext ac = new ClassPathXmlApplicationContext(confs);
        service = (StudentService) ac.getBean("studentServiceImpl");
    }

    @Test
    public void test01() {
        List<Student> students = service.findStudentsByName("李");
        for (Student student : students) {
            System.out.println(student);
        }
    }


    @Test
    public void test02(){
        System.out.println(service.findAllStudentsCount());
    }

    @Test
    public void test03(){
        Student s = new Student();
            s.setAge(25);
            s.setName("高高");
            service.saveStudent(s);
    }

    @Test
    public void test04(){
        Jedis jedis = new Jedis("192.168.1.107",6379);

        System.out.println(jedis.get("city"));
    }
}
