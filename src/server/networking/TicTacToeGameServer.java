package server.networking;

import server.model.lobbymodel.ServerLobbyModel;
import shared.networking.RMIServer;
import shared.transferobjects.Message;
import shared.util.Util;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TicTacToeGameServer implements RMIServer {

    private ServerLobbyModel serverLobbyModel;



    @Override
    public void startServer() throws RemoteException, AlreadyBoundException
    {
        //TODO: Skal der være this i bind?
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(Util.SERVERNAME, this);
        UnicastRemoteObject.exportObject(this, 0);
        System.out.println("Server Started!");

        serverLobbyModel = new ServerLobbyModel();

    }

    @Override
    public void loginPlayer(String playerName) {

    }

    @Override
    public void host(String playerName) {

        serverLobbyModel.createGameRoom();


    }

    @Override
    public void joinGameRoom(int roomId, String playerName) {


    }

    @Override
    public void addMessage(Message message) {

    }

    @Override
    public void broadcast() {

    }

    @Override
    public void getServerDate() {

    }

    @Override
    public ServerLobbyModel getServerLobbyModel() {
        return null;
    }

    @Override
    public void run() {

    }
}
