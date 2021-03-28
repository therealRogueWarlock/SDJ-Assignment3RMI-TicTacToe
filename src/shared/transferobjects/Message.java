package shared.transferobjects;

import java.io.Serializable;

public class Message implements Serializable {
	private String name;
	private String message;
	private String targetName;
	private int targetRoomId;

	public Message(String msg) {
		this.name = null;
		message = msg;
		targetName = "Lobby";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTarget(String target) {
		targetName = target;
	}

	public void setTargetRoomId(int targetId){
		this.targetRoomId =targetId;
	}

	public int getTargetRoomId() {
		return targetRoomId;
	}

	public String getName() {
		return name;
	}

	public String getStringMessage() {
		return message;
	}

	public String getTarget() {
		return targetName;
	}

	@Override
	public String toString() {
		return name+ ": " + message;
	}
}
