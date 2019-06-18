package scse.com.domain.in;

public class CourseGroupIn {
	private int groupId;
	private String 	groupName;
	private	String courseId;
	private	int count;
	
	private	int bigCourseTeacher;
	private int bigCourseTime;
	private int bigCourseWeek;
	private int bigCourseDeadline;
	
	private int smallCourseTeacher;
	private int smallCourseTime;
	private int smallCourseWeek;
	private int smallCourseDeadline;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getBigCourseTeacher() {
		return bigCourseTeacher;
	}
	public void setBigCourseTeacher(int bigCourseTeacher) {
		this.bigCourseTeacher = bigCourseTeacher;
	}
	public int getBigCourseTime() {
		return bigCourseTime;
	}
	public void setBigCourseTime(int bigCourseTime) {
		this.bigCourseTime = bigCourseTime;
	}
	public int getBigCourseWeek() {
		return bigCourseWeek;
	}
	public void setBigCourseWeek(int bigCourseWeek) {
		this.bigCourseWeek = bigCourseWeek;
	}
	public int getBigCourseDeadline() {
		return bigCourseDeadline;
	}
	public void setBigCourseDeadline(int bigCourseDeadline) {
		this.bigCourseDeadline = bigCourseDeadline;
	}
	public int getSmallCourseTeacher() {
		return smallCourseTeacher;
	}
	public void setSmallCourseTeacher(int smallCourseTeacher) {
		this.smallCourseTeacher = smallCourseTeacher;
	}
	public int getSmallCourseTime() {
		return smallCourseTime;
	}
	public void setSmallCourseTime(int smallCourseTime) {
		this.smallCourseTime = smallCourseTime;
	}
	public int getSmallCourseWeek() {
		return smallCourseWeek;
	}
	public void setSmallCourseWeek(int smallCourseWeek) {
		this.smallCourseWeek = smallCourseWeek;
	}
	public int getSmallCourseDeadline() {
		return smallCourseDeadline;
	}
	public void setSmallCourseDeadline(int smallCourseDeadline) {
		this.smallCourseDeadline = smallCourseDeadline;
	}
	@Override
	public String toString() {
		return "CourseGroupIn [groupId=" + groupId + ", groupName=" + groupName + ", courseId=" + courseId + ", count="
				+ count + ", bigCourseTeacher=" + bigCourseTeacher + ", bigCourseTime=" + bigCourseTime
				+ ", bigCourseWeek=" + bigCourseWeek + ", bigCourseDeadline=" + bigCourseDeadline
				+ ", smallCourseTeacher=" + smallCourseTeacher + ", smallCourseTime=" + smallCourseTime
				+ ", smallCourseWeek=" + smallCourseWeek + ", smallCourseDeadline=" + smallCourseDeadline + "]";
	}
}
