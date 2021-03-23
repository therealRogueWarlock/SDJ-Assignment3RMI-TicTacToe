import server.model.ServerModelTester;
import server.networking.TicTacToeGameServer;
import shared.networking.RMIServer;

import java.rmi.server.UnicastRemoteObject;

public class RunServer {


    public static void main(String[] args) {
        RMIServer ticTacToeGameServer = new TicTacToeGameServer();

        new Thread(ticTacToeGameServer::run).start();


        ServerModelTester serverModelTester = new ServerModelTester(ticTacToeGameServer.getServerLobbyModel());
        serverModelTester.start();
    }

}
