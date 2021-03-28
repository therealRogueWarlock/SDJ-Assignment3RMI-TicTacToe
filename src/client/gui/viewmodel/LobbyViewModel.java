package client.gui.viewmodel;

import client.gui.views.lobbyview.LobbyViewController;
import client.model.lobbymodel.ClientLobbyModel;
import client.model.lobbymodel.tableobjects.GameTableRow;
import shared.transferobjects.GameData;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.transferobjects.ServerData;
import shared.util.LobbyModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class LobbyViewModel implements ViewModel, PropertyChangeListener {
	private ClientLobbyModel clientLobbyModel;
	private ObservableList<GameTableRow> observableGameRooms;
	private ObjectProperty<GameTableRow> selectedGameRoom;
	private StringProperty txtMessage;
	private ObservableList<String> lobbyChatMessages;

	public LobbyViewModel(LobbyModel lobbyModel) {
		this.clientLobbyModel = (ClientLobbyModel) lobbyModel;
		txtMessage = new SimpleStringProperty();

		observableGameRooms = FXCollections.observableArrayList();
		selectedGameRoom = new SimpleObjectProperty<>();

		clientLobbyModel.addListener("gameRoomAdd", this);
		clientLobbyModel.addListener("gameRoomDel", this);
		clientLobbyModel.addListener("messageAddedLobby", this);
		clientLobbyModel.addListener("Update",this);

		txtMessage = new SimpleStringProperty();

		lobbyChatMessages = FXCollections.observableArrayList();

	}

	public boolean host() {
		return clientLobbyModel.host();
	}

	public void sendMessage(Message message) {
		clientLobbyModel.sendMessage(message);
	}

	public void update(){
		clientLobbyModel.update();
	}


	public boolean join() {
		try {

			GameTableRow selectedItem = selectedGameRoom.getValue();

			if (selectedItem != null){
				return clientLobbyModel.join(null, selectedItem.getId(),null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		return false;
	}

	private void updateListViews(PropertyChangeEvent evt){
		ArrayList<Message> lobbyChat = (ArrayList<Message>) ((ServerData) evt.getNewValue()).getArg();
		ArrayList<GameData> allGameRoomGameData = (ArrayList<GameData>) ((ServerData) evt.getNewValue()).getArg2();
		Platform.runLater(()-> {
			lobbyChatMessages.clear();
			for (Message message: lobbyChat) {
				lobbyChatMessages.add(message.toString());
			}
			observableGameRooms.clear();
			for (GameData gameData:allGameRoomGameData){
				observableGameRooms.add(new GameTableRow(gameData));
			}
		});



	}


	public ObservableList<GameTableRow> getObservableGameRooms() {
		return observableGameRooms;
	}

	public ObservableList<String> getLobbyChatMessages() {
		return lobbyChatMessages;
	}

	public StringProperty txtMessageProperty() {
		return txtMessage;
	}

	public ObjectProperty<GameTableRow> selectedGameRoomProperty() {
		return selectedGameRoom;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case "gameRoomAdd" -> {
				Platform.runLater(() -> observableGameRooms.add(new GameTableRow((GameData) evt.getNewValue())));
			}

			case "gameRoomDel" -> Platform.runLater(() -> {
				observableGameRooms.removeIf(row -> row.getId() == (int) evt.getNewValue());
			});

			case "messageAddedLobby" -> {
				Message message = (Message) evt.getNewValue();
				String senderName = message.getName();
				String txtMessage = message.getStringMessage();
				Platform.runLater(() -> lobbyChatMessages.add(senderName + ": " + txtMessage));
			}
			case "Update" -> updateListViews(evt);
		}
	}

    public void quit() {
		clientLobbyModel.quit();
    }
}
