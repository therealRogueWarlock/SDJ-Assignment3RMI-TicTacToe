package client.model.gameroommodel;

import client.networking.Client;
import shared.transferobjects.Message;
import shared.transferobjects.TicTacToePiece;
import shared.util.GameRoomModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientGameRoomModel implements GameRoomModel, PropertyChangeListener {

	private PropertyChangeSupport support;
	private Client client;

	public ClientGameRoomModel(Client client) {
		this.client = client;
		support = new PropertyChangeSupport(this);

		this.client.addListener("piecePlaced", this);
		this.client.addListener("win", this);
		this.client.addListener("draw", this);
		this.client.addListener("turnSwitch", this);
		this.client.addListener("messageAddedGameRoom", this);
		this.client.addListener("gameInfoUpdate",this);

	}

	@Override
	public boolean join(PropertyChangeListener listener, String playerName) {
		// only used on serverside.
		return false;
	}

	@Override
	public boolean placePiece(TicTacToePiece ticTacToePiece) {
		ticTacToePiece.setPiece(client.getName());
		client.placePiece(ticTacToePiece);
		return false;
	}

	@Override
	public void sendMessage(Message msg) {
		client.sendMessage(msg);
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		support.firePropertyChange(evt);
	}

	@Override
	public int getRoomId() {
		return 0;
	}

	@Override
	public String getPlayerCount() {
		return ";";
	}

	@Override
	public String getPlayerNames() {
		return "";
	}
}
