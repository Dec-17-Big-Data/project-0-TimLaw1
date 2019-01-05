package com.revature.models;

import java.io.Serializable;

public class Champion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6590458798816016608L;
	private Integer champion_id;
	private String champion_name;
	private String ability1;
	private String ability2;
	private String ability3;
	private String ability4;
	private String passive;
	private Float health;
	public Champion() {
		super();
	}
	public Champion(Integer champion_id, String champion_name, String ability1, String ability2, String ability3,
			String ability4, String passive, Float health) {
		super();
		this.champion_id = champion_id;
		this.champion_name = champion_name;
		this.ability1 = ability1;
		this.ability2 = ability2;
		this.ability3 = ability3;
		this.ability4 = ability4;
		this.passive = passive;
		this.health = health;
	}
	public Integer getChampion_id() {
		return champion_id;
	}
	public void setChampion_id(Integer champion_id) {
		this.champion_id = champion_id;
	}
	public String getChampion_name() {
		return champion_name;
	}
	public void setChampion_name(String champion_name) {
		this.champion_name = champion_name;
	}
	public String getAbility1() {
		return ability1;
	}
	public void setAbility1(String ability1) {
		this.ability1 = ability1;
	}
	public String getAbility2() {
		return ability2;
	}
	public void setAbility2(String ability2) {
		this.ability2 = ability2;
	}
	public String getAbility3() {
		return ability3;
	}
	public void setAbility3(String ability3) {
		this.ability3 = ability3;
	}
	public String getAbility4() {
		return ability4;
	}
	public void setAbility4(String ability4) {
		this.ability4 = ability4;
	}
	public String getPassive() {
		return passive;
	}
	public void setPassive(String passive) {
		this.passive = passive;
	}
	public Float getHealth() {
		return health;
	}
	public void setHealth(Float health) {
		this.health = health;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ability1 == null) ? 0 : ability1.hashCode());
		result = prime * result + ((ability2 == null) ? 0 : ability2.hashCode());
		result = prime * result + ((ability3 == null) ? 0 : ability3.hashCode());
		result = prime * result + ((ability4 == null) ? 0 : ability4.hashCode());
		result = prime * result + ((champion_id == null) ? 0 : champion_id.hashCode());
		result = prime * result + ((champion_name == null) ? 0 : champion_name.hashCode());
		result = prime * result + ((health == null) ? 0 : health.hashCode());
		result = prime * result + ((passive == null) ? 0 : passive.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Champion other = (Champion) obj;
		if (ability1 == null) {
			if (other.ability1 != null)
				return false;
		} else if (!ability1.equals(other.ability1))
			return false;
		if (ability2 == null) {
			if (other.ability2 != null)
				return false;
		} else if (!ability2.equals(other.ability2))
			return false;
		if (ability3 == null) {
			if (other.ability3 != null)
				return false;
		} else if (!ability3.equals(other.ability3))
			return false;
		if (ability4 == null) {
			if (other.ability4 != null)
				return false;
		} else if (!ability4.equals(other.ability4))
			return false;
		if (champion_id == null) {
			if (other.champion_id != null)
				return false;
		} else if (!champion_id.equals(other.champion_id))
			return false;
		if (champion_name == null) {
			if (other.champion_name != null)
				return false;
		} else if (!champion_name.equals(other.champion_name))
			return false;
		if (health == null) {
			if (other.health != null)
				return false;
		} else if (!health.equals(other.health))
			return false;
		if (passive == null) {
			if (other.passive != null)
				return false;
		} else if (!passive.equals(other.passive))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Champion [champion_id=" + champion_id + ", champion_name=" + champion_name + ", ability1=" + ability1
				+ ", ability2=" + ability2 + ", ability3=" + ability3 + ", ability4=" + ability4 + ", passive="
				+ passive + ", health=" + health + "]";
	}
	
}
