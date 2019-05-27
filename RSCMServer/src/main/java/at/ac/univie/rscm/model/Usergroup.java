package at.ac.univie.rscm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usergroups")
public class Usergroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int usergroupId;
	private String usergroupName;
	private String usergroupDescription;
	@ManyToMany(mappedBy = "usergroups", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("usergroupId: " + usergroupId + "<br/>");
		sb.append("usergroupName: " + usergroupName + "<br/>");
		sb.append("usergroupDescription: " + usergroupDescription + "<br/>");
		if(users!=null) {
			sb.append("users number: " + users.size() + "<br/>");
		}else {
			sb.append("users: null<br/>");
		}
		return sb.toString();
	}
	
	
}
