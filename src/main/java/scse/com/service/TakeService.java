package scse.com.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scse.com.access.StudentContext;
import scse.com.controller.CourseController;
import scse.com.domain.Course;
import scse.com.domain.CourseGroup;
import scse.com.domain.Student;
import scse.com.domain.Take;
import scse.com.domain.in.CourseGroupIn;
import scse.com.domain.in.CourseGroupTime;
import scse.com.domain.in.TakeIn;
import scse.com.exception.GlobalException;
import scse.com.mapper.TakeMapper;
import scse.com.rabbitmq.MQReceiver;
import scse.com.redis.CourseKey;
import scse.com.redis.HtmlKey;
import scse.com.redis.RedisService;
import scse.com.redis.StudentKey;
import scse.com.redis.TakeKey;
import scse.com.result.CodeMsg;
import scse.com.result.Result;

@Service
public class TakeService {
	private static Logger log = LoggerFactory.getLogger(TakeService.class);
	@Autowired
	private TakeMapper takeMapper;
	@Autowired
	private CourseGroupService courseGroupService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private StudentService studentService;
	
	public ArrayList<Take> getTakeByStuNo(int stuNo){
		return takeMapper.getTakeByStuNo(stuNo);
	};
	
	public boolean checkStudentCourseGroup(int stuNo,int groupId) {
		return takeMapper.getTakeIdByStuNoAndGroupId(stuNo, groupId)>0;
	};
	
	//检查所选课程与学生已修课程是否冲突
	public boolean checkStudentCourseTimes(int groupId,Student student) {
		return false;
		
		
	}
	//检查学生是否完成课程的先修课
	public boolean checkStudentCourseBefore(Student student,String courseId) {
		//获取学生的先修表
		ArrayList<Take> takes=(ArrayList<Take>) redisService.getList(TakeKey.stu_takes, student.getStuNo()+"", Take.class);
		if(takes==null||takes.size()<=0) {
			takes=this.getTakeByStuNo(student.getStuNo());
			redisService.set(TakeKey.stu_takes, student.getStuNo()+"", takes);
		}
		//获取课程信息，
		Course course= redisService.get(CourseKey.course_list, courseId, Course.class);
		if(course==null) {
			course=courseService.getCourseById(courseId);
			if(course==null) {
				//这里应该是抛异常，课程找不到
				return false;
			}
			redisService.set(CourseKey.course_list, courseId, course);
		}	
		return checkComplateBeforeCourse(course,takes);
	};
	private boolean checkComplateBeforeCourse(Course course, ArrayList<Take> takes) {
		log.info(course.toString());
		if(StringUtils.isBlank(course.getBeforeCourse().replace(" ", ""))||course.getBeforeCourse().equals(" ")) {
			return true;
		}
		int a=course.getBeforeCourse().indexOf("||");
		if(a>=0) {
			String[] beforeCourseOr=course.getBeforeCourse().split("\\|\\|");//获取先修课程
			log.info("遍历||切割后数组");
			for(String strOr:beforeCourseOr) {
				int b=strOr.indexOf("&&");
				if(b>=0) {
					String[] beforeCourseAndIn=strOr.split("\\&\\&");
					log.info("||切割后数组中有&&");
					int equalsNum=beforeCourseAndIn.length;
					//遍历学生已修课程与strAndIn对比，区别strAndIn匹配才行
					for(String strAndIn:beforeCourseAndIn) {
						if(checkEquals(takes,strAndIn)) {
							equalsNum--;
						}
					}
					if(equalsNum==0) {
						return true;
					}
				}else {
					log.info("||切割后数组中没有&&");
					//遍历学生已修课程与str对比，只要满足一个即可
					if(checkEquals(takes,strOr)) {
						return true;
					}
				}
			}
			return false;
		}else {
			//如果没有||，但能只有&&，例如123&&456&&789
			int c=course.getBeforeCourse().indexOf("&&");
			if(c>=0) {
				log.info("只有&&没有||");
				String[] beforeCourseAnd=course.getBeforeCourse().split("\\&\\&");
				int  equalsNum2=beforeCourseAnd.length;
				for(String strAnd:beforeCourseAnd) {
					if(checkEquals(takes,strAnd)) {
						equalsNum2--;
					}
				}
				if(equalsNum2==0) {
					return true;
				}
			}
		}
		if(checkEquals(takes,course.getBeforeCourse())) { //只有一个先修条件
			return true;
		}
		//即没有&&也没有||，即不需要选修课程
		return false;
	}

	private boolean checkEquals(ArrayList<Take> takes, String strAndIn) {
		for(Take take:takes) {
			if(take.getCourseId().equals(strAndIn)) {
				return true; //有一个匹配，即返回true
			}
		}
		return false;
	}

	public boolean checkDeadlineRepeat(int deadline,int checkDeadline) {
		//checkDeadline为想要选修的课程的期限
		if(deadline==0) {
			return false; 
		}
		if(deadline==6&&checkDeadline==7||checkDeadline==6&&deadline==7) {  
			return false;  
		}
		if(deadline==1&&checkDeadline==5||checkDeadline==1&&deadline==5) {  
			return false; 
		}
		if(deadline==4&&checkDeadline==5||checkDeadline==4&&deadline==5) {  
			return false;  
		}
		if(deadline==1&&checkDeadline==2||deadline==1&&checkDeadline==3||deadline==1&&checkDeadline==4) {  
			return false;  
		}
		if(checkDeadline==1&&deadline==2||checkDeadline==1&&deadline==3||checkDeadline==1&&deadline==4) {  
			return false;  
		}
		if(checkDeadline==3&&deadline==4||checkDeadline==4&&deadline==3) {  
			return false;  
		}
		return true;
	}

	public boolean deleteTable() {
		takeMapper.deleteTable();
		return true;
	}

	public int addTake(TakeIn takeIn) {
		return takeMapper.addTake(takeIn);
	}

	public ArrayList<TakeIn> getTakeInByStuNo(int stuNo) {
		return takeMapper.getTakeInByStuNo(stuNo);
	}

	public List<Integer> checkCourseTimeRepeat(CourseGroupTime courseGroupTimeBig) {
		return takeMapper.checkCourseTimeRepeat(courseGroupTimeBig);
	}
	
	@Transactional
	public boolean selectCourse(int groupId,Student student,Course course,boolean isChange) {
		//减少课程组人数
		if(!courseGroupService.recCourseGroupCount(groupId)) {
			return false;
		}
		if(!isChange) {
			//减少学生学分
			student.setMayCredits(student.getMayCredits()-course.getNeedCredits());
			if(!studentService.updateStudent(student)) {
				redisService.set(CourseKey.select_success,groupId+student.getStuNo()+"",-1);
				return false;
			}
			redisService.set(StudentKey.token, student.getSalt(), student);
		}
		//添加学生选修表
		TakeIn takeIn=new TakeIn();
		takeIn.setStuNo(student.getStuNo());
		takeIn.setCourseGroupId(groupId);
		takeIn.setCourseId(course.getCourseId());
		int takeId=takeMapper.addTake(takeIn);
		if(takeId>0) {
			log.info("选课成功，takes加入缓存,");
			redisService.set(CourseKey.select_success,groupId+student.getStuNo()+"",takeId);
			//reids同步
			redisSynInfo(student,course.getCourseId());
			return true;
		}
		log.info("takes没加入缓存");
		redisService.set(CourseKey.select_success,groupId+student.getStuNo()+"",-1);
		return false;
	}
	
	@Transactional
	public boolean cancleCourse(int groupId,Student student,boolean isChange) {
		//删除take表里学生该课的组，
		if(takeMapper.deleteStuTake(groupId,student.getStuNo())<=0) {
			return false;
		}
		CourseGroup courseGroup=redisService.get(CourseKey.group, groupId+"", CourseGroup.class);
		if(courseGroup==null) {
			courseGroup=courseGroupService.getCourseGroupById(groupId);
			if(courseGroup==null) {
				return false;
			}
			redisService.set(CourseKey.group, groupId+"", courseGroup);
		}
		if(!isChange) {
			//增加学生学分
			student.setMayCredits(student.getMayCredits()+courseGroup.getCourse().getNeedCredits());
			if(!studentService.updateStudent(student)) {
				return false;
			}
			redisService.set(StudentKey.token, student.getSalt(), student);
		}
		//增加课程组的人数
		if(!courseGroupService.incCourseGroupCount(courseGroup.getGroupId())) {
			return false;
		}
		//reids同步
		redisSynInfo(student,courseGroup.getCourse().getCourseId());
		return true;
	}

	@Transactional
	public boolean changeCourse(int newGroupId,int oldGroupId, Student student,Course course) {
		if(!this.cancleCourse(oldGroupId,student,true)){
			return false;
		}
		if(!this.selectCourse(newGroupId, student, course,true)){
			return false;
		}
		return true;
	}
	/*
	 * 学号跟课程id
	 */
	private void redisSynInfo(Student student, String courseId) {
		//redis同步学生选修表
		ArrayList<Take> newTakes=this.getTakeByStuNo(student.getStuNo());
		redisService.set(TakeKey.stu_takes,student.getStuNo()+"", newTakes);
		//redis同步选课表
		ArrayList<CourseGroup> courseGroups=courseGroupService.getCourseGroupByCId(courseId);
		if(courseGroups!=null&&courseGroups.size()>0) {
			redisService.set(CourseKey.course_group_id,courseId,courseGroups);
		}
		//删除缓存页面
		redisService.delete(HtmlKey.course_html, courseId);
		redisService.delete(HtmlKey.select_course_html,"");
	}
}
