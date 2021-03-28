import server.networking.TicTacToeGameServer;
import shared.networking.RMIServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class RunServer {


    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        RMIServer ticTacToeGameServer = new TicTacToeGameServer();

        ticTacToeGameServer.startServer();
    }

}
