package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller department;

	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	public void setService(SellerService service) {
		this.service = service;
	}

	public void setSeller(Seller department) {
		this.department = department;
	}

	/*
	 * Método que inscreve um objeto na lista de objetos que receberão o evento
	 * disparado por SellerForm
	 */
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	/*
	 * Método que salva ou atualiza o banco de dados com base nos dados passados
	 * nos text fields e fecha a janela de diálogo
	 */
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (department == null) {
			throw new IllegalStateException("Seller was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			department = getFormData();
			service.saveOrUpdate(department);
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
	 * Método que instancia um objeto Seller com base no que foi pego nas caixas
	 * de texto
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
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	/*
	 * Método que pega os dados do Seller e popular as caixas de texto do
	 * formulário
	 */
	public void updateFormData() {
		if (department == null) {
			throw new IllegalStateException("Seller was null");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(String.valueOf(department.getName()));
	}

	/* Método para escrever o erro(quando houver) no label do lado de name */

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
