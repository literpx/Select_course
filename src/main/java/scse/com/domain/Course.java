package scse.com.domain;

public class Course {
	private String courseId;
	private String courseName;
	private double needCredits;
	private String assess;
	private String beforeCourse;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public double getNeedCredits() {
		return needCredits;
	}
	public void setNeedCredits(double needCredits) {
		this.needCredits = needCredits;
	}
	public String getAssess() {
		return assess;
	}
	public void setAssess(String assess) {
		this.assess = assess;
	}
	public String getBeforeCourse() {
		return beforeCourse;
	}
	public void setBeforeCourse(String beforeCourse) {
		this.beforeCourse = beforeCourse;
	}
	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", courseName=" + courseName + ", needCredits=" + needCredits
				+ ", assess=" + assess + ", beforeCourse=" + beforeCourse + "]";
	}
}
