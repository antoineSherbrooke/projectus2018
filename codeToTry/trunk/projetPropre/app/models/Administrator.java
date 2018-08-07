package models;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import io.ebean.Finder;
import io.ebean.Model;

@Entity
public class Administrator extends Model {
	
	public static final Finder<Integer, Administrator> find = new Finder<Integer, Administrator>(Administrator.class);
	
	@Id
	private Integer idAdmin;
	
	@ManyToOne(cascade=CascadeType.ALL)
  	private Users userAdmin;
  
	public Administrator(Users user) {
		userAdmin = user;
		int max;
		if(Administrator.find.all().size() !=0 ){
			max =Administrator.find.all().get(Administrator.find.all().size() - 1).getIdAdmin();
			for(int i =0 ; i<Administrator.find.all().size();i++) {
				if(Administrator.find.all().get(i).getIdAdmin()>max) {
					max =Administrator.find.all().get(i).getIdAdmin();
				}
			}
			this.idAdmin = max+ 1;
		}else {
			idAdmin = 0;
		}
	}

	public static Administrator getAdministratorByCIP(String cip) {
		
		Administrator result = null;
		
		List<Administrator> listadmin= Administrator.find.all();
		
		for(int i=0; i<listadmin.size();i++) {
			if(listadmin.get(i).getUserAdmin().getCip().equals(cip)) {
				result=listadmin.get(i);
			}
		}
		
		
		return result;
	}
	

	public Users getUserAdmin() {
		return userAdmin;
	}
	
	public Integer getIdAdmin() {
		return idAdmin;
	}
	
}
