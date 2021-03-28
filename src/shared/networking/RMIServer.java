package shared.networking;

import client.networking.RMIClient;
import server.model.lobbymodel.ServerLobbyModel;
import shared.transferobjects.Message;
import shared.transferobjects.ServerData;
import shared.transferobjects.TicTacToePiece;

import java.beans.PropertyChangeEvent;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote {


    void startServer() throws RemoteException, AlreadyBoundException;

    void registerListener(ClientCallback clientCallback) throws RemoteException;

    void loginPlayer(String playerName) throws RemoteException;

    boolean host(ClientCallback clientCallback, String playerName) throws RemoteException;

    boolean joinGameRoom(ClientCallback clientCallback, int roomId, String playerName) throws RemoteException;

    void addMessage(Message message) throws RemoteException;

    void broadcast(PropertyChangeEvent evt) throws RemoteException;

    ServerData getServerDate() throws RemoteException;

    ServerLobbyModel getServerLobbyModel() throws RemoteException;

    void removeListener(ClientCallback client) throws RemoteException;

    boolean placePiece(TicTacToePiece piece) throws RemoteException;
}
