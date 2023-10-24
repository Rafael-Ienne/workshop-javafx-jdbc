package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Util {
	
	/*Método que retorna o stage atual onde o controller que recebeu o evento está. O parâmetro
	  é o evento que o botão recebeu*/
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}

}
