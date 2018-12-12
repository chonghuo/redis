package com.tcredit.test;

import com.tcredit.bean.Student;
import com.tcredit.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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


    //模拟高并发情景
    @Test
    public void test04(){
            // 定义任务：查询学生总人数
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    service.findAllStudentsCount();
                }
            };
        //创建一个线程池。池内默认有二十个线程
        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        //模拟1000个并发访问，然后将这1000个并发访问请求交给这20个线程处理
        for (int i = 0 ; i <1000;i++){
            //为线程池中的线程对象提交所要执行的任务
            threadPool.submit(task);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    @Test
    public void test05(){
        // 定义任务：查询学生总人数
        Runnable task = new Runnable() {
            @Override
            public void run() {
                service.findAllStudentsCountByOldType();
            }
        };
        //创建一个线程池。池内默认有二十个线程
        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        //模拟1000个并发访问，然后将这1000个并发访问请求交给这20个线程处理
        for (int i = 0 ; i <1000;i++){
            //为线程池中的线程对象提交所要执行的任务
            threadPool.submit(task);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
