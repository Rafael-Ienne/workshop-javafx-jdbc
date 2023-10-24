package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	/*Dependência de serviço para que os dados sejam mostrados na view de DepartmentList*/
	private DepartmentService service;
	
	/*Atributo que corresponde ao TableView*/
	@FXML
	private TableView<Department> tableViewDepartments;
	
	/*Atributo que corresponde à coluna id do TableView*/
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	/*Atributo que corresponde à coluna Name do TableView*/
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	private ObservableList<Department> obsList;
	
	/*Atributo que corresponde ao botão New do ToolBar*/
	@FXML
	private Button btNew;
	
	/*Método que injeta dependência para fazer um acoplamento fraco*/
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

	/*Método para inicializar o comportamento de componentes de uma view, como as colunas de uma tabela*/
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		/*Esquema para que o TableView acompanhe a altura da janela*/
		Stage stage =(Stage) Main.getMainScene().getWindow();
		tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
		
		
	}
	
	/*Método que acessa o serviço, carrega departamento, coloca na ObservableList e a adiciona na TableView*/
	public void updateTableView() {
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		} 
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartments.setItems(obsList);
		
	}
	
	/*Método que cria a janela modal de diálogo que será gerada ao se clicar no botão new da página de Department*/
	/*Esse método recebe como parâmetro o caminho .fxml da view e o Stage que criou a janela de diálogo */
	private void createDialogForm(Department dep, String absoluteName, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(dep);
			controller.updateFormData();
			
			/*Instancia-se outro stage para ter um palco na frente do outro*/
			Stage dialogStage = new Stage();
			
			/*Configurações da janela de diálogo*/
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
