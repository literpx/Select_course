package scse.com.rabbitmq;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import scse.com.controller.CourseController;
import scse.com.domain.CourseGroup;
import scse.com.domain.Student;
import scse.com.domain.Take;
import scse.com.domain.in.DoSelect;
import scse.com.domain.in.TakeIn;
import scse.com.exception.GlobalException;
import scse.com.redis.CourseKey;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.redis.TakeKey;
import scse.com.result.CodeMsg;
import scse.com.result.Result;
import scse.com.service.CourseGroupService;
import scse.com.service.StudentService;
import scse.com.service.TakeService;
import scse.com.vo.RedisSynVo;

@Service
public class MQReceiver {
	
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
	@Autowired
	private StudentService studentService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private TakeService takeService;
	@Autowired
	private CourseGroupService courseGroupService;
	
	@RabbitListener(queues=MQConfig.UPDATE_STUDENT)
	public void receiveUpdateStudent(String message) {
		Student student=JSON.parseObject(message, Student.class);
		if(studentService.updateStudent(student)) {
			log.info("密码更新成功！！！");
			redisService.set(StudentKey.token, student.getSalt(), student);
		}
	}
	@RabbitListener(queues=MQConfig.DO_SELECT)
	public void doSelect(String message) {
		DoSelect doSelect=JSON.parseObject(message, DoSelect.class);
		if(doSelect==null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		takeService.selectCourse(doSelect.getGroupId(), doSelect.getStudent(), doSelect.getCourse(),false);
	}
	
	
}
