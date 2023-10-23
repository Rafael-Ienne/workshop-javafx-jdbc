package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	/*Método que retorna lista de departamentos mocado*/
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "IT"));
		list.add(new Department(2, "Books"));
		list.add(new Department(3, "Games"));
		
		return list;
	}

}
