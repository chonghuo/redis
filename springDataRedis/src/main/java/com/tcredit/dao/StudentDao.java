package com.tcredit.dao;

import java.util.List;

import com.tcredit.bean.Student;

public interface StudentDao {

	List<Student> selectStudentsByName(String name);

	Integer selectAllStudentsCount();

	void insertStudent(Student student);

}
