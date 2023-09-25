package com.seanergie.persistence;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@Access( AccessType.PROPERTY )
public class ObjectWithUnidVersionTimestamp extends ObjectWithUnidAndVersion implements TimestampEntity {

	private Instant created;
	private Instant updated;

	/**
	 * Empty Constructor for hibernate to enable CGLib optimization.
	 * Keep the access protected or public.
	 */
	protected ObjectWithUnidVersionTimestamp() {
		super();
	}

	@Override
	@Column( updatable = false )
	public Instant getCreated() {
		if( created == null ){
			created = Instant.now();
		}

		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}

	@Override
	public Instant getUpdated() {
		if( updated == null && created != null ){
			updated = created;
		}

		return updated;
	}
	public void setUpdated(Instant updated) {
		this.updated = updated;
	}

	@PrePersist
	protected void onCreate() {
		var now = Instant.now();

		if( created == null ){
			setCreated( now );
		}

		setUpdated( now );
	}

	@PreUpdate
	protected void onUpdate() {
		setUpdated( Instant.now() );
	}

	@Override
	public int hashCode() {
		return Objects.hash( created );
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}