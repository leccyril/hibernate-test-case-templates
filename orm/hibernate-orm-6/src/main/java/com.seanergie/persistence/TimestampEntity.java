package com.seanergie.persistence;

import java.time.Instant;

public interface TimestampEntity {

	Instant getCreated();

	Instant getUpdated();
}