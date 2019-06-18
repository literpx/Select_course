package scse.com.domain;

public class Deadline {
	private int deadlineId;
	private String deadline;
	public int getDeadlineId() {
		return deadlineId;
	}
	public void setDeadlineId(int deadlineId) {
		this.deadlineId = deadlineId;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	@Override
	public String toString() {
		return "Deadline [deadlineId=" + deadlineId + ", deadline=" + deadline + "]";
	}
}
