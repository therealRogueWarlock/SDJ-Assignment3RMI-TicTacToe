package client.networking;

import shared.networking.RMIServer;
import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.util.Util;

import java.beans.PropertyChangeListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements Client{

    private RMIServer rmiServer;

    @Override
    public void start() {
        try
        {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            rmiServer = (RMIServer) registry.lookup(Util.SERVERNAME);
        }
        catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void joinGame(int roomId) {

    }

    @Override
    public void hostGame() {

    }

    @Override
    public void update() {

    }

    @Override
    public void setClientName(String name) {

    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {

    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {

    }
}
