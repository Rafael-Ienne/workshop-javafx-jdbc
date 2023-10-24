package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/*Classe utilitária que tem alert(janela de aviso que aparece na frente da janela principal)*/
public class Alerts {

	/*Método estático que cria e mostra alert*/
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	/*Alert para confirmar a deleção. O retorno do tipo ButtonType serve para ver se usuário apertou em sim ou não*/
	public static Optional<ButtonType> showConfirmation(String title, String content) { 
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.setTitle(title); 
		alert.setHeaderText(null); 
		alert.setContentText(content); 
		return alert.showAndWait(); 
	}
}
