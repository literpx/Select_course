package scse.com.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import scse.com.domain.CourseGroup;
import scse.com.domain.Take;
import scse.com.domain.in.CourseGroupTime;
import scse.com.domain.in.TakeIn;
@Mapper
public interface TakeMapper {
	
	//获取学生的选修表
	@Select("select * from tb_take where stu_no=#{stuNo}")
	@Results({
		@Result(id=true,column="take_id",property="takeId"),
		@Result(column="stu_no",property="stuNo"),
		@Result(column="course_id",property="courseId"),
		@Result(column="course_group_id",property="courseGroup",javaType=CourseGroup.class
		,one=@One(select="scse.com.mapper.CourseGroupMapper.getCourseGroupById")) 
	})
	public ArrayList<Take> getTakeByStuNo(int stuNo);
	
	@Select("select * from tb_take where stu_no=#{stuNo}")
	public ArrayList<TakeIn> getTakeInByStuNo(int stuNo);
	
	//获取学生对应课程组的选修表
	@Select("select IFNULL(max(take_id),0) As take_id from tb_take where stu_no=#{stuNo} and course_group_id=#{groupId}")
	public int getTakeIdByStuNoAndGroupId(@Param("stuNo")int stuNo,@Param("groupId")int groupId);
	
	@Select("select if(g.big_course_time=#{time}  and g.big_course_week=#{week}"
			+ ",g.small_course_deadline,g.big_course_deadline) As deadline "
			+ "from tb_take t right JOIN tb_course_group g on t.course_group_id=g.group_id"
			+ " where g.big_course_time=#{time}  and g.big_course_week=#{week} and t.stu_no=#{stuNo} "
			+ "and g.group_id<>#{excludeGroupId} or g.group_id<>#{excludeGroupId} and "
			+ " g.small_course_time=#{time} and g.small_course_week=#{week} and t.stu_no=#{stuNo}")
	public List<Integer> checkCourseTimeRepeat(CourseGroupTime courseGroupTime);
	
	
	@Insert("insert into tb_take(stu_no,course_group_id,course_id) "
			+ "values(#{stuNo},#{courseGroupId},#{courseId})")
	@SelectKey(keyColumn="take_id", keyProperty="takeId", resultType=Integer.class, before=false, statement="select last_insert_id()")
	public int addTake(TakeIn takeIn);

	@Delete("delete from tb_take")
	public int deleteTable();
	@Delete("delete from tb_take where course_group_id=#{groupId} and stu_no=#{stuNo}")
	public int deleteStuTake(@Param("groupId")int groupId,@Param("stuNo")int stuNo);
}
