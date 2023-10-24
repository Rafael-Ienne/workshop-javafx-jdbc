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
	
	/*Método que passa o valor passado no formulário para inteiro*/
	public static Integer tryParsetoInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException e) {
			return null;
		}
	}

}
