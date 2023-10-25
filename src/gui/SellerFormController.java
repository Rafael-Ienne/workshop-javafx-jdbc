package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private DepartmentService departmentService;

	private Seller seller;

	/*
	 * Atributo que corresponde às opções de departamento que podem ser escolhidos
	 */
	@FXML
	private ComboBox<Department> comboBoxDepartment;

	private ObservableList<Department> obsList;

	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	public void setServices(SellerService service, DepartmentService service2) {
		this.service = service;
		this.departmentService = service2;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	/*
	 * Método que inscreve um objeto na lista de objetos que receberão o evento
	 * disparado por SellerForm
	 */
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	/* Atributo que corresponde ao id do seller */
	@FXML
	private TextField txtId;

	/* Atributo que corresponde ao nome do seller */
	@FXML
	private TextField txtName;

	/* Atributo que corresponde à caixa de erro ao lado do nome */
	@FXML
	private Label labelErrorName;

	/* Atributo que corresponde ao email do seller */
	@FXML
	private TextField txtEmail;

	/* Atributo que corresponde à caixa de erro ao lado do email */
	@FXML
	private Label labelErrorEmail;

	/* Atributo que corresponde à data de nascimento do seller */
	@FXML
	private DatePicker dpBirthDate;

	/* Atributo que corresponde à caixa de erro ao lado da data de nascimento */
	@FXML
	private Label labelErrorBirthDate;

	/* Atributo que corresponde ao salario base do seller */
	@FXML
	private TextField txtBaseSalary;

	/* Atributo que corresponde à caixa de erro ao lado do salário base */
	@FXML
	private Label labelErrorBaseSalary;

	/* Atributo que corresponde ao botão de salvar o seller */
	@FXML
	private Button btSave;

	/* Atributo que corresponde ao processo de fechar o formulário */
	@FXML
	private Button btCancel;

	/*
	 * Método que salva ou atualiza o banco de dados com base nos dados passados nos
	 * text fields e fecha a janela de diálogo
	 */
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			seller = getFormData();
			service.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Util.currentStage(event).close();
		} catch (ValidationException e1) {
			setErrorMessages(e1.getErrors());

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	/*
	 * Método que instancia um objeto Seller com base no que foi pego nas caixas de
	 * texto
	 */
	private Seller getFormData() {

		ValidationException ve = new ValidationException("Validation error");

		Seller obj = new Seller();
		obj.setId(Util.tryParsetoInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			ve.addError("name", "Field cannot be empty");
		}
		obj.setName(txtName.getText());

		if (ve.getErrors().size() > 0) {
			throw ve;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Util.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle lb) {
		initializeNodes();

	}

	/* Método que estabelece restrições para os campos de id e name */
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 60);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Util.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	/*
	 * Método que pega os dados do Seller e popula as caixas de texto do formulário
	 */
	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(String.valueOf(seller.getName()));
		txtEmail.setText(String.valueOf(seller.getEmail()));
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if (seller.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(seller.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(seller.getDepartment());
		}
	}

	/* Método para escrever o erro(quando houver) no label do lado de name */

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

	/*
	 * Método que chama o departament service e carrega os departamentos do banco de
	 * dados, preenchendo a obsList
	 */
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("Department servoce was null");
		}
		obsList = FXCollections.observableList(departmentService.findAll());
		comboBoxDepartment.setItems(obsList);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
