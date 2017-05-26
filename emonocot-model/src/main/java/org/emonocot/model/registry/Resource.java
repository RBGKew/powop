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
package org.emonocot.model.registry;

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
import javax.validation.constraints.NotNull;

import org.emonocot.model.Base;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

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
@ToString(exclude="organisation")
@Builder
@AllArgsConstructor
public class Resource extends Base {

	private static final long serialVersionUID = 5676965857186600965L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	private ResourceType resourceType;

	@URL
	@NotEmpty
	private String uri;

	@JsonSerialize(using = OrganisationSerializer.class)
	@JsonDeserialize(using = OrganisationDeserialiser.class)
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Organisation organisation;

	@NotEmpty
	private String title;

	private Long jobId;

	@OneToOne
	private JobConfiguration jobConfiguration;

	@NaturalId
	@NotEmpty
	protected String identifier;

	public Resource() {
		setIdentifier(UUID.randomUUID().toString());
	}
}