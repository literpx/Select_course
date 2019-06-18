package scse.com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;

import scse.com.redis.HtmlKey;
import scse.com.redis.RedisService;
import scse.com.result.Result;
import scse.com.service.StudentService;
import scse.com.vo.StudentLoginVo;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;
	private static Logger log = LoggerFactory.getLogger(StudentController.class);
	
	@RequestMapping(value="/toLogin", produces="text/html")
	@ResponseBody
	public String toLogin(HttpServletRequest request,HttpServletResponse response,Model model) {
		String html = redisService.get(HtmlKey.login, "", String.class);
    	if(!StringUtils.isEmpty(html)) { //已缓存页面
    		log.info("已缓存页面,进入登陆页面");
    		return html;
    	}
    	SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
    	//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("login", ctx);
    	if(!StringUtils.isEmpty(html)) {
    		redisService.set(HtmlKey.login, "", html);
    	}
    	return html;
	}
	
	@RequestMapping("/error/{goWhere}")//错误动态跳转
	public String goError(@PathVariable("goWhere") String goWhere) {
		return goWhere;
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public Result<String> doLogin(HttpServletResponse response,StudentLoginVo studentLoginVo) {
		studentService.studentLogin(response,studentLoginVo);
		return Result.success("登陆成功");
	}
}
