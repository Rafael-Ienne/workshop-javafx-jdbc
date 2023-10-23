package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); 
	
	/*Método que retorna lista de departamentos mocado*/
	public List<Department> findAll(){
		
		return dao.findAll();
	}

}
