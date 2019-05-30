package com.backbase.kalah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.kalah.model.DatabaseSequence;
import com.backbase.kalah.repository.SequenceRepository;

@Service
public class SequenceServiceImpl implements SequenceService {
	
	@Autowired
	private SequenceRepository sequenceRepository;

	public long generateId(String sequenceName) {
		DatabaseSequence dbSequence = sequenceRepository.findById(sequenceName).orElse(null);
		if (dbSequence != null) {
			dbSequence.setSeq(dbSequence.getSeq()+1);
			sequenceRepository.save(dbSequence);
			return dbSequence.getSeq();
		}else {
			sequenceRepository.insert(new DatabaseSequence(sequenceName, 1));
			return 1;
		}
	}

}
