package scse.com.domain.in;

public class TakeIn {
	private int takeId;
	private int stuNo;
	private int courseGroupId;
	private String courseId;
	public int getTakeId() {
		return takeId;
	}
	public void setTakeId(int takeId) {
		this.takeId = takeId;
	}
	public int getStuNo() {
		return stuNo;
	}
	public void setStuNo(int stuNo) {
		this.stuNo = stuNo;
	}
	public int getCourseGroupId() {
		return courseGroupId;
	}
	public void setCourseGroupId(int courseGroupId) {
		this.courseGroupId = courseGroupId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	@Override
	public String toString() {
		return "TakeIn [takeId=" + takeId + ", stuNo=" + stuNo + ", courseId=" + courseId + ", courseGroupId="
				+ courseGroupId + "]";
	}
}
