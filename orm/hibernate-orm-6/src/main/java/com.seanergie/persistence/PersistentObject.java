package com.seanergie.persistence;

@FunctionalInterface
public interface PersistentObject {

	boolean isPersisted();
}