package scse.com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import scse.com.access.AccessLimit;
import scse.com.domain.Course;
import scse.com.domain.CourseGroup;
import scse.com.domain.Student;
import scse.com.domain.Take;
import scse.com.domain.Time;
import scse.com.domain.Week;
import scse.com.domain.in.CourseGroupTime;
import scse.com.domain.in.DoSelect;
import scse.com.domain.in.TakeIn;
import scse.com.rabbitmq.MQReceiver;
import scse.com.rabbitmq.MQSender;
import scse.com.redis.CourseKey;
import scse.com.redis.HtmlKey;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.redis.TakeKey;
import scse.com.redis.TimeKey;
import scse.com.result.CodeMsg;
import scse.com.result.Result;
import scse.com.service.CourseGroupService;
import scse.com.service.CourseService;
import scse.com.service.StudentService;
import scse.com.service.TakeService;
import scse.com.service.TimeService;
import scse.com.vo.RedisSynVo;
import scse.com.vo.SelectSuccessVo;

@Controller
@RequestMapping("/course")
public class CourseController implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(CourseController.class);
	@Autowired
	private RedisService redisService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;
	@Autowired
	private TimeService timeService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TakeService takeService;
	@Autowired
	private CourseGroupService courseGroupService;
	@Autowired
	private MQSender sender;
	private Map<String,Boolean> localOverMap =
			new HashMap<String,Boolean>();
	
	/**
	 * 系统初始化
	 * */
	public void afterPropertiesSet() throws Exception {
		//获取所有选课信息，加入缓存
		ArrayList<Time> times=times=timeService.getTimeList();
		redisService.set(TimeKey.time_obj, "",times);
		ArrayList<Week> weeks=weeks=timeService.getWeekList();
		redisService.set(TimeKey.week_obj, "",weeks);
		//获取所有课程组里的课程id(不重复)
		ArrayList<String> courseIds=(ArrayList<String>) redisService.getList(CourseKey.course_id_list, "", String.class);
		if(courseIds!=null&&courseIds.size()>0) {
		}else {
			courseIds=courseGroupService.getCourseGroupCId();
			redisService.set(CourseKey.course_id_list, "",courseIds);
			for(String courseId:courseIds) {
				ArrayList<CourseGroup> courseGroups=courseGroupService.getCourseGroupByCId(courseId);
				if(courseGroups!=null&&courseGroups.size()>0) {
					redisService.set(CourseKey.course_group_id, courseId,courseGroups);
					for(CourseGroup courseGroup:courseGroups) {
						if(courseGroup.getCount()<=0) {
							log.info("该组人数为0："+courseGroup.getGroupId());
							localOverMap.put(courseGroup.getGroupId()+"", true);
						}
					}
				}
			}
		}
		//把所有的课程组的人数加入缓存，用来做预减预加处理
		ArrayList<CourseGroup> CourseGroupList=
				(ArrayList<CourseGroup>) redisService.getList(CourseKey.course_group_list, "", CourseGroup.class);
		if(CourseGroupList!=null&&CourseGroupList.size()>0) {
		}else {
			CourseGroupList=courseGroupService.getCourseGroupCount();
			redisService.set(CourseKey.course_group_list, "",CourseGroupList);
			for(CourseGroup courseGroup:CourseGroupList) {
				redisService.set(CourseKey.course_count_group, courseGroup.getGroupId()+"",courseGroup.getCount());
			}
		}
		//课程名称列(左边的课程列表)
		ArrayList<Course> courseList=(ArrayList<Course>) redisService.getList(CourseKey.course_name_list, "", Course.class);
		if(courseList==null||courseList.size()<=0) { 
			courseList=courseService.getCoursesLimit(546);
			redisService.set(CourseKey.course_name_list, "",courseList);
		 }
	}
	@RequestMapping("/start")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public String start(HttpServletRequest request,HttpServletResponse response,Model model) { 
		String html = redisService.get(HtmlKey.start, "start", String.class);
    	if(!StringUtils.isEmpty(html)) { //已缓存页面
    		return html;
    	}
    	SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
    	//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("start", ctx);
    	if(!StringUtils.isEmpty(html)) {
    		redisService.set(HtmlKey.start, "start", html);
    	}
    	return html;
	}
	
	@RequestMapping("/to_select_Course")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public String toSelectCourse(HttpServletRequest request,HttpServletResponse response,
			Model model,Student student) { 
		String html =getHtml(HtmlKey.select_course_html,"");
    	if(!StringUtils.isEmpty(html)) { //已缓存页面
    		log.info("已缓存页面,进入选课页面");
    		return html;
    	}
		model.addAttribute("student", student);
		//获取时间列表
		ArrayList<Time> times=(ArrayList<Time>)redisService.getList(TimeKey.time_obj, "", Time.class);
		if(times==null||times.size()<=0) { 
			times=timeService.getTimeList();
			redisService.set(TimeKey.time_obj, "",times);
    	}
		//System.out.println(times);
		model.addAttribute("times", times);
		//获取周期表
		ArrayList<Week> weeks=(ArrayList<Week>) redisService.getList(TimeKey.week_obj, "", Week.class);
		if(weeks==null||weeks.size()<=0) { 
			weeks=timeService.getWeekList();
			redisService.set(TimeKey.week_obj, "",weeks);
    	}
		model.addAttribute("weeks", weeks);
		//获取学生的先修信息
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) { 
			takes=takeService.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
	   }
		model.addAttribute("takes", takes);
		//获取选课表信息列
		ArrayList<Course> courseList=(ArrayList<Course>) redisService.getList(CourseKey.course_name_list, "", Course.class);
		if(courseList==null||courseList.size()<=0) { 
			courseList=courseService.getCoursesLimit(546);
			redisService.set(CourseKey.course_name_list, "",courseList);
    	}
		model.addAttribute("courseList", courseList);
		html =setHtml(request,response,model,HtmlKey.select_course_html,"","selectCourse");
		return html;
	}
	
	@RequestMapping("/getStuCourse")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<String> getStuCourse(HttpServletRequest request,HttpServletResponse response,Model model,Student student) {
		
		model.addAttribute("sutdent", student);
		//获取时间列表
		ArrayList<Time> times=(ArrayList<Time>)redisService.getList(TimeKey.time_obj, "", Time.class);
		if(times==null||times.size()<=0) { 
			times=timeService.getTimeList();
			redisService.set(TimeKey.time_obj, "",times);
    	}
		//System.out.println(times);
		model.addAttribute("times", times);
		//获取周期表
		ArrayList<Week> weeks=(ArrayList<Week>) redisService.getList(TimeKey.week_obj, "", Week.class);
		if(weeks==null||weeks.size()<=0) { 
			weeks=timeService.getWeekList();
			redisService.set(TimeKey.week_obj, "",weeks);
    	}
		model.addAttribute("weeks", weeks);
		//获取学生的先修信息
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) { 
			takes=takeService.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
	   }
		model.addAttribute("takes", takes);
		SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		String html = thymeleafViewResolver.getTemplateEngine().process("studentCourseTemplate", ctx);
		return Result.success(html);
	}
	
	@RequestMapping("/getCourseGroup")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<String> getCourseGroup(HttpServletRequest request,HttpServletResponse response
			,Model model,String courseId,Student student) {
		String html=null;
		//获取课程组，先查缓存
		ArrayList<CourseGroup> courseGroups=
			(ArrayList<CourseGroup>) redisService.getList(CourseKey.course_group_id, courseId,CourseGroup.class);
		if(courseGroups==null||courseGroups.size()<=0) {
			courseGroups=courseGroupService.getCourseGroupByCId(courseId);
			if(courseGroups!=null&&courseGroups.size()>0) {
				redisService.set(CourseKey.course_group_id, courseId,courseGroups);
			}
		}
		//先判断改课程是否已经被选完
		int num=courseGroups.size();
		for(CourseGroup courseGroup:courseGroups) {
			log.info("groupId:"+courseGroup.getGroupId());
			System.out.println(localOverMap.get(courseGroup.getGroupId()+""));
			if(localOverMap.get(courseGroup.getGroupId()+"")==null) {
				break;
			}else {
				if(localOverMap.get(courseGroup.getGroupId()+"")){
					num--;
				}
			}
		}
		log.info(num+"");
		if(num<=0) {   //课程全部组已被选完
			log.info("课程已被选完");
			html=getHtml(HtmlKey.over_html,courseId);
			if(!StringUtils.isEmpty(html)) { //已缓存页面
	    		return Result.success(html);
	    	}else {
	    		return Result.success(setHtml(request,response,model,HtmlKey.over_html,courseId,"courseOverTemplate"));
	    	}
		}
		System.out.println("66666666");
		//再判断学生有没有完成该课的必修课,true为完成了
		if(!takeService.checkStudentCourseBefore(student,courseId)) {
			log.info("你还没有完成该课程的先修课程");
			html=getHtml(HtmlKey.error_html,courseId);
			if(!StringUtils.isEmpty(html)) { //已缓存页面
	    		return Result.success(html);
	    	}else {
	    		return Result.success(setHtml(request,response,model,HtmlKey.error_html,courseId,"courseErrorTemplate"));
	    	}
		}
		html=getHtml(HtmlKey.course_html,courseId);
    	if(!StringUtils.isEmpty(html)) { //已缓存页面
    		return Result.success(html);
    	}
		//获取时间列表
		ArrayList<Time> times=(ArrayList<Time>)redisService.getList(TimeKey.time_obj, "", Time.class);
		if(times==null||times.size()<=0) {
			times=timeService.getTimeList();
			redisService.set(TimeKey.time_obj, "",times);
		}
		model.addAttribute("times", times);
		//获取周期表
		ArrayList<Week> weeks=(ArrayList<Week>) redisService.getList(TimeKey.week_obj, "", Week.class);
		if(weeks==null||weeks.size()<=0) { 
			weeks=timeService.getWeekList();
			redisService.set(TimeKey.week_obj, "",weeks);
		}
		model.addAttribute("weeks", weeks);
		
		model.addAttribute("courseGroups", courseGroups);
		
	    html =setHtml(request,response,model,HtmlKey.course_html,courseId,"courseGroupTemplate");
		return Result.success(html);
	}
	
	public String getHtml(HtmlKey htmlKey, String index) {
		String html= redisService.get(htmlKey, index, String.class);
		if(!StringUtils.isEmpty(html)) { //已缓存页面
    		return html;
    	}
		return null;
	}
	public String setHtml(HttpServletRequest request,HttpServletResponse response
			,Model model,HtmlKey htmlKey, String index,String htmlName) {
		SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		String html = thymeleafViewResolver.getTemplateEngine().process(htmlName, ctx);
		 if(!StringUtils.isEmpty(html)) {
		    	redisService.set(htmlKey, index,html);
		    	return html;
		 }
		return null;
	}
	
	@PostMapping("/do_select_Course")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<String> doSelectCourse(@RequestParam("groupId") int groupId,Student student) {  
		if(student==null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		if(localOverMap.get(groupId+"")!=null) {
			if(localOverMap.get(groupId+"")) {
				return Result.error(CodeMsg.CREDITS_IS_FULL);
			}
		}
		//获取数据库Coursegroup信息，
		CourseGroup courseGroup=redisService.get(CourseKey.group, groupId+"", CourseGroup.class);
		if(courseGroup==null) {
			courseGroup=courseGroupService.getCourseGroupById(groupId);
			if(courseGroup==null) {
				return Result.error(CodeMsg.COURSE_NOT_EXIST);
			}
			redisService.set(CourseKey.group, groupId+"", courseGroup);
		}
		//查询学生所选修课，检查这门课有没有已修
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) {
			takes=takeService.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
		}
		if(takes!=null&&takes.size()>0) {
			//查询学生选修表是否存在
			for(Take take:takes) {
				if(take.getCourseGroup().getGroupId()==groupId) {
					//课程一样，组号也一样，返回给用户不提示也不做任何操作
					return Result.error(CodeMsg.COURSE_IS_SELECT);
				}
				if(take.getCourseId().equals(courseGroup.getCourse().getCourseId())) { //已选
					//throw new GlobalException(CodeMsg.COURSE_IS_SELECT);
					System.out.println("调换课程");
					return Result.error(CodeMsg.COURSE_CHANGE);
				}
			}
		}
		//查看学生可用学分够不够
		if(courseGroup.getCourse().getNeedCredits()>student.getMayCredits()) {
			return Result.error(CodeMsg.CREDITS_INSUFFICIENT);
		}
		//判断时间是否有冲突
		if(checkConflictTime(courseGroup,student,courseGroup.getGroupId())) {
			return Result.error(CodeMsg.COURSE_IS_CONFLICT);
		}
		
		//选课成功,欲减库存
		long count=redisService.decr(CourseKey.course_count_group, groupId+"");
		if(count<=0) {
			//该组选课已满
			log.info("课程预减后已满：count："+count);
			localOverMap.put(courseGroup.getGroupId()+"", true);
		}
		//发送消息
		DoSelect DoSelect=new DoSelect(courseGroup.getGroupId(),courseGroup.getCourse(),student);
		redisService.set(CourseKey.select_success,groupId+student.getStuNo()+"",0);
		sender.sendDoSelect(DoSelect);
		return Result.success(courseGroup.getCourse().getCourseId());//返回给用户一个课程id，方便后面轮询参数使用
	}
	

	private boolean checkConflictTime(CourseGroup courseGroup,Student student,int excludeGroupId) {
		CourseGroupTime courseGroupTimeBig=new CourseGroupTime();
		courseGroupTimeBig.setTime(courseGroup.getBigCourseTime().getTimeId());
		courseGroupTimeBig.setWeek(courseGroup.getBigCourseWeek().getWeekId());
		courseGroupTimeBig.setStuNo(student.getStuNo());
		courseGroupTimeBig.setExcludeGroupId(excludeGroupId);
		CourseGroupTime courseGroupTimeSmall=new CourseGroupTime();
		courseGroupTimeSmall.setTime(courseGroup.getSmallCourseTime().getTimeId());
		courseGroupTimeSmall.setWeek(courseGroup.getSmallCourseWeek().getWeekId());
		courseGroupTimeSmall.setStuNo(student.getStuNo());
		courseGroupTimeSmall.setExcludeGroupId(excludeGroupId);
		List<Integer> deadlineBig=takeService.checkCourseTimeRepeat(courseGroupTimeBig); //获取是否由相同时间的期限列，0为没有
		List<Integer> deadlineSmall=takeService.checkCourseTimeRepeat(courseGroupTimeSmall);
		if(deadlineBig.size()>0) {
			for(int i:deadlineBig) {
				if(takeService.checkDeadlineRepeat(i,courseGroup.getBigCourseDeadline().getDeadlineId())) {
					System.out.println("课程冲突！！！！！！");
					return true;  //true为是冲突
				}
			}
		}
		if(deadlineSmall.size()>0) {
			for(int j:deadlineSmall) {
				if(takeService.checkDeadlineRepeat(j,courseGroup.getSmallCourseDeadline().getDeadlineId())) {
					System.out.println("课程冲突！！！！！！");
					return true;
				}
			}
		}
		return false;
	}

	@PostMapping("/do_change_Course")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<RedisSynVo> doChangeCourse(@RequestParam("groupId") int groupId,Student student) {
		if(student==null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		CourseGroup courseGroup=redisService.get(CourseKey.group, groupId+"", CourseGroup.class);
		if(courseGroup==null) {
			courseGroup=courseGroupService.getCourseGroupById(groupId);
			if(courseGroup==null) {
				return Result.error(CodeMsg.COURSE_NOT_EXIST);
			}
			redisService.set(CourseKey.group, groupId+"", courseGroup);
		}
		
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) {
			takes=takeService.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
		}
		int oldGroupId=0;
		if(takes!=null&&takes.size()>0) {
			//需要查到旧的课程的课程id和组
			for(Take take:takes) {
				if(take.getCourseId().equals(courseGroup.getCourse().getCourseId())) { 
					oldGroupId=take.getCourseGroup().getGroupId();
					System.out.println("找到旧的课程组oldGroupId："+oldGroupId);
				}
			}
		}
		//判断时间是否有冲突
		if(checkConflictTime(courseGroup,student,oldGroupId)) {
			return Result.error(CodeMsg.COURSE_IS_CONFLICT);
		}
		if(takeService.changeCourse(groupId,oldGroupId, student,courseGroup.getCourse())) {
			RedisSynVo redisSynVo=new RedisSynVo(groupId,courseGroup.getCourse().getCourseId(),student);
			//获取学生新信息
			return Result.success(redisSynVo);
		}
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	@PostMapping("/do_cancel_select")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<Double> cancelSelect(@RequestParam("groupId") int groupId
			,@RequestParam("courseId") String courseId,Student student) {
		if(student==null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//如果没有选择这门课，直接返回错误
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) {
			takes=takeService.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
		}
		if(takes!=null&&takes.size()>0) {
			boolean groupExit=false;
			for(Take take:takes) {
				if(take.getCourseGroup().getGroupId()==groupId) {
					groupExit=true;
					break;
				}
			}
			if(!groupExit) {
				return Result.error(CodeMsg.COURSE_NOT_EXIST);
			}
		}else {
			return Result.error(CodeMsg.COURSE_NOT_EXIST);
		}
		//开始退课
		if(takeService.cancleCourse(groupId,student,false)) {
			//加库存
			int count=redisService.get(CourseKey.course_count_group, groupId+"",Integer.class);
			if(count<0) {
				redisService.set(CourseKey.course_count_group, groupId+"",0);
			}
			redisService.incr(CourseKey.course_count_group, groupId+"");
			if(localOverMap.get(groupId+"")!=null) {
				if(localOverMap.get(groupId+"")) {
					localOverMap.put(groupId+"", false);
				}
			}
			CourseGroup courseGroup=redisService.get(CourseKey.group, groupId+"", CourseGroup.class);
			if(courseGroup==null) {
				courseGroup=courseGroupService.getCourseGroupById(groupId);
				if(courseGroup==null) {
					return Result.error(CodeMsg.COURSE_NOT_EXIST);
				}
				redisService.set(CourseKey.group, groupId+"", courseGroup);
			}
			//获取学生新信息
			Student stu=studentService.getStudentByNo(student.getStuNo());
			log.info(stu.getStuName()+"退课成功");
			return Result.success(stu.getMayCredits()); 
		}
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	

	/*
	 * -1失败
	 * 0继续轮询
	 * 1成功
	 */
	@GetMapping("/check_success")
	@AccessLimit(needLogin=true)
	@ResponseBody
	public Result<SelectSuccessVo> checkSuccess(int groupId,String courseId,Student student) {
		int result=-1;
		if(redisService.exists(CourseKey.select_success,groupId+student.getStuNo()+"")) {
			result=redisService.get(CourseKey.select_success,groupId+student.getStuNo()+"",Integer.class);
		}
		if(result>0) {
			System.out.println("redis中查询成功！！！！！！");
			//获取学生新信息
			Student stu=studentService.getStudentByNo(student.getStuNo());
			return Result.success(new SelectSuccessVo(1,stu.getMayCredits())); 
		}else if(result==0) {
			return Result.success(new SelectSuccessVo(0)); 
		}else if(localOverMap.get(groupId+"")!=null){
			if(localOverMap.get(groupId+"")){
				return Result.success(new SelectSuccessVo(-1)); 
			}
		}
		return Result.success(new SelectSuccessVo(0)); 
	}
}
