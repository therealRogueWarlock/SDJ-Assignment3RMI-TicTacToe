package server.networking;

import server.model.gameroommodel.ServerGameRoomModel;
import server.model.lobbymodel.ServerLobbyModel;
import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.transferobjects.GameData;
import shared.transferobjects.Message;
import shared.transferobjects.ServerData;
import shared.util.Util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TicTacToeGameServer implements RMIServer, PropertyChangeListener {

    private ServerLobbyModel serverLobbyModel;
    private ArrayList<ClientCallback> clientCallbacks;

    @Override
    public void startServer() throws RemoteException, AlreadyBoundException
    {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(Util.SERVERNAME, this);
        UnicastRemoteObject.exportObject(this, 0);
        System.out.println("Server Started!");

        serverLobbyModel = new ServerLobbyModel();
        clientCallbacks = new ArrayList<>();
        serverLobbyModel.addListener(this);

    }

    @Override
    public void loginPlayer(String playerName) {
        serverLobbyModel.addPlayer(playerName);
    }

    @Override
    public void host(PropertyChangeListener listener, String playerName) {
        ServerGameRoomModel gameRoom = serverLobbyModel.createGameRoom();

        gameRoom.addListener("resultMessage", this);
        gameRoom.addListener("gameRoomDel",this);

        gameRoom.join(listener, playerName);
    }

    @Override
    public void joinGameRoom(PropertyChangeListener listener, int roomId, String playerName) {
        serverLobbyModel.join(listener, roomId, playerName);
    }

    @Override
    public void addMessage(Message message) {
        serverLobbyModel.addMessage(message);
    }

    @Override
    public void broadcast() {

    }

    @Override
    public ServerData getServerDate() {

        ArrayList<Message> allLobbyMessages = serverLobbyModel.getAllMessages();
        ServerData serverData = new ServerData("update", allLobbyMessages);
        ArrayList<GameData> allDameRoomData = serverLobbyModel.getAllGameRoomData();
        serverData.setArg2(allDameRoomData);

        return serverData;
    }

    public void registerListener(ClientCallback listener){
        clientCallbacks.add(listener);



        /*
        support.addPropertyChangeListener("updated", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    listener.updated();
                } catch (RemoteException e) {
                    serverLobbyModel.removeListener("updated", this);
                }

            }
        });*/
    }

    @Override
    public ServerLobbyModel getServerLobbyModel() {
        return serverLobbyModel;
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("TictacToe server firede change");

        for (ClientCallback client:clientCallbacks) {
            try {
                client.updated(evt);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
}
