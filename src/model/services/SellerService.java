package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao(); 
	
	/*Método que retorna lista de vendedores*/
	public List<Seller> findAll(){
		
		return dao.findAll();
	}
	
	/*Método que atualiza ou insere um novo vendedor com base no id*/
	public void saveOrUpdate(Seller sel) {
		if(sel.getId() == null){
			dao.insert(sel);
		} else {
			dao.update(sel);
		}
	}
	
	/*Método para remover valor de uma lista*/
	public void remove(Seller sel) {
		dao.deleteById(sel.getId());
	}

}
