package scse.com.vo;

import scse.com.domain.CourseGroup;
import scse.com.domain.in.CourseGroupIn;

public class GroupVo extends CourseGroupIn{
	private double needCredits;
	private String bigCourseTeacherName;
	private String smallCourseTeacherName;
	private String bigCourseDeadlineDesc;
	private String smallCourseDeadlineDesc;
	public GroupVo(CourseGroup courseGroup) { //输出到前端转化为简单的vo
	}
	public double getNeedCredits() {
		return needCredits;
	}
	public void setNeedCredits(double needCredits) {
		this.needCredits = needCredits;
	}
	public String getBigCourseTeacherName() {
		return bigCourseTeacherName;
	}
	public void setBigCourseTeacherName(String bigCourseTeacherName) {
		this.bigCourseTeacherName = bigCourseTeacherName;
	}
	public String getSmallCourseTeacherName() {
		return smallCourseTeacherName;
	}
	public void setSmallCourseTeacherName(String smallCourseTeacherName) {
		this.smallCourseTeacherName = smallCourseTeacherName;
	}
	public String getBigCourseDeadlineDesc() {
		return bigCourseDeadlineDesc;
	}
	public void setBigCourseDeadlineDesc(String bigCourseDeadlineDesc) {
		this.bigCourseDeadlineDesc = bigCourseDeadlineDesc;
	}
	public String getSmallCourseDeadlineDesc() {
		return smallCourseDeadlineDesc;
	}
	public void setSmallCourseDeadlineDesc(String smallCourseDeadlineDesc) {
		this.smallCourseDeadlineDesc = smallCourseDeadlineDesc;
	}
	@Override
	public String toString() {
		return "GroupVo [needCredits=" + needCredits + ", bigCourseTeacherName=" + bigCourseTeacherName
				+ ", smallCourseTeacherName=" + smallCourseTeacherName + ", bigCourseDeadlineDesc="
				+ bigCourseDeadlineDesc + ", smallCourseDeadlineDesc=" + smallCourseDeadlineDesc + "]";
	}
}
