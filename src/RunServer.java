import server.model.ServerModelTester;
import server.networking.TicTacToeGameServer;
import shared.networking.RMIServer;

public class RunServer {


    public static void main(String[] args) {
        RMIServer ticTacToeGameServer = new TicTacToeGameServer();

        new Thread(ticTacToeGameServer::run).start();


        ServerModelTester serverModelTester = new ServerModelTester(ticTacToeGameServer.getServerLobbyModel());
        serverModelTester.start();

        /*
        //TODO: Dette skal til RMIServerImpl
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(Util.SERVERNAME, server);
        System.out.println("Server Started!");
        --> RMIServerImpl
         */



    }

}
