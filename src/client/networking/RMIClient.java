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
        support  = new PropertyChangeSupport(this);
    }

    @Override
    public void start() {
        try
        {

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ticTacToeGameServer = (RMIServer) registry.lookup(Util.SERVERNAME);
            UnicastRemoteObject.exportObject(this, 0);

            ticTacToeGameServer.registerListener(this);

            System.out.println("Client started connection to server");
        }
        catch (RemoteException | NotBoundException e)
        {
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
            ticTacToeGameServer.host(this,clientName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        try {

             ServerData serverData  = ticTacToeGameServer.getServerDate();

             iChanged(new PropertyChangeEvent(this,"Update", null, serverData));

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
    public void updated(Object obj) throws RemoteException {

    }

    @Override
    public void updated(PropertyChangeEvent evt) throws RemoteException {
        support.firePropertyChange(evt);
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
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName,listener);
    }

    private void iChanged(PropertyChangeEvent event) {
        System.out.println("ServerLobby model etect change, fire change");
        support.firePropertyChange(event);
    }

}
