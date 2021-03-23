import server.model.ServerModelTester;
import server.networking.SocketServer;
import shared.networking.RMIServer;

public class RunServer {


    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();

        new Thread(socketServer::run).start();


        ServerModelTester serverModelTester = new ServerModelTester(socketServer.getServerLobbyModel());
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
