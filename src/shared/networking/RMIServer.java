package shared.networking;

import server.model.lobbymodel.ServerLobbyModel;
import shared.transferobjects.Message;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote {


    void startServer() throws RemoteException, AlreadyBoundException;

    void loginPlayer(String playerName) throws RemoteException;

    void createGameRoom(String playerName) throws RemoteException;

    void joinGameRoom(int roomId, String playerName) throws RemoteException;

    void addMessage(Message message) throws RemoteException;

    void broadcast() throws RemoteException;

    void getServerDate() throws RemoteException;

    ServerLobbyModel getServerLobbyModel() throws RemoteException;

    void run() throws RemoteException;


}
