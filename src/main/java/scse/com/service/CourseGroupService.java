package scse.com.service;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scse.com.domain.CourseGroup;
import scse.com.domain.in.CourseGroupIn;
import scse.com.domain.in.CourseGroupTime;
import scse.com.mapper.CourseGroupMapper;
import scse.com.redis.CourseKey;
import scse.com.redis.RedisService;

@Service
public class CourseGroupService {

	@Autowired
	private CourseGroupMapper courseGroupMapper;
	@Autowired
	private RedisService redisService;
	
	public boolean checkCourseRepeat(CourseGroupTime courseGroupTime) {
		return courseGroupMapper.checkCourseRepeat(courseGroupTime)>0;
	}

	public void addCourseGroup(CourseGroupIn courseGroupIn) {
		courseGroupMapper.addCourseGroup(courseGroupIn);
	}
	
	//分组获得所有组里的不重复的课程id
	public ArrayList<String> getCourseGroupCId() {
		return courseGroupMapper.getCourseGroupCId();
	}
	
	public ArrayList<Integer> getCourseGroupIdByCId(String courseId) {
		return courseGroupMapper.getCourseGroupIdByCId(courseId);
	}
	//由课程id获取课程组
	public ArrayList<CourseGroup> getCourseGroupByCId(String courseId) {
		return courseGroupMapper.getCourseGroupByCId(courseId);
	}

	//由课程组id获取组信息
	public CourseGroup getCourseGroupById(int groupId) {
		return courseGroupMapper.getCourseGroupById(groupId);
	}
	
	//由课程组id获取组信息
	public CourseGroupIn getCourseGroupInById(int groupId) {
			return courseGroupMapper.getCourseGroupInById(groupId);
	}

	public boolean deleteTable() {
		 courseGroupMapper.deleteTable();
		 return true;
	}
	//获取组号和人数
	public ArrayList<CourseGroup> getCourseGroupCount(){
		return courseGroupMapper.getCourseGroupCount();
	};
	//增加课程组的人数
	public boolean incCourseGroupCount(int groupId) {
		return courseGroupMapper.incCourseGroupCount(groupId)>0;
	};
	//减少课程组的人数
	public boolean recCourseGroupCount(int groupId) {
		return courseGroupMapper.recCourseGroupCount(groupId)>0;
	}

	public String getcIdBygId(int groupId) {
		return courseGroupMapper.getcIdBygId(groupId);
	};
}
