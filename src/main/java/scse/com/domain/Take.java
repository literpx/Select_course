package scse.com.domain;

public class Take {
	private int takeId;
	private int stuNo;
	private String courseId;
	private CourseGroup courseGroup;
	public int getTakeId() {
		return takeId;
	}
	public void setTakeId(int takeId) {
		this.takeId = takeId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public int getStuNo() {
		return stuNo;
	}
	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}
	public CourseGroup getCourseGroup() {
		return courseGroup;
	}
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
	@Override
	public String toString() {
		return "Take [takeId=" + takeId + ", stuNo=" + stuNo + ", courseId=" + courseId + ", courseGroup=" + courseGroup
				+ "]";
	}
}
