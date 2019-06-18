package scse.com.service;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scse.com.domain.Student;
import scse.com.exception.GlobalException;
import scse.com.mapper.StudentMapper;
import scse.com.rabbitmq.MQSender;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.result.CodeMsg;
import scse.com.util.MD5Util;
import scse.com.util.UUIDUtil;
import scse.com.vo.StudentLoginVo;

@Service
public class StudentService {
	
	public  static final String COOKI_NAME_TOKEN = "token";
	
	Logger log=LoggerFactory.getLogger(StudentService.class);
	
	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MQSender sender;
	
	public boolean studentLogin(HttpServletResponse response, StudentLoginVo studentLoginVo) {
		Student student=studentMapper.getStudentByNo(studentLoginVo.getStuNo());
		if(student==null) {
			throw new GlobalException(CodeMsg.STUDENT_NOT_EXIST);
		}
		//检查密码
		String dbPassword=student.getPassword();
		String salt=student.getSalt();  //salt即reids里的token
		String inputPassword=studentLoginVo.getPassword();
		String formPassword=MD5Util.formPassToDBPass(inputPassword, salt);
		if(!formPassword.equals(dbPassword)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//密码正确，旧的session删除
		redisService.delete(StudentKey.token,salt);
		//登陆成功,生成tonken，存入cookie,存入redis
		String token=UUIDUtil.uuid();
		//更新数据库密码和salt
		String newPassword=MD5Util.formPassToDBPass(inputPassword,token);
		Student newStudent=new Student();
		newStudent.setPassword(newPassword);
		newStudent.setSalt(token);
		newStudent.setStuNo(student.getStuNo());
		//发送到处理队列
		sender.sendUpdateStudent(newStudent);
		addCookie(response, token, student);
		return true;
	}

	private void addCookie(HttpServletResponse response, String token, Student student) {
		//key:前缀:token 值:student
		redisService.set(StudentKey.token, token, student);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		//cookie存活时间与缓存过期时间一致
		cookie.setMaxAge(StudentKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	public ArrayList<Student> getStudentsList() {
		return studentMapper.getStudentsList();
	}

	public boolean updateStudent(Student student) {
		return studentMapper.updateStudent(student)>0;
	}

	public boolean addStudent(Student student) {
		return studentMapper.addStudent(student)>0;
	}
	
	public Student getStudentByNo(int stuNo) {
		return studentMapper.getStudentByNo(stuNo);
	}
}
