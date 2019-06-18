package scse.com.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
	
	public static final String UPDATE_STUDENT="student.update";
	public static final String DO_SELECT="take.insert";
	
	/*
	 * Direct模式
	 */
	@Bean
	public Queue queue1() {
		return new Queue(UPDATE_STUDENT, true);
	}
	@Bean
	public Queue queue2() {
		return new Queue(DO_SELECT, true);
	}
}
