package client.networking;

import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.transferobjects.Message;
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

public class RMIClient implements Client, ClientCallback {

    private RMIServer ticTacToeGameServer;
    private String clientName;
    private PropertyChangeSupport support;

    public RMIClient() {
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void start() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ticTacToeGameServer = (RMIServer) registry.lookup(Util.SERVERNAME);
            UnicastRemoteObject.exportObject(this, 0);
            ticTacToeGameServer.registerListener(this);
            System.out.println("Connecting to Server");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message message) {
        try {
            message.setName(clientName);
            ticTacToeGameServer.addMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hostGame() {
        try {
            return ticTacToeGameServer.host(this, clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean joinGame(int roomId) {
        try {
            return ticTacToeGameServer.joinGameRoom(this, roomId, clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update() {
        try {
            ServerData serverData = ticTacToeGameServer.getServerDate();
            iChanged(new PropertyChangeEvent(this, "Update", null, serverData));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placePiece(TicTacToePiece ticTacToePiece) {
        try{ticTacToeGameServer.placePiece(ticTacToePiece);}catch (RemoteException e){e.printStackTrace();
        }
    }

    @Override
    public void updated() {

    }

    @Override
    public void updated(PropertyChangeEvent evt) throws RemoteException {
        iChanged(evt);
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
        support.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    private void iChanged(PropertyChangeEvent event) {
        support.firePropertyChange(event);
    }

    @Override
    public void quit() {
        try {
            ticTacToeGameServer.removeListener(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
