package hr.semgen.masena.share;

import java.io.Serializable;

public abstract class ReturnRemoteObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private Status status;
	private String errMsg;
	private String userMsg;

	public static enum Status {
		OK, ERROR
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}

	public String getUserMsg() {
		return userMsg;
	}
}
