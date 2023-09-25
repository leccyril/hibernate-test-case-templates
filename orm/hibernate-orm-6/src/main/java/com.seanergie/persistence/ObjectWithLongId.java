package com.seanergie.persistence;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class ObjectWithLongId implements PersistentObject {

	/**
	 * Unique ID, Primary key
	 */
	private Long id;

	/**
	 * Empty Constructor for hibernate to enable CGLib optimization.
	 * Keep the access protected or public.
	 */
	protected ObjectWithLongId() {
		// NoOp
	}

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	public Long getId() {
		return id;
	}
	/**
	 * @param id object Unique ID.
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	@Override
	@Transient
	public boolean isPersisted() {
		return this.id != null;
	}
}