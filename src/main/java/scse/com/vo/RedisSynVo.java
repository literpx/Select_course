package scse.com.vo;

import scse.com.domain.Student;

public class RedisSynVo {
	private int groupId;
	private String courseId;
	private Student student;
	public RedisSynVo(int groupId,String courseId,Student student) {
		this.groupId=groupId;
		this.courseId=courseId;
		this.student=student;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
}
