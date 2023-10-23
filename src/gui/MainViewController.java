package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/*A interface Initializable permite colocar código para ser executado antes da inicialização do controlador*/
public class MainViewController implements Initializable{
	
	/*Associação com o menu item Seller*/
	@FXML
	private MenuItem menuItemSeller;
	
	/*Associação com o menu item Department*/
	@FXML
	private MenuItem menuItemDepartment;
	
	/*Associação com o menu item About*/
	@FXML
	private MenuItem menuItemAbout;
	
	/*Método que trata a ação ao se clicar no menu item Seller*/
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	/*Método que trata a ação ao se clicar no menu item Department*/
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	/*Método que trata a ação ao se clicar no menu item About*/
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	/*Método que carrega a view about dentro do view principal. O synchronized garante que o processamento dentro do try
	 não seja interrompido durante o multi thread*/
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			/*Criando uma referência à view principal*/
			Scene mainScene = Main.getMainScene();
			/*O mainVBox pega o VBox da view main*/
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			/*Variável que preserva o menu principal*/
			Node mainMenu = mainVbox.getChildren().get(0);
			/*Limpeza do menu principal (children) da view main*/
			mainVbox.getChildren().clear();
			/*Adição do mainMenu*/
			mainVbox.getChildren().add(mainMenu);
			/*Adição dos filhos do newVBox ao main menu*/
			mainVbox.getChildren().addAll(newVBox.getChildren());
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);;
		}
	}
	

}
