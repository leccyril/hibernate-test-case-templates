package com.seanergie.persistence;

import java.util.UUID;

public interface IObjectWithUnid {

	UUID getUnid();

	void setUnid(UUID unid);
}