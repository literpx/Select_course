package scse.com.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import scse.com.domain.Classroom;
import scse.com.domain.Course;
import scse.com.domain.Deadline;
import scse.com.domain.Teacher;
import scse.com.domain.Time;
import scse.com.domain.Week;
import scse.com.domain.in.CourseGroupIn;
import scse.com.domain.in.CourseGroupTime;
import scse.com.mapper.CourseGroupMapper;
import scse.com.mapper.CourseMapper;

@Service
public class CourseService {
	
	@Autowired
	private CourseMapper courseDao;
	@Autowired
	private TimeService timeService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseGroupService courseGroupService;
	
	public ArrayList<Course> getCoursesLimit(int limit) {
		return courseDao.getCoursesLimit(limit);
	}
	
	public Course getCourseById(String courseId) {
		return courseDao.getCourseById(courseId);
	}
	
}
