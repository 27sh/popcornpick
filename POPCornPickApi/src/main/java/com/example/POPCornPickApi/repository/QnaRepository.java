package com.example.POPCornPickApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.POPCornPickApi.dto.QnaDto;
import com.example.POPCornPickApi.entity.Qna;

public interface QnaRepository extends JpaRepository<Qna, Long>{

	public List<Qna> findByQnaNo(Qna qnaNo);
		
	
	//public List<QnaDto> findByQnaList();
}
