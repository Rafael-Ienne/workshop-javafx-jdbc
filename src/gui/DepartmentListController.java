package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
	

}
