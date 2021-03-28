package shared.networking;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote{

    void updated() throws RemoteException;

    void updated(PropertyChangeEvent evt) throws RemoteException;


    // Todo:

}
