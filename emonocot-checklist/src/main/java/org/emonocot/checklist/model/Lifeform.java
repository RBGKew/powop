package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name= "Lifeform")
public class Lifeform {
	/**
	 * 
	 */
	@Id
	@Column(name="Lifeform_id")
	private Integer id;

	/**
	 * 
	 */
	@Column(name="Lifeform_abbreviation")
	private String abbreviation;

	/**
	 * 
	 */
	@Column(name="Lifeform_description")
	private String description;
	
	/**
	 * 
	 */
	public Lifeform() {
		
	}
	
	/**
	 * @param abbreviation the Checklist's abbreviation for the Lifeform
	 * @param description the Checklist's description of the Lifeform
	 */
	public Lifeform(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @param abbreviation the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
