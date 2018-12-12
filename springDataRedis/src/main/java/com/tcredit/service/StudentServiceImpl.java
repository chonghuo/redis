package com.tcredit.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.tcredit.bean.Student;
import com.tcredit.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao dao;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Transactional
	@Override
	public List<Student> findStudentsByName(String name) {
		String key = "findStudentsByName_"+name;
		BoundValueOperations<Object, Object> ops = redisTemplate.boundValueOps(key);
//		从缓存中获取指定key的数据
		Object students = ops.get();
//		若缓存中指定key的数据为null,则从db中查询，查询结果先要放入到Redis数据库
		if(students == null){
		   log.info("从数据库查询姓名中包含" + name + "的所有学生");
//		 从DB中查询
			students = dao.selectStudentsByName(name);
//			将db中查询的结果存入redis缓存
			ops.set(students);
		}else{

			log.info("从缓存中查询姓名中包含" + name + "的所有学生");
		}

		return (List<Student>) students;



	}
	//使用双重检测锁机制"预防"热点缓存
	@Transactional
	@Override
	public Integer findAllStudentsCount() {

		ValueOperations<Object, Object> ops = redisTemplate.opsForValue();
		String key = "findAllStudentsCount";
		Object count = ops.get(key);
		if(count == null){
			log.info("从DB中查询所有学生人数");
			//同步代码块
			synchronized (this){
				count =ops.get(key);
				// 第二次检测
				if(count == null){
					//从db中查询
					count = dao.selectAllStudentsCount();
					//将查询结果存放到redis缓存，并为其设置生存时限
					ops.set(key,count,20, TimeUnit.SECONDS);
				}else{
					log.info("从缓存中查询所有学生人数:"+count+"--11111");
				}
			}

		}else{

			log.info("从缓存中查询所有学生人数:"+count+"--22222");
		}
		return (Integer) count;

	}

	@Transactional
	@Override
	public Integer findAllStudentsCountByOldType() {

		ValueOperations<Object, Object> ops = redisTemplate.opsForValue();
		String key = "findAllStudentsCount";
		Object count = ops.get(key);
		if(count == null){
			//从db中查询
			count = dao.selectAllStudentsCount();
			//将查询结果存放到redis缓存，并为其设置生存时限
			ops.set(key,count,20, TimeUnit.SECONDS);
		}

		return (Integer) count;

	}

	@Transactional
	@Override
	public void saveStudent(Student student) {
		Set<Object> keys = redisTemplate.keys("findStudentsByName_*");
//		从redis缓存中删除指定的key
		redisTemplate.delete(keys);
		log.debug("向DB中插入一个学生信息");
		dao.insertStudent(student);
	}

}
