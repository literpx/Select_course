package scse.com.domain;

import java.util.Date;

public class Time {
	private int timeId;
	private Date begin;
	private Date end;
	private String desc;
	public int getTimeId() {
		return timeId;
	}
	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "Time [timeId=" + timeId + ", begin=" + begin + ", end=" + end + ", desc=" + desc + "]";
	}
}
