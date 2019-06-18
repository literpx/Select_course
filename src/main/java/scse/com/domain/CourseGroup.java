package scse.com.domain;

public class CourseGroup {
	private int groupId;
	private String 	groupName;
	private	Course course; //外键关联，下同
	private	int count;
	
	private	Teacher bigCourseTeacher;
	private Time bigCourseTime;
	private Week bigCourseWeek;
	private Deadline bigCourseDeadline;
	
	private Teacher smallCourseTeacher;
	private Time smallCourseTime;
	private Week smallCourseWeek;
	private Deadline smallCourseDeadline;
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
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Teacher getBigCourseTeacher() {
		return bigCourseTeacher;
	}
	public void setBigCourseTeacher(Teacher bigCourseTeacher) {
		this.bigCourseTeacher = bigCourseTeacher;
	}
	public Time getBigCourseTime() {
		return bigCourseTime;
	}
	public void setBigCourseTime(Time bigCourseTime) {
		this.bigCourseTime = bigCourseTime;
	}
	public Week getBigCourseWeek() {
		return bigCourseWeek;
	}
	public void setBigCourseWeek(Week bigCourseWeek) {
		this.bigCourseWeek = bigCourseWeek;
	}
	public Deadline getBigCourseDeadline() {
		return bigCourseDeadline;
	}
	public void setBigCourseDeadline(Deadline bigCourseDeadline) {
		this.bigCourseDeadline = bigCourseDeadline;
	}
	public Teacher getSmallCourseTeacher() {
		return smallCourseTeacher;
	}
	public void setSmallCourseTeacher(Teacher smallCourseTeacher) {
		this.smallCourseTeacher = smallCourseTeacher;
	}
	public Time getSmallCourseTime() {
		return smallCourseTime;
	}
	public void setSmallCourseTime(Time smallCourseTime) {
		this.smallCourseTime = smallCourseTime;
	}
	public Week getSmallCourseWeek() {
		return smallCourseWeek;
	}
	public void setSmallCourseWeek(Week smallCourseWeek) {
		this.smallCourseWeek = smallCourseWeek;
	}
	public Deadline getSmallCourseDeadline() {
		return smallCourseDeadline;
	}
	public void setSmallCourseDeadline(Deadline smallCourseDeadline) {
		this.smallCourseDeadline = smallCourseDeadline;
	}
	@Override
	public String toString() {
		return "CourseGroup [groupId=" + groupId + ", groupName=" + groupName + ", course=" + course + ", count="
				+ count + ", bigCourseTeacher=" + bigCourseTeacher + ", bigCourseTime=" + bigCourseTime
				+ ", bigCourseWeek=" + bigCourseWeek + ", bigCourseDeadline=" + bigCourseDeadline
				+ ", smallCourseTeacher=" + smallCourseTeacher + ", smallCourseTime=" + smallCourseTime
				+ ", smallCourseWeek=" + smallCourseWeek + ", smallCourseDeadline=" + smallCourseDeadline + "]";
	}
}
