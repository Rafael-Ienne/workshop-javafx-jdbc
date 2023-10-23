package gui.util;

import javafx.scene.control.TextField;

/*Classe que possui métodos para adicionar listeners aos controladores que irão controlar o comportamento do controle*/
public class Constraints {

	/*Função que é executada quando o controle sofrer alteração ou houver interação c/ o usuário e possibilita que se aceite
	 apenas valores inteiros*/
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*")) {
				txt.setText(oldValue);
			}
		});
	}

	/*Função que limita a quantidade de caracteres*/
	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && newValue.length() > max) {
				txt.setText(oldValue);
			}
		});
	}

	/*Função que permite entrar apenas valores double*/
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(oldValue);
			}
		});
	}
}