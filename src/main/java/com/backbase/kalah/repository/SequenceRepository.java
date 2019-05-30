package com.backbase.kalah.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backbase.kalah.model.DatabaseSequence;

@Repository
public interface SequenceRepository extends MongoRepository<DatabaseSequence, String>{

}
