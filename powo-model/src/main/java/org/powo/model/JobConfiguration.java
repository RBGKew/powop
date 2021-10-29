package org.powo.model;

import java.util.Map;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.batch.core.BatchStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic=true)
public class JobConfiguration extends Base {

	private static final long serialVersionUID = -8150198731653314742L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String jobName;

	private Long lastJobExecution;

	private String description;

	@Singular
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private Map<String, String> parameters;

	@Enumerated(value = EnumType.STRING)
	private BatchStatus jobStatus;

	private String jobExitCode;

	public JobConfiguration() {
		setIdentifier(UUID.randomUUID().toString());
	}

	@NotNull
	@Override
	@JsonIgnore(false)
	public String getIdentifier() {
		return identifier;
	}

}