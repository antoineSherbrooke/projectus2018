package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.NotNull;

@Entity
public class Status  extends Model {

	public static final Finder<String, Status> find = new Finder<String, Status>(Status.class);
	
	@Id
	@NotNull
	private String name;
	
	public Status(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Status status) {
		return this.name.equals(status.getName());
	}
	
	public static List<String>listStatusDependOfUser(Users user) {
		Status statusUsers = user.getStatus();
		List<String> listStatusName = new ArrayList<String>();
		List<Status> listAllStatus = Status.find.all();
		
		
		if(statusUsers.getName()==null) {
			listStatusName.add("");
		}else {
			listStatusName.add(statusUsers.getName());
		}
		for(int i=0;i<listAllStatus.size();i++) {
			if(statusUsers==null || !statusUsers.getName().equals(listAllStatus.get(i).getName()) ) {
				listStatusName.add(listAllStatus.get(i).getName());
			}
		}
		return listStatusName;
	}
}
