package server.model.lobbymodel;

import server.model.chatmodel.ChatRoom;
import server.model.gameroommodel.ServerGameRoomModel;
import shared.transferobjects.GameData;
import shared.transferobjects.Message;
import shared.util.GameRoomModel;
import shared.util.LobbyModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerLobbyModel implements LobbyModel, PropertyChangeListener {

    private ArrayList<ServerGameRoomModel> gameRooms;
    private PlayerList playerList;
    private ChatRoom chatRoom;
    private int gameRoomsId;

    private PropertyChangeSupport support;


    public ServerLobbyModel() {
        this.gameRooms = new ArrayList<>();
        this.playerList = new PlayerList();
        this.chatRoom = new ChatRoom();
        this.support = new PropertyChangeSupport(this);
    }


    public void addMessage(Message message) {
//		System.out.println("Add message to lobby " + message.getName() + " " + message.getStringMessage());
        if (message.getTarget().equals("GameRoom")) {
            System.out.println("ServerLobbyModel [addMessage()] > \t Attempting to send message to game 0");
            gameRooms.get(0).addMessage(message); /* Function fires PropertyChange automatically */                     //FIXME: Virker kun til at sende til det første gameroom. Løsningen til placePiece() kan også bruges her
        } else {
            System.out.println("ServerLobbyModel [addMessage()] > \t Attempting to send message to lobby from " + message.getName());
            chatRoom.addMessage(message); /* Does not fire a PropertyChange */
            iChanged("messageAddedLobby", message);
        }
//		System.out.println(getAllMessages().toString());
    }

    public void addPlayer(String name) {
        playerList.addPlayer(name);
    }

    public void removePlayer(String name) {
        playerList.removePlayer(0);                                                                                // FIXME: Hvordan skal vi kunne fjerne en spiller fra en lobby? (Tror måske det allerede er fikset, men er ikke sikker)
    }

    public ArrayList<Message> getAllMessages() {
        return chatRoom.getAllMessages();
    }

    public HashMap<Integer, String> getPlayers() {
        return playerList.getPlayers();
    }

    public ArrayList<ServerGameRoomModel> getGameRooms() {
        return gameRooms;
    }

    public ArrayList<GameData> getAllGameRoomData() {
        ArrayList<GameData> allDameRoomData = new ArrayList<>();

        for (ServerGameRoomModel gameRoom : getGameRooms()) {
            allDameRoomData.add(new GameData(gameRoom.getRoomId(),
                    gameRoom.getPlayerNames()));
        }
        return allDameRoomData;
    }


    private GameRoomModel getGameRoomById(int id) {
        for (GameRoomModel gameRoom : gameRooms) {
            if (gameRoom.getRoomId() == id) {
                return gameRoom;
            }
        }
        return null;
    }

    private void removeGameRoomById(int id) {
        iChanged("gameRoomDel", id);
        gameRooms.removeIf(gameRoom -> gameRoom.getRoomId() == id);
    }

    public synchronized ServerGameRoomModel createGameRoom()  {
        ServerGameRoomModel gameRoom = new ServerGameRoomModel();
        gameRoom.addId(gameRoomsId);
        gameRooms.add(gameRoom);
        gameRoomsId++;
        return gameRoom;
    }


    @Override
    public synchronized void join(PropertyChangeListener listener, int roomId, String playerName) {
        GameRoomModel gameRoom = getGameRoomById(roomId);
        gameRoom.join(listener, playerName);
    }

    @Override
    public void sendMessage(Message message) {
        // Only used Client Side
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
        if (evt.getPropertyName().equals("resultMessage")) {
            addMessage((Message) evt.getNewValue());
        } else if (evt.getPropertyName().equals("gameRoomDel")) {
            removeGameRoomById((Integer) evt.getNewValue());
        }else{
            iChanged(evt.getPropertyName(), evt.getNewValue());
        }

    }

    private void iChanged(String eventType, Object newValue) {
//		System.out.println("ServerLobbyModel detected a change. Firing Change");
        support.firePropertyChange(eventType, null, newValue);
    }

}
