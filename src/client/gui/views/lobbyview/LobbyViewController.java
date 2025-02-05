package client.gui.views.lobbyview;

import client.core.ViewHandler;
import client.gui.viewmodel.LobbyViewModel;
import client.gui.viewmodel.ViewModel;
import client.gui.views.ViewController;
import client.model.lobbymodel.tableobjects.GameTableRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.transferobjects.Message;

import java.io.IOException;

public class LobbyViewController implements ViewController {
    @FXML private TextField textToSendLobby;
    @FXML private ListView lobbyChat;
    @FXML private TableView<GameTableRow> gameRooms;
    @FXML private TableColumn<GameTableRow, Integer> listRoomId;
    @FXML private TableColumn<GameTableRow, String> listPlayerNames;
    private ViewHandler viewHandler;
    private LobbyViewModel lobbyViewModel;

    @Override
    public void init(ViewHandler viewHandler, ViewModel model) {
        this.viewHandler = viewHandler;
        lobbyViewModel = (LobbyViewModel) model;

        gameRooms.setItems(lobbyViewModel.getObservableGameRooms());

        listRoomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listPlayerNames.setCellValueFactory(new PropertyValueFactory<>("players"));

        lobbyViewModel.selectedGameRoomProperty().bind(gameRooms.getSelectionModel().selectedItemProperty());

        lobbyViewModel.txtMessageProperty().bind(textToSendLobby.textProperty());

        lobbyChat.setItems(lobbyViewModel.getLobbyChatMessages());

    }

    public void hostGame() throws IOException {
        viewHandler.loadView("GameRoom");
        if (lobbyViewModel.host())
            viewHandler.swapToLoadedView();

    }

    public void sendTextLobby() {
        if (!textToSendLobby.getText().isEmpty()) {
            lobbyViewModel.sendMessage(new Message(textToSendLobby.getText()));
            textToSendLobby.clear();
        }
    }

    public void quitGame() {
        lobbyViewModel.quit();
        System.exit(1);
    }

    public void joinGame() throws IOException {
        viewHandler.loadView("GameRoom");
        if (lobbyViewModel.join()) {
            viewHandler.swapToLoadedView();
        }
    }

    @Override
    public void swapScene(String scene) throws IOException {
        viewHandler.openView(scene);
    }

    public void update() {
        lobbyViewModel.update();
    }


}
