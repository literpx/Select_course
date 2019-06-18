package scse.com.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import scse.com.domain.Teacher;

@Mapper
public interface TeacherMapper {
	
	@Select("select * from tb_teacher ")
	public ArrayList<Teacher> getTeachers();
	
	@Select("select * from tb_teacher where teacher_no=#{teacherNo} limit 1")
	public Teacher getTeacherByNo(int teacherNo);
}
