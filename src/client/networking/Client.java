package client.networking;

import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.util.Subject;

public interface Client extends Subject {

	void start();

	void sendMessage(Message message);

	void joinGame(int roomId);

	void hostGame();

	void update();

	void setClientName(String name);

	void sendRequest(Request request);

    String getName();
}
