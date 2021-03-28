package server.model.gameroommodel;

import server.model.chatmodel.ChatRoom;
import server.model.gamemodel.TicTacToe;
import shared.transferobjects.GameData;
import shared.transferobjects.Message;
import shared.transferobjects.TicTacToePiece;
import shared.util.GameRoomModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class ServerGameRoomModel implements GameRoomModel, Serializable {
    private TicTacToe ticTacToe;
    private ChatRoom chatRoom;
    private int gameRoomId;
    private String[] players = new String[2];
    private PropertyChangeSupport support;
    ;

    public ServerGameRoomModel() {
        support = new PropertyChangeSupport(this);
        ticTacToe = new TicTacToe();
        chatRoom = new ChatRoom();
    }


    public boolean join(PropertyChangeListener listener, String playerName) {

        if (addPlayerInfo(playerName)) {
            System.out.println("ServerGameRoomModel (line 31) > \tAdding " + listener + " to gameroom with id " + gameRoomId);

            addListener("piecePlaced", listener);
            addListener("win", listener);
            addListener("draw", listener);
            addListener("turnSwitch", listener);
            addListener("messageAddedGameRoom", listener);
            addListener("gameRoomDel", listener);
            addListener("gameInfoUpdate", listener);

            iChanged("turnSwitch", null);
            iChanged("gameInfoUpdate", new GameData(getRoomId(), getPlayerNames()));

            return true;
        }

        return false;
    }


    public boolean addPlayerInfo(String playerName) {
        if (players[0] == null) {
            players[0] = playerName;
        } else if (players[1] == null) {
            players[1] = playerName;
        } else return false;

        return true;
    }

    public void addId(int gameRoomId) {
        this.gameRoomId = gameRoomId;
    }

    /**
     * @param message Message from user
     * @implNote fires a PropertyChange "messageAddedGameRoom"
     */
    public void addMessage(Message message) {
        chatRoom.addMessage(message);
        iChanged("messageAddedGameRoom", message);
    }


    @Override
    public boolean placePiece(TicTacToePiece ticTacToePiece) {
        System.out.println(ticTacToePiece.getPiece());
        if (ticTacToe.placePiece(ticTacToePiece)) {
            iChanged("piecePlaced", ticTacToePiece);
        }

        if (ticTacToe.checkForWin(ticTacToePiece.getPiece())) {
            String winnerName = ticTacToePiece.getPiece();

            iChanged("win", ticTacToePiece.getPiece());





            Message newMessage = new Message(getPlayerNames() + " : " + winnerName + " Won!");
            newMessage.setName("Lobby");

            iChanged("resultMessage", newMessage);

            iChanged("gameRoomDel", gameRoomId);


        } else if (ticTacToe.checkDraw()) {
            iChanged("draw", null);

            iChanged("gameRoomDel", gameRoomId);


        }
        iChanged("turnSwitch", null);
        return false;
    }

    @Override
    public int getRoomId() {
        return gameRoomId;
    }

    @Override
    public String getPlayerCount() {
        return null;
    }

    @Override
    public String getPlayerNames() {
        return players[0] + " vs " + players[1];
    }

    @Override
    public void sendMessage(Message message) {
        chatRoom.addMessage(message);
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

    public void iChanged(String type, Object newValue) {
        support.firePropertyChange(type, null, newValue);
    }

}
