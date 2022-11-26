package serverGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ServerController {

	    @FXML // fx:id="dbNameField"
	    private TextField dbNameField; // Value injected by FXMLLoader

	    @FXML // fx:id="defulatButton"
	    private Button defulatButton; // Value injected by FXMLLoader

	    @FXML // fx:id="ipField"
	    private TextField ipField; // Value injected by FXMLLoader

	    @FXML // fx:id="passwordField"
	    private TextField passwordField; // Value injected by FXMLLoader

	    @FXML // fx:id="portField"
	    private TextField portField; // Value injected by FXMLLoader

	    @FXML // fx:id="usernameField"
	    private TextField usernameField; // Value injected by FXMLLoader
	    
	    @FXML // fx:id="quitButton"
	    private Button quitButton; // Value injected by FXMLLoader
	    
	    @FXML
	    void insertDefaultValues(ActionEvent event) {
	    	ipField.setText("localhost");
			portField.setText("5555");
	    	usernameField.setText("root");
			passwordField.setText("Aa123456");
			dbNameField.setText("ekrut");
	    }
	    
	    @FXML
	    void quitApp(ActionEvent event) {
	    	
	    }

}
