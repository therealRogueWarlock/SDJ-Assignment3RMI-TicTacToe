package server.model.chatmodel;

import shared.transferobjects.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatRoom implements Serializable {
	private ArrayList<Message> allMessages;

	public void addMessage(Message msg) {
		allMessages.add(msg);
	}

	public ChatRoom() {
		this.allMessages = new ArrayList<>();
	}

	public ArrayList<Message> getAllMessages() {
		return (ArrayList<Message>) allMessages.clone();
	}

}
