package scse.com.vo;

public class SelectSuccessVo {
	private int status;
	private double mayCredits;
	public SelectSuccessVo() {
	}
	public SelectSuccessVo(int status) {
		this.status=status;
	}
	public SelectSuccessVo(int status,double mayCredits) {
		this.status=status;
		this.mayCredits=mayCredits;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getMayCredits() {
		return mayCredits;
	}
	public void setMayCredits(double mayCredits) {
		this.mayCredits = mayCredits;
	}
}
