package com.backbase.kalah.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.backbase.kalah.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, Long> {

}
