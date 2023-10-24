package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/*Classe de exceção para validação de formulário*/
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/*Atributo para guardar os erros de cada campo do formulário*/
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String errorMsg) {
		errors.put(fieldName, errorMsg);
	}

}
