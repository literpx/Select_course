package scse.com.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import scse.com.domain.Course;
import scse.com.domain.CourseGroup;
import scse.com.domain.Deadline;
import scse.com.domain.Take;
import scse.com.domain.Teacher;
import scse.com.domain.Time;
import scse.com.domain.Week;
import scse.com.domain.in.CourseGroupIn;
import scse.com.domain.in.CourseGroupTime;

@Mapper
public interface CourseGroupMapper {
	//添加课程组表
	@Insert("insert into tb_course_group(course_id,group_name,count,big_course_teacher,big_course_time"
		+ ",big_course_week,big_course_deadline,small_course_teacher,small_course_time,small_course_week,"
		+ "small_course_deadline) values(#{courseId},#{groupName},#{count},#{bigCourseTeacher}"
		+ ",#{bigCourseTime},#{bigCourseWeek},#{bigCourseDeadline},#{smallCourseTeacher}"
		+ ",#{smallCourseTime},#{smallCourseWeek},#{smallCourseDeadline})")
	public int addCourseGroup(CourseGroupIn courseGroupIn);
	
	//获取课程组
	@Select("select * from tb_course_group where group_id=#{id} limit 1")
	@Results({
		@Result(id=true,column="group_id",property="groupId"),
		@Result(column="group_name",property="groupName"),
		@Result(column="count",property="count"),
		@Result(column="course_id",property="course",javaType=Course.class
		,one=@One(select="scse.com.mapper.CourseMapper.getCourseById")), 
		
		@Result(column="big_course_teacher",property="bigCourseTeacher",javaType=Teacher.class
		,one=@One(select="scse.com.mapper.TeacherMapper.getTeacherByNo")), 
		@Result(column="big_course_time",property="bigCourseTime",javaType=Time.class
		,one=@One(select="scse.com.mapper.TimeMapper.getTimeById")), 
		@Result(column="big_course_week",property="bigCourseWeek",javaType=Week.class
		,one=@One(select="scse.com.mapper.TimeMapper.getWeekById")), 
		@Result(column="big_course_deadline",property="bigCourseDeadline",javaType=Deadline.class
		,one=@One(select="scse.com.mapper.TimeMapper.getDeadlineById")),
		
		@Result(column="small_course_teacher",property="smallCourseTeacher",javaType=Teacher.class
		,one=@One(select="scse.com.mapper.TeacherMapper.getTeacherByNo")), 
		@Result(column="small_course_time",property="smallCourseTime",javaType=Time.class
		,one=@One(select="scse.com.mapper.TimeMapper.getTimeById")), 
		@Result(column="small_course_week",property="smallCourseWeek",javaType=Week.class
		,one=@One(select="scse.com.mapper.TimeMapper.getWeekById")), 
		@Result(column="small_course_deadline",property="smallCourseDeadline",javaType=Deadline.class
		,one=@One(select="scse.com.mapper.TimeMapper.getDeadlineById")),
	})
	public CourseGroup getCourseGroupById(int id);
	
	@Select("select course_id from tb_course_group where group_id=#{groupId} limit 1")
	public String getcIdBygId(@Param("groupId") int groupId);
	
	@Select("select * from tb_course_group where group_id=#{id} limit 1")
	public CourseGroupIn getCourseGroupInById(int id);
	
	//同一课程组中查找是否存在同一时间段
	@Select("select IFNULL(max(group_id),0) As group_id from tb_course_group where big_course_time=#{time} "
			+ "and big_course_week=#{week} and big_course_deadline=#{deadline} or"
			+ " small_course_time=#{time} and small_course_week=#{week} and"
			+ " small_course_deadline=#{deadline}")
	public int checkCourseRepeat(CourseGroupTime courseGroupTime);
	
	//按组获取课程编号
	@Select("select course_id from tb_course_group GROUP BY course_id")
	public ArrayList<String> getCourseGroupCId();
	
	//由课程编号搜索相对应的组号
	@Select("select group_id from tb_course_group where course_id=#{courseId}")
	public ArrayList<Integer> getCourseGroupIdByCId(String courseId);
	
	//获取组号和人数
	@Select("select group_id,count from tb_course_group")
	public ArrayList<CourseGroup> getCourseGroupCount();
	//增加课程组的人数
	@Update("update tb_course_group set count=count+1 where group_id=#{groupId}")//这个高并发可能出bug
	public int incCourseGroupCount(int groupId);
	//减少课程组的人数
	@Update("update tb_course_group set count=count-1 where group_id=#{groupId} and count>0")
	public int recCourseGroupCount(int groupId);

	//由课程号获取对应课程的组
	@Select("select * from tb_course_group where course_id=#{courseId}")
	@Results({
		@Result(id=true,column="group_id",property="groupId"),
		@Result(column="group_name",property="groupName"),
		@Result(column="count",property="count"),
		@Result(column="course_id",property="course",javaType=Course.class
		,one=@One(select="scse.com.mapper.CourseMapper.getCourseById")), 
		
		@Result(column="big_course_teacher",property="bigCourseTeacher",javaType=Teacher.class
		,one=@One(select="scse.com.mapper.TeacherMapper.getTeacherByNo")), 
		@Result(column="big_course_time",property="bigCourseTime",javaType=Time.class
		,one=@One(select="scse.com.mapper.TimeMapper.getTimeById")), 
		@Result(column="big_course_week",property="bigCourseWeek",javaType=Week.class
		,one=@One(select="scse.com.mapper.TimeMapper.getWeekById")), 
		@Result(column="big_course_deadline",property="bigCourseDeadline",javaType=Deadline.class
		,one=@One(select="scse.com.mapper.TimeMapper.getDeadlineById")),
		
		@Result(column="small_course_teacher",property="smallCourseTeacher",javaType=Teacher.class
		,one=@One(select="scse.com.mapper.TeacherMapper.getTeacherByNo")), 
		@Result(column="small_course_time",property="smallCourseTime",javaType=Time.class
		,one=@One(select="scse.com.mapper.TimeMapper.getTimeById")), 
		@Result(column="small_course_week",property="smallCourseWeek",javaType=Week.class
		,one=@One(select="scse.com.mapper.TimeMapper.getWeekById")), 
		@Result(column="small_course_deadline",property="smallCourseDeadline",javaType=Deadline.class
		,one=@One(select="scse.com.mapper.TimeMapper.getDeadlineById")),
	})
	public ArrayList<CourseGroup> getCourseGroupByCId(String courseId);

	@Delete("delete from tb_take")
	public void deleteTable();
}
