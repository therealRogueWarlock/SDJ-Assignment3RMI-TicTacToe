package client.model.lobbymodel;

import client.networking.Client;
import shared.transferobjects.Message;
import shared.util.LobbyModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientLobbyModel implements LobbyModel, PropertyChangeListener {

	private Client client;
	private PropertyChangeSupport support;

	public ClientLobbyModel(Client client) {
		support = new PropertyChangeSupport(this);
		this.client = client;
		this.client.addListener("gameRoomAdd", this);
		this.client.addListener("gameRoomDel", this);
		this.client.addListener("messageAddedLobby", this);
		this.client.addListener("Update", this);
	}

	public boolean host() {
		return client.hostGame();
	}

	@Override
	public boolean join(PropertyChangeListener listener, int roomId, String playerName) {
		return client.joinGame(roomId);
	}

	public void update(){
		client.update();
	}

	@Override
	public void sendMessage(Message message) {
		client.sendMessage(message);
	}

	@Override
	public void addListener(String propertyName, PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	@Override
	public void removeListener(String propertyName, PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void quit(){
		client.quit();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("ClientLobby Model detect change" + evt.getPropertyName());
		support.firePropertyChange(evt);
	}

}
