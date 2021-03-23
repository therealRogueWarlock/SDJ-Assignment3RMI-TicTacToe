package client.networking;

import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.util.Util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements Client, ClientCallback {

    private RMIServer ticTacToeGameServer;
    private String clientName;
    private PropertyChangeSupport support;


    @Override
    public void start() {
        try
        {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ticTacToeGameServer = (RMIServer) registry.lookup(Util.SERVERNAME);
        }
        catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message message) {
        ticTacToeGameServer.addMessage(message);
    }

    @Override
    public void joinGame(int roomId) {
        ticTacToeGameServer.joinGameRoom(roomId, clientName);
    }

    @Override
    public void hostGame() {
        ticTacToeGameServer.createGameRoom(clientName);

    }

    @Override
    public void update() {
        ticTacToeGameServer.getServerDate();
    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public void setClientName(String name) {
        clientName = name;
    }

    @Override
    public String getName() {
        return clientName;
    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName,listener);

    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName,listener);
    }
}
