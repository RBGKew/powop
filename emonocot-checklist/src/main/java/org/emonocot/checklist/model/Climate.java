package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name= "Climate")
public class Climate {
//	/**
//	 * 
//	 */
//	ST("ST","Subtropical and tropical"),
//	/**
//	 * 
//	 */
//	MT("MT","Tropical Dry Forest"),
//	/**
//	 * 
//	 */
//	BF("BF","Boreal Forest or Taiga"),
//	/**
//	 * 
//	 */
//	G("G","Temperate Mixed Forest"),
//	/**
//	 * 
//	 */
//	UNKNOWN("UNKNOWN","Unknown"),
//	/**
//	 * 
//	 */
//	WT("WT","Tropical Moist Forest"),
//	/**
//	 * 
//	 */
//	S("S","Mediterranean Forest, woodlands and Scrub"),
//	/**
//	 * 
//	 */
//	DT("DT","Desert and Xeric Shrubland"),
//	/**
//	 * 
//	 */
//	A("A","Tundra"),
//	/**
//	 * 
//	 */
//	TA("TA","Montane tropical"),
//	/**
//	 * 
//	 */
//	MA("MA","Mangrove");

	/**
	 * 
	 */
	@Id
	@Column(name="Climate_id")
	private Integer id;

	/**
	 * 
	 */
	@Column(name="Climate_abbreviation")
	private String abbreviation;

	/**
	 * 
	 */
	@Column(name="Climate_description")
	private String description;
	
	/**
	 * 
	 */
	public Climate() {
		
	}
	
	/**
	 * @param abbreviation the Checklist's abbreviation for the Climate
	 * @param description the Checklist's description of the Climate
	 */
	public Climate(String abbreviation, String description) {
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
