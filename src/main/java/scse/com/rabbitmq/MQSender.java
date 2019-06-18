package scse.com.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import scse.com.domain.Student;
import scse.com.domain.in.DoSelect;
import scse.com.redis.RedisService;
import scse.com.vo.RedisSynVo;


@Service
public class MQSender {
	
	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void sendUpdateStudent(Student student) {
		if(student==null) {
			return;
		}
		String msg=JSONObject.toJSONString(student);
		amqpTemplate.convertAndSend(MQConfig.UPDATE_STUDENT,msg);
	}
	public void sendDoSelect(DoSelect doSelect) {
		if(doSelect==null) {
			return;
		}
		String msg=JSONObject.toJSONString(doSelect);
		amqpTemplate.convertAndSend(MQConfig.DO_SELECT,msg);
	}
}
