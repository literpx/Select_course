package scse.com.access;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import scse.com.domain.Student;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.result.CodeMsg;
import scse.com.result.Result;
import scse.com.service.StudentService;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private StudentService studentService;
	@Autowired
	private RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {  //好像是有绑定参数的方法
			Student student=null;
			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);//获取自定义的限制操作
			if(accessLimit == null) {  //没有限制条件，直接通过
				return true;
			}
			if(accessLimit.needLogin()) {
				student=getStudent(request,response); //获取用户信息
				//System.out.println(student);
				if(student==null) {
					//用户没有登陆
					request.setAttribute("errMsg",CodeMsg.SESSION_ERROR.getMsg());
					request.getRequestDispatcher("/student/error/login").forward(request,response);
					return false;
				}
				StudentContext.setStdent(student);//保存学生信息，用来controller获取
			}
		}
		return true;
	}

	@SuppressWarnings("static-access")
	private Student getStudent(HttpServletRequest request, HttpServletResponse response) {
		//在请求中获取cookie，cookie可以有两种方式传值，一是保存在cookie数组中，而是放在参数中,首选第一种
		String cookieToken=getCookieToken(request,studentService.COOKI_NAME_TOKEN);
		String paramToken=request.getParameter(studentService.COOKI_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return redisService.get(StudentKey.token, token, Student.class);
	}

	private String getCookieToken(HttpServletRequest request,String cookieName) {
		if(request.getCookies()==null) {
			return null;
		}
		Cookie[] cookies=request.getCookies();
		for(Cookie cookie:cookies) {
			if(cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
