package scse.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scse.com.domain.Teacher;
import scse.com.mapper.TeacherMapper;

@Service
public class TeacherService {
	
	@Autowired
	private TeacherMapper teacherMapper;
	
	//获取教师列
	public ArrayList<Teacher> getTeachers(){
		return teacherMapper.getTeachers();
	}
	
}
