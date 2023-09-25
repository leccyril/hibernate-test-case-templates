package com.seanergie.persistence;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class is a base class for every persistent object having a UNID like identifier.
 * The id, called <code>unid</code> is generated using hibernate <code>uuid.hex</code>,
 * so that every object across the database can be uniquely identified by its <code>unid</code>.<br>
 * This is similar to the <code>@DocumentUniqueId</code> of Lotus Notes / Domino.
 */
@MappedSuperclass
@Access( AccessType.PROPERTY )
public class ObjectWithUnid implements PersistentObjectWithUnid {

	/**
	 * Unique ID, Primary key
	 */
	private UUID unid;

	/**
	 * Empty Constructor for hibernate to enable CGLib optimization.
	 * Keep the access protected or public.
	 */
	protected ObjectWithUnid() {
		// NoOp
	}

	/**
	 * @return The object Unique ID.
	 * http://www.ietf.org/rfc/rfc4122.txt
	 */
	@Id
	@GeneratedValue( generator = "system-uuid" )
	@GenericGenerator( name = "system-uuid", strategy = "uuid2" )
	@Column( nullable = false, columnDefinition = "uuid" )
	@Override
	@SuppressWarnings( "JavadocLinkAsPlainText" )
	public UUID getUnid() {
		return unid;
	}
	/**
	 * @param unid object Unique ID.
	 */
	@Override
	public void setUnid(UUID unid) {
		this.unid = unid;
	}
}