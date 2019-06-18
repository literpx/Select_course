package scse.com.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import scse.com.domain.Course;
import scse.com.domain.Take;


@Mapper
public interface CourseMapper {
	@Insert("insert into tb_course (course_id, course_name, need_credits,assess,before_course) "
			+ "values(#{courseId}, #{courseName},#{needCredits},#{assess},#{beforeCourse})")
	public  int insertCourse(Course course);

	@Select("select * from tb_course limit #{limit}")
	public ArrayList<Course> getCoursesLimit(int limit);
	
	@Select("select * from tb_course where course_id=#{courseId} limit 1")
	public Course getCourseById(String courseId);
	
	
}
