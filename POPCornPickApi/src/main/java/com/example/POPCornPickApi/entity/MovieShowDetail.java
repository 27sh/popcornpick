package com.example.POPCornPickApi.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class MovieShowDetail extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long detailNo;
	
	@ManyToOne
	@JoinColumn(name = "movieDc", referencedColumnName = "movieDc", nullable = false)
	private Movie movie;
	
	@ManyToOne
	@JoinColumn(name = "roomNo", referencedColumnName = "roomNo", nullable = false)
	private Room room;
	
	@Column(nullable = true)
	private Date endDate;
	
	@Column(nullable = true)
	private Long totalViewer;
	
	@Column(nullable = true)
	private boolean dubbing;
	
	@Column(nullable = true)
	private boolean subtitle;
}
