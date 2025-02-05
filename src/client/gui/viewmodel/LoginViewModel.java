package client.gui.viewmodel;

import client.model.loginmodel.ClientLoginModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.util.LoginModel;

import java.beans.PropertyChangeEvent;

public class LoginViewModel implements ViewModel {
	private StringProperty name;
	private ClientLoginModel clientLoginModel;

	public LoginViewModel(LoginModel loginModel) {
		clientLoginModel = (ClientLoginModel) loginModel;

		name = new SimpleStringProperty();

	}

	public boolean tryLogin() {
		if (validLoginName()) {
			clientLoginModel.login(name.getValue());
			return true;
		}
		return false;
	}

	public StringProperty nameProperty() {
		return name;
	}

	private boolean validLoginName() {
		return name.getValue() != null && !(name.getValue().contains(" "));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

}
