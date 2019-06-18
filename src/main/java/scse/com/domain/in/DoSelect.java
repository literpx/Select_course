package scse.com.domain.in;

import scse.com.domain.Course;
import scse.com.domain.Student;

public class DoSelect {
	private int groupId;
	private Course course;
	private Student student;
	public DoSelect(int groupId,Course course,Student student) {
		this.groupId=groupId;
		this.course=course;
		this.student=student;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "DoSelect [groupId=" + groupId + ", course=" + course + ", student=" + student + "]";
	}
}
