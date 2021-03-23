package shared.util;

import shared.transferobjects.Message;

import java.beans.PropertyChangeListener;

public interface LobbyModel extends Subject {

	void join(PropertyChangeListener listener, int roomId, String playerName);

	void sendMessage(Message message);

}
