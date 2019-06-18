package scse.com.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import scse.com.domain.Student;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.result.Result;
import scse.com.service.InitService;
import scse.com.service.StudentService;
import scse.com.util.MD5Util;
import scse.com.util.UUIDUtil;

@Controller
@RequestMapping("/init")
public class InitController {
	@Autowired
	private InitService initService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RedisService redisService;
	
	private final int stuCount=2000;
	
	@RequestMapping("/reset_db")
	@ResponseBody
	public Result<String> resetDb() { 
		initService.resetDb();
		return Result.success("");
	}
	
	@RequestMapping("/upload_group_course")
	@ResponseBody
	public Result<String> uploadGroupCourse() { 
		initService.uploadCourseGroup();
		return Result.success("");
	}


	@RequestMapping("/courseUpload")
	@ResponseBody
	public String courseUpload() {  //上传课程
		initService.uploadCourse();
		return "";
	}
	
	@RequestMapping("/sutdent_insert_db")
	@ResponseBody
	public String sutdentInsert() {  //批量上传学生
		for(int i=1;i<=stuCount;i++){
			Student student=new Student();
			String timeString=new Date().getTime()+"";
			timeString=timeString.substring(timeString.length()-9, timeString.length());
			student.setStuNo(Integer.valueOf(timeString)+i);
			student.setStuName("学生"+i);
			String token=UUIDUtil.uuid();
			String password=MD5Util.inputPassToDbPass("123456", token);
			student.setPassword(password);
			student.setSalt(token);
			student.setGrade(16);
			student.setMajor("计算机科学与技术");
			student.setMayCredits(50);
			student.setHaveCredits(0);
			studentService.addStudent(student);
			redisService.set(StudentKey.token, token, student);
		}
		return "";
	}
	@RequestMapping("/sutdent_insert_redis")
	@ResponseBody
	public String sutdentInsertRedis() {  //批量把学生的信息加入缓存
		ArrayList<Student> students=studentService.getStudentsList();
		for(Student student:students) {
			redisService.set(StudentKey.token, student.getSalt(), student);
		}
		return "";
	}
	//	@RequestMapping("/upload_select_course")
//	@ResponseBody
//	public Result<String> uploadSelectCourse() { 
//		initService.uploadSelectCourse();
//		return Result.success("");
//	}
}
