package com.tcredit.service;


import com.tcredit.bean.Student;

import java.util.List;

public interface StudentService {
	
	List<Student> findStudentsByName(String name);
	
	Integer findAllStudentsCount();
	
	void saveStudent(Student student);

	Integer findAllStudentsCountByOldType();
	
	
}
