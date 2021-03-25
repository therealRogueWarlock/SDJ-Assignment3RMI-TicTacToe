package shared.networking;

import client.networking.RMIClient;
import server.model.lobbymodel.ServerLobbyModel;
import shared.transferobjects.Message;
import shared.transferobjects.ServerData;

import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote {


    void startServer() throws RemoteException, AlreadyBoundException;

    void registerListener(ClientCallback listener) throws RemoteException;

    void loginPlayer(String playerName) throws RemoteException;

    void host(PropertyChangeListener listener, String playerName) throws RemoteException;

    void joinGameRoom(PropertyChangeListener listener, int roomId, String playerName) throws RemoteException;

    void addMessage(Message message) throws RemoteException;

    void broadcast() throws RemoteException;

    ServerData getServerDate() throws RemoteException;

    ServerLobbyModel getServerLobbyModel() throws RemoteException;

    void removeListener(ClientCallback client) throws RemoteException;
}
