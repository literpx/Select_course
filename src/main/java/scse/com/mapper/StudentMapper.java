package scse.com.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import scse.com.domain.Student;

@Mapper
public interface StudentMapper {
	
	@Select("select * from tb_student where stu_no=#{stuNo} limit 1")
	public Student getStudentByNo(@Param("stuNo") int stuNo);
	
	@Select("select * from tb_student")
	public ArrayList<Student> getStudentsList();
	
	public int updateStudent(Student student);
	
	@Insert("insert into tb_student(stu_no,password,salt,stu_name,major,grade,have_credits,may_credits)"
			+ " values(#{stuNo},#{password},#{salt},#{stuName},#{major},#{grade},#{haveCredits},#{mayCredits})")
	public int addStudent(Student student);
}
