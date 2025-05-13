package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sport")
public class Sport extends SuperEntity {

	@Column(name = "name", length = 20)
	private String name;

	@Column(name = "icon_name", length = 50)
	private String iconName;

	@Column(name = "rules", length = 250)
	private String rules;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}
}
