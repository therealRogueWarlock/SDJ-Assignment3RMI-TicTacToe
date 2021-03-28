package client.gui.viewmodel;

import client.model.gameroommodel.ClientGameRoomModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.transferobjects.GameData;
import shared.transferobjects.Message;
import shared.transferobjects.Request;
import shared.transferobjects.TicTacToePiece;
import shared.util.GameRoomModel;
import shared.util.Subject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class GameRoomViewModel implements ViewModel, Subject {
    private StringProperty txtMessage;
    private StringProperty winLabel;
    private StringProperty gameRoomInfo;

    private BooleanProperty winLabelDisabled;
    private ClientGameRoomModel clientGameRoomModel;
    private ArrayList<StringProperty> slots;
    private BooleanProperty turnSwitcher;
    private ObservableList<String> gameRoomChatMessages;
    private PropertyChangeSupport support;

    private int roomId;



    public GameRoomViewModel(GameRoomModel gameRoomModel) {
        this.clientGameRoomModel = (ClientGameRoomModel) gameRoomModel;

        this.clientGameRoomModel.addListener("piecePlaced", this);
        this.clientGameRoomModel.addListener("win", this);
        this.clientGameRoomModel.addListener("draw", this);
        this.clientGameRoomModel.addListener("turnSwitch", this);
        this.clientGameRoomModel.addListener("messageAddedGameRoom", this);
        this.clientGameRoomModel.addListener("gameInfoUpdate",this);

        resetRoom();
    }

    public void resetRoom() {
        txtMessage = new SimpleStringProperty();

        gameRoomInfo = new SimpleStringProperty();


        turnSwitcher = new SimpleBooleanProperty();
        turnSwitcher.setValue(true);

        slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            slots.add(new SimpleStringProperty());
        }

        winLabel = new SimpleStringProperty();
        winLabelDisabled = new SimpleBooleanProperty();
        winLabelDisabled.setValue(true);

        gameRoomChatMessages = FXCollections.observableArrayList();

        support = new PropertyChangeSupport(this);
    }

    public void placePiece(int x, int y) {
        System.out.println("ViewModel: send placePiece to GameRoomModel client");
        clientGameRoomModel.placePiece(new TicTacToePiece(x, y, roomId));
    }

    public void updateGameBoard(int x, int y, String symbol) {
        System.out.println("Updating game board");

        Platform.runLater(() -> slots.get(convert2dTo1d(x, y)).setValue(String.valueOf(symbol)));
    }

    public void sendMessage(Message message) {
        clientGameRoomModel.sendMessage(message);
    }

    private void returnToLobby() {

        support.firePropertyChange("ViewChange", "GameRoom", "Lobby");

    }

    private int convert2dTo1d(int x, int y) {
        return (y * 3) + x;
    }

    public ArrayList<StringProperty> getSlots() {
        return slots;
    }

    public ObservableList<String> getGameRoomChatMessages() {
        return gameRoomChatMessages;
    }

    public StringProperty txtMessageProperty() {
        return txtMessage;
    }

    public StringProperty winLabelProperty() {
        return winLabel;
    }

    public StringProperty gameRoomInfoProperty() {
        return gameRoomInfo;
    }

    public BooleanProperty turnSwitcherProperty() {
        return turnSwitcher;
    }

    public BooleanProperty winLabelDisabledProperty() {
        return winLabelDisabled;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventType = evt.getPropertyName();

        switch (eventType) {
            case "piecePlaced" -> {
                TicTacToePiece newPiece = (TicTacToePiece) evt.getNewValue();
                updateGameBoard(newPiece.getX(), newPiece.getY(), newPiece.getPiece());
            }
            case "win" -> {

                Platform.runLater(() -> {
                    winLabelDisabled.setValue(false);
                    winLabel.setValue(evt.getNewValue() + " Wins!");
                });
                turnSwitcher.setValue(false);
                returnToLobby();
            }
            case "draw" -> {
                Platform.runLater(() -> {
                    winLabelDisabled.setValue(false);
                    winLabel.setValue(eventType);
                });
                turnSwitcher.setValue(false);
                returnToLobby();
            }
            case "turnSwitch" -> turnSwitcher.setValue(!turnSwitcher.getValue());
            case "messageAddedGameRoom" -> {
                System.out.println("gameRoom detected incoming message");
                Message message = (Message) evt.getNewValue();
                String senderName = message.getName();
                String txtMessage = message.getStringMessage();
                Platform.runLater(() -> gameRoomChatMessages.add(senderName + ": " + txtMessage));
            }
            case "gameInfoUpdate" -> {
                GameData gameData = (GameData) evt.getNewValue();
                roomId = gameData.getId();
                String roomId = String.valueOf(gameData.getId());
                String players = gameData.getPlayers();
                Platform.runLater(() -> gameRoomInfo.set(roomId + " " + players));
            }
        }
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
}
