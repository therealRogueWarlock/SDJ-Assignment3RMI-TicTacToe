package shared.util;

import shared.transferobjects.Message;
import shared.transferobjects.TicTacToePiece;

import java.beans.PropertyChangeListener;

public interface GameRoomModel extends Subject {

	boolean join(PropertyChangeListener listener, String playerName);

	boolean placePiece(TicTacToePiece ticTacToePiece);

	void sendMessage(Message message);

	int getRoomId();

	String getPlayerCount();

	String getPlayerNames();

}
