package com.seanergie.persistence;

import jakarta.persistence.Transient;

public interface PersistentObjectWithUnid extends PersistentObject, IObjectWithUnid {

	@Override
	@Transient
	default boolean isPersisted() {
		return getUnid() != null;
	}
}