package shared.networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {

    void updated() throws RemoteException;

    // Todo:

}
