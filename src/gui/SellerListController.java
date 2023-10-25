package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	
	/* Coluna que corresponde ao email de um seller */
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	/* Coluna que corresponde ao birthdate de um seller */
	@FXML
	private TableColumn<Seller, Date> tableColumnbirthDate;
	
	/* Coluna que corresponde ao salario base de um seller */
	@FXML
	private TableColumn<Seller, Double> tableColumnbaseSalary;
	
	/* Coluna que adiciona um botão para remover um seller */
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	/* Coluna que adiciona um botão para editar um seller */
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	/*
	 * Dependência de serviço para que os dados sejam mostrados na view de
	 * SellerList
	 */
	private SellerService service;

	/* Atributo que corresponde ao TableView */
	@FXML
	private TableView<Seller> tableViewSellers;

	/* Atributo que corresponde à coluna id do TableView */
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	/* Atributo que corresponde à coluna Name do TableView */
	@FXML
	private TableColumn<Seller, String> tableColumnName;

	private ObservableList<Seller> obsList;

	/* Atributo que corresponde ao botão New do ToolBar */
	@FXML
	private Button btNew;

	/* Método que injeta dependência para fazer um acoplamento fraco */
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Util.currentStage(event);
		Seller dep = new Seller();
		createDialogForm(dep, "/gui/SellerForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	/*
	 * Método para inicializar o comportamento de componentes de uma view, como as
	 * colunas de uma tabela
	 */
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnbirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Util.formatTableColumnDate(tableColumnbirthDate, "dd/MM/yyyy");
		tableColumnbaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Util.formatTableColumnDouble(tableColumnbaseSalary, 2);
		

		/* Esquema para que o TableView acompanhe a altura da janela */
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSellers.prefHeightProperty().bind(stage.heightProperty());

	}

	/*
	 * Método que acessa o serviço, carrega seller, coloca na ObservableList e
	 * a adiciona na TableView
	 */
	public void updateTableView() {

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSellers.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	/*
	 * Método que cria a janela modal de diálogo que será gerada ao se clicar no
	 * botão new da página de Seller
	 */
	/*
	 * Esse método recebe como parâmetros um objeto Seller vazio, o caminho
	 * .fxml da view e o Stage que criou a janela de diálogo
	 */
	private void createDialogForm(Seller dep, String absoluteName, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(dep);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			/* Instancia-se outro stage para ter um palco na frente do outro */
			Stage dialogStage = new Stage();

			/* Configurações da janela de diálogo */
			dialogStage.setTitle("Enter seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	/* Método que atualiza a table view ao ocorrer o salvamento de um novo objeto */
	@Override
	public void onDataChanged() {
		updateTableView();

	}

	/*
	 * Método que cria os botões de editar do lado de cada linha e abre a janela de
	 * diálogo já com os dados do seller preenchidos
	 */
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Util.currentStage(event)));
			}
		});
	}

	/*
	 * Método que cria os botões de remover do lado de cada linha e abre a janela de
	 * diálogo já com os dados do seller preenchidos
	 */
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> option = Alerts.showConfirmation("Confirmation", "Are you sure about your decision?");

		if (option.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e1) {
				Alerts.showAlert("Error removing object", null, e1.getMessage(), AlertType.ERROR);
			}
		}

	}

}
