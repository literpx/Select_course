package scse.com.domain;

public class Week {
	
	private int weekId;
	private String week;
	public int getWeekId() {
		return weekId;
	}
	public void setWeekId(int weekId) {
		this.weekId = weekId;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	@Override
	public String toString() {
		return "Week [weekId=" + weekId + ", week=" + week + "]";
	}
}
