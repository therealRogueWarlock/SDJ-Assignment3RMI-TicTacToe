package server.networking;

import server.model.gameroommodel.ServerGameRoomModel;
import server.model.lobbymodel.ServerLobbyModel;
import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.transferobjects.GameData;
import shared.transferobjects.Message;
import shared.transferobjects.ServerData;
import shared.transferobjects.TicTacToePiece;
import shared.util.GameRoomModel;
import shared.util.Util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    public void startServer() throws RemoteException, AlreadyBoundException {
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
    public synchronized boolean host(ClientCallback clientCallback, String playerName) {
        ServerGameRoomModel gameRoom = serverLobbyModel.createGameRoom();

        gameRoom.addListener("resultMessage", serverLobbyModel);
        gameRoom.addListener("gameRoomDel", serverLobbyModel);

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    clientCallback.updated(evt);
                } catch (RemoteException e) {
                    serverLobbyModel.removeListener(this);
                }
            }
        };

        if (gameRoom.join(listener, playerName)) {
            GameData newGameRoomData = new GameData(gameRoom.getRoomId(), gameRoom.getPlayerNames());
            propertyChange(
                    new PropertyChangeEvent(
                            this, "gameRoomAdd", null, newGameRoomData));
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean joinGameRoom(ClientCallback clientCallback, int roomId, String playerName) {

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    clientCallback.updated(evt);
                } catch (RemoteException e) {
                    serverLobbyModel.removeListener(this);
                }

            }
        };

        if (serverLobbyModel.join(listener, roomId, playerName)) {
            broadcast(new PropertyChangeEvent(this, "Update", null, getServerDate()));
            return true;
        }

        return false;
    }

    @Override
    public void addMessage(Message message) {
        serverLobbyModel.addMessage(message);
    }

    @Override
    public void broadcast(PropertyChangeEvent evt) {
        new Thread(() -> {
            for (ClientCallback client : clientCallbacks) {
                try {
                    client.updated(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    @Override
    public ServerData getServerDate() {

        ArrayList<Message> allLobbyMessages = serverLobbyModel.getAllMessages();
        ServerData serverData = new ServerData("Update", allLobbyMessages);
        ArrayList<GameData> allDameRoomData = serverLobbyModel.getAllGameRoomData();
        serverData.setArg2(allDameRoomData);

        return serverData;
    }

    public void registerListener(ClientCallback clientCallback) {
        clientCallbacks.add(clientCallback);
    }

    @Override
    public ServerLobbyModel getServerLobbyModel() {
        return serverLobbyModel;
    }

    @Override
    public boolean placePiece(TicTacToePiece piece) {
        GameRoomModel gameRoom = serverLobbyModel.getGameRooms().get(piece.getTargetGameRoom());
        return gameRoom.placePiece(piece);
    }

    @Override
    public void removeListener(ClientCallback client) {
        System.out.println("A Client has Disconnected");
        clientCallbacks.remove(client);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        broadcast(evt);
    }
}
