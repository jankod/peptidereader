package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.util.LinkedList;

public class RemoteProgressStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Poruke sa servera o progresu
	 */
	private LinkedList<String> messages = new LinkedList<String>();

	private LinkedList<String> errMessages = new LinkedList<String>();

	private TaskStatus status = TaskStatus.NOT_RUNNING;

	/**
	 * Status, dali jos vrti ili ne
	 */
	public static enum TaskStatus {
		NOT_RUNNING, RUNNING, FINISH_OK, FINISH_BAD;

		public boolean isFinish() {
			if (this == FINISH_BAD || this == FINISH_OK) {
				return true;
			}

			return false;
		}
	}

	public void setMessages(LinkedList<String> messages) {
		this.messages = messages;
	}

	public LinkedList<String> getMessages() {
		return messages;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setErrMessages(LinkedList<String> errMessages) {
		this.errMessages = errMessages;
	}

	public LinkedList<String> getErrMessages() {
		return errMessages;
	}

}
