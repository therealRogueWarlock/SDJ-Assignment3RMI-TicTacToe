package shared.networking;

import server.model.lobbymodel.ServerLobbyModel;
import shared.transferobjects.Message;

import java.rmi.Remote;

public interface RMIServer extends Remote {


    void startServer();

    void loginPlayer(String playerName);

    void createGameRoom(String playerName);

    void joinGameRoom(int roomId, String playerName);

    void addMessage(Message message);

    void broadcast();

    void getServerDate();

    ServerLobbyModel getServerLobbyModel();

    void run();


}
