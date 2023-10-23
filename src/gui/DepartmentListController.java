package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	/*Atributo que corresponde ao TableView*/
	@FXML
	private TableView<Department> tableViewDepartments;
	
	/*Atributo que corresponde à coluna id do TableView*/
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	/*Atributo que corresponde à coluna Name do TableView*/
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	/*Atributo que corresponde ao botão New do ToolBar*/
	@FXML
	private Button btNew;;
	
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
	

}
