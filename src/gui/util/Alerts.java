package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
}
