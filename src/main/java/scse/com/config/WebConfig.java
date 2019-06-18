package scse.com.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import scse.com.access.AccessInterceptor;


@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter{
	
	@Autowired
	StudentArgumentResolver studentArgumentResolver;
	
	@Autowired
	AccessInterceptor accessInterceptor;
	
	//参数处理中添加StudentArgumentResolver的处理方法
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(studentArgumentResolver);
	}
	
	//添加拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessInterceptor);
	}
	
}
