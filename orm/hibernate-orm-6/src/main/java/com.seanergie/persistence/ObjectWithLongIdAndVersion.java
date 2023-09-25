package com.seanergie.persistence;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class ObjectWithLongIdAndVersion extends ObjectWithLongId {

	/**
	 * Version number
	 */
	private int version;

	/**
	 * Empty Constructor for hibernate to enable CGLib optimization.
	 * Keep the access protected or public.
	 */
	protected ObjectWithLongIdAndVersion() {
		super();
	}

	@Version
	public int getVersion() {
		return version;
	}
	protected void setVersion(int version) {
		this.version = version;
	}
}