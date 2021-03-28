package client.networking;

import shared.transferobjects.Message;
import shared.transferobjects.TicTacToePiece;
import shared.util.Subject;

public interface Client extends Subject {

	void start();

	void sendMessage(Message message);

	boolean joinGame(int roomId);

	void hostGame();

	void update();

	void placePiece(TicTacToePiece ticTacToePiece);

	void setClientName(String name);

    String getName();

    void quit();
}
