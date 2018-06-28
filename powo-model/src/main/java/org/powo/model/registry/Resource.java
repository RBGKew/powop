/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.model.registry;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.powo.model.Base;
import org.powo.model.JobConfiguration;
import org.powo.model.constants.ResourceType;
import org.powo.model.marshall.json.OrganisationDeserialiser;
import org.powo.model.marshall.json.OrganisationSerializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@ToString(exclude = "organisation")
@Builder
@AllArgsConstructor
public class Resource extends Base {

	private static final long serialVersionUID = 5676965857186600965L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resourceSequenceGenerator")
	@SequenceGenerator(name = "resourceSequenceGenerator", allocationSize = 1000, sequenceName = "seq_resource")
	private Long id;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	private ResourceType resourceType;

	@URL
	@NotEmpty
	private String uri;

	@JsonSerialize(using = OrganisationSerializer.class)
	@JsonDeserialize(using = OrganisationDeserialiser.class)
	@ManyToOne(fetch = FetchType.LAZY)
	private Organisation organisation;

	@NotEmpty
	private String title;

	private Long jobId;

	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	private JobConfiguration jobConfiguration;

	@NotEmpty
	@NaturalId
	protected String identifier;

	public Resource() {
		setIdentifier(UUID.randomUUID().toString());
	}
}