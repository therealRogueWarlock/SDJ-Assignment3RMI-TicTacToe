package client.core;

import client.gui.viewmodel.ViewModel;
import client.gui.views.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHandler {

	private ViewModelFactory viewModelFactory;
	private Stage stage;
	private Scene loadedScene;

	public ViewHandler(Stage stage, ViewModelFactory viewModelFactory) {
		this.stage = stage;
		this.viewModelFactory = viewModelFactory;

	}

	public void start() throws IOException {
		openView("Login");
	}

	public void openView(String viewToOpen) throws IOException {
		Scene scene = sceneLoader(viewToOpen);
		stage.setScene(scene);
		stage.show();
	}

	public void loadView(String viewToOpen) throws IOException {
		loadedScene = sceneLoader(viewToOpen);
	}

	public void swapToLoadedView(){
		stage.setScene(loadedScene);
		stage.show();
	}

	private Scene sceneLoader(String view) throws IOException {
		Scene scene;
		Parent root;

		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(getClass().getResource("../gui/views/" + view.toLowerCase() + "view/" + view + "View.fxml"));
		root = loader.load();
		ViewController viewController = loader.getController();
		viewController.init(this, getViewModelByViewName(view));

		scene = new Scene(root);
		return scene;
	}

	private ViewModel getViewModelByViewName(String viewName) {
		return switch (viewName) {
			case "Login" -> viewModelFactory.getLoginViewModel();
			case "Lobby" -> viewModelFactory.getLobbyViewModel();
			case "GameRoom" -> viewModelFactory.getGameRoomViewModel();
			default -> null;
		};
	}

}
