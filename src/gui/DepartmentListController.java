package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	/* Coluna que adiciona um botão para remover um departamento */
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	/* Coluna que adiciona um botão para editar um departamento */
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	/*
	 * Dependência de serviço para que os dados sejam mostrados na view de
	 * DepartmentList
	 */
	private DepartmentService service;

	/* Atributo que corresponde ao TableView */
	@FXML
	private TableView<Department> tableViewDepartments;

	/* Atributo que corresponde à coluna id do TableView */
	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	/* Atributo que corresponde à coluna Name do TableView */
	@FXML
	private TableColumn<Department, String> tableColumnName;

	private ObservableList<Department> obsList;

	/* Atributo que corresponde ao botão New do ToolBar */
	@FXML
	private Button btNew;

	/* Método que injeta dependência para fazer um acoplamento fraco */
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Util.currentStage(event);
		Department dep = new Department();
		createDialogForm(dep, "/gui/DepartmentForm.fxml", parentStage);
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

		/* Esquema para que o TableView acompanhe a altura da janela */
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());

	}

	/*
	 * Método que acessa o serviço, carrega departamento, coloca na ObservableList e
	 * a adiciona na TableView
	 */
	public void updateTableView() {

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartments.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	/*
	 * Método que cria a janela modal de diálogo que será gerada ao se clicar no
	 * botão new da página de Department
	 */
	/*
	 * Esse método recebe como parâmetros um objeto Department vazio, o caminho
	 * .fxml da view e o Stage que criou a janela de diálogo
	 */
	private void createDialogForm(Department dep, String absoluteName, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setDepartment(dep);
			controller.setService(new DepartmentService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			/* Instancia-se outro stage para ter um palco na frente do outro */
			Stage dialogStage = new Stage();

			/* Configurações da janela de diálogo */
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	/* Método que atualiza a table view ao ocorrer o salvamento de um novo objeto */
	@Override
	public void onDataChanged() {
		updateTableView();

	}

	/*
	 * Método que cria os botões de editar do lado de cada linha e abre a janela de diálogo já
	 * com os dados do departamento preenchidos
	 */
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Util.currentStage(event)));
			}
		});
	}

	/*
	 * Método que cria os botões de remover do lado de cada linha e abre a janela de diálogo já
	 * com os dados do departamento preenchidos
	 */
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
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

	private void removeEntity(Department obj) {
		Optional<ButtonType> option = Alerts.showConfirmation("Confirmation", "Are you sure about your decision?");
		
		if(option.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch(DbIntegrityException e1) {
				Alerts.showAlert("Error removing object", null, e1.getMessage(), AlertType.ERROR);
			}
		}
		
	}

}
