package scse.com.domain.in;

public class CourseGroupTime {
	private int stuNo;
	private int time;
	private int week;
	private int deadline;
	private int excludeGroupId;//排除检查的组id
	public CourseGroupTime() {
	}
	public CourseGroupTime(int time, int week, int deadline) {
		this.time=time;
		this.week=week;
		this.deadline=deadline;
	}
	
	public int getExcludeGroupId() {
		return excludeGroupId;
	}
	public void setExcludeGroupId(int excludeGroupId) {
		this.excludeGroupId = excludeGroupId;
	}
	public int getStuNo() {
		return stuNo;
	}
	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDeadline() {
		return deadline;
	}
	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}
	@Override
	public String toString() {
		return "CourseGroupTime [stuNo=" + stuNo + ", time=" + time + ", week=" + week + ", deadline=" + deadline
				+ ", excludeGroupId=" + excludeGroupId + "]";
	}
}
