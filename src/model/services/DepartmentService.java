package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); 
	
	/*Método que retorna lista de departamentos*/
	public List<Department> findAll(){
		
		return dao.findAll();
	}
	
	/*Me´todo que atualiza ou insere um novo departamento com base no id*/
	public void saveOrUpdate(Department dep) {
		if(dep.getId() == null){
			dao.insert(dep);
		} else {
			dao.update(dep);
		}
	}
	
	/*Me´todo para remover valor de uma lista*/
	public void remove(Department dep) {
		dao.deleteById(dep.getId());
	}

}
