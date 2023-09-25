package com.seanergie.persistence;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * A <code>ObjectWithUnid</code> that also uses hibernate versioning facility.
 *
 * @author sylvain
 */
@MappedSuperclass
@Access( AccessType.PROPERTY )
public class ObjectWithUnidAndVersion extends ObjectWithUnid {

	/**
	 * Version number
	 */
	@SuppressWarnings( "squid:S3052" )
	private int version;

	/**
	 * Empty Constructor for hibernate to enable CGLib optimization.
	 * Keep the access protected or public.
	 */
	protected ObjectWithUnidAndVersion() {
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