package scse.com.vo;

public class TakeVo {
	private int takeId;
	private GroupVo groupVo;
	public int getTakeId() {
		return takeId;
	}
	public void setTakeId(int takeId) {
		this.takeId = takeId;
	}
	public GroupVo getGroupVo() {
		return groupVo;
	}
	public void setGroupVo(GroupVo groupVo) {
		this.groupVo = groupVo;
	}
	@Override
	public String toString() {
		return "TakeVo [takeId=" + takeId + ", groupVo=" + groupVo + "]";
	}
}
