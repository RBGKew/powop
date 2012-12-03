/**
 * 
 */
package org.emonocot.job.dwc.write;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;

/**
 * @author jk00kg
 *
 */
public class SimpleRecord implements Record {

	/**
	 * 
	 */
	private String id;
	
	/**
	 * 
	 */
	private ConceptTerm rowType;
	
	/**
	 * A representation of the row as controlled ConceptTerms and corresponding values
	 */
	private LinkedHashMap<ConceptTerm, String> data = new LinkedHashMap<ConceptTerm, String>();

	/**
	 * @param rowType the type of row this is
	 */
	public SimpleRecord(ConceptTerm rowType) {
		this.rowType = rowType;
	}

	/**
	 * @param rowType the type of row this is
	 * @param data the field,value pairs that are entered into the row
	 */
	public SimpleRecord(ConceptTerm rowType, LinkedHashMap<ConceptTerm, String> data) {
		this.rowType = rowType;
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#column(int)
	 */
	public String column(int index) {
		return (String) (data.keySet().toArray())[index];
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#id()
	 */
	public String id() {
		return id;
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#rowType()
	 */
	public String rowType() {
		return rowType.simpleName();
	}

	/**
	 * @return the rowType
	 */
	public final ConceptTerm getRowType() {
		return rowType;
	}

	/**
	 * @param rowType the rowType to set
	 */
	public final void setRowType(ConceptTerm rowType) {
		this.rowType = rowType;
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#value(org.gbif.dwc.terms.ConceptTerm)
	 */
	public String value(ConceptTerm term) {
		return data.get(term);
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#value(java.lang.String)
	 */
	public String value(String qterm) {
		for(Object term : data.keySet().toArray()){
			if(((ConceptTerm) term).qualifiedName().contains(qterm)){
				return data.get(term);
			}
		}
		throw new IllegalArgumentException("Unable to recognise " + qterm + " as a field");
	}

	/* (non-Javadoc)
	 * @see org.gbif.dwc.record.Record#terms()
	 */
	public Collection<ConceptTerm> terms() {
		return data.keySet();
	}

	/**
	 * @param term the term or name of the field
	 * @param value the value to put in this field
	 */
	public void setProperty(ConceptTerm term, String value) {
		data.put(term, value);
	}

}
