package client.networking;

import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.transferobjects.ServerData;
import shared.transferobjects.TicTacToePiece;
import shared.util.Util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient implements Client, ClientCallback, PropertyChangeListener {

    private RMIServer ticTacToeGameServer;
    private String clientName;
    private PropertyChangeSupport support;

    @Override
    public void start() {
        try
        {

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ticTacToeGameServer = (RMIServer) registry.lookup(Util.SERVERNAME);
            UnicastRemoteObject.exportObject(this, 0);
            ticTacToeGameServer.registerListener(this);

            support  = new PropertyChangeSupport(this);

        }
        catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message message) {
        try {
            ticTacToeGameServer.addMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinGame(int roomId) {

        try {
            ticTacToeGameServer.joinGameRoom(this, roomId, clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hostGame() {

        try {
            ticTacToeGameServer.host(this, clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        try {
             ServerData serverData  = ticTacToeGameServer.getServerDate();
             iChanged(serverData.getType(), serverData);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placePiece(TicTacToePiece ticTacToePiece) {

    }

    @Override
    public void updated() {

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

    private void iChanged(String eventType, Object newValue) {
        System.out.println("ServerLobby model detect change, fire change");
        support.firePropertyChange(eventType, null, newValue);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        iChanged(evt.getPropertyName(),evt.getNewValue());
    }
}
