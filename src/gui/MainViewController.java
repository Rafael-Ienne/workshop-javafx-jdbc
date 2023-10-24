package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

/*A interface Initializable permite colocar código para ser executado antes da inicialização do controlador*/
public class MainViewController implements Initializable {

	/* Associação com o menu item Seller */
	@FXML
	private MenuItem menuItemSeller;

	/* Associação com o menu item Department */
	@FXML
	private MenuItem menuItemDepartment;

	/* Associação com o menu item About */
	@FXML
	private MenuItem menuItemAbout;

	/* Método que trata a ação ao se clicar no menu item Seller */
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml",(SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}

	/*
	 * Método que trata a ação ao se clicar no menu item Department. Deve-se passar
	 * o caminho da View e uma função para inicializar o controlador
	 */
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	/* Método que trata a ação ao se clicar no menu item About */
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	/*
	 * Método que carrega a view about dentro do view principal. O synchronized
	 * garante que o processamento dentro do try não seja interrompido durante o
	 * multi thread
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			/* Criando uma referência à view principal */
			Scene mainScene = Main.getMainScene();
			/* O mainVBox pega o VBox da view main */
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			/* Variável que preserva o menu principal */
			Node mainMenu = mainVbox.getChildren().get(0);
			/* Limpeza do menu principal (children) da view main */
			mainVbox.getChildren().clear();
			/* Adição do mainMenu */
			mainVbox.getChildren().add(mainMenu);
			/* Adição dos filhos do newVBox ao main menu */
			mainVbox.getChildren().addAll(newVBox.getChildren());

			/*
			 * O loader.getController() retorna o controller do tipo que foi informado no
			 * parâmetro e o initializingAction.accept(controller) executa a função dada
			 * como parâmetro
			 */
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
			;
		}
	}

}
