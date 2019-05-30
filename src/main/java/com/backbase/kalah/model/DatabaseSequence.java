package com.backbase.kalah.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequence")
public class DatabaseSequence{
	
	public DatabaseSequence(String id, long seq) {
		super();
		this.id = id;
		this.seq = seq;
	}
	@Id
	private String id;
	private long seq;
	
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
}
