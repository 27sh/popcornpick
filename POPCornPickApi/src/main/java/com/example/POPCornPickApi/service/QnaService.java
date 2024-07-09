package com.example.POPCornPickApi.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.POPCornPickApi.entity.Qna;
import com.example.POPCornPickApi.repository.QnaRepository;

@Service
public class QnaService {

	@Autowired
	private QnaRepository qnaRepository;
	//리스트 전부 조회히는것

    public List<Qna> getAllQna() {
    	
        return qnaRepository.findAll();
    }
		
    public Qna getQnaDetail(Long qnaNo) {
		    	
    	return qnaRepository.findByQnaNo(qnaNo);
    }

	
	
	
}