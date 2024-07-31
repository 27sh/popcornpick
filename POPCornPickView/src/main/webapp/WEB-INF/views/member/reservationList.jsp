<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예매내역 조회 페이지</title>
<style>
main {
	width: 1200px;
	margin: 80px auto;
/* 	min-height: 700px; */
	border:1px solid #eee;
}

.flex-box {
	display: flex;
	box-sizing: border-box;
}

.myInfo {
	width: 900px;
	margin: 0 35px;
}

.myInfo h2 {
	margin-top: 20px;
}

.myInfo #p1,
.myInfo #right {
	margin: 15px 0;
}

#p1 {
	padding-bottom: 20px;
	border-bottom: 1px solid;
}

#right {
	text-align: right;
}

#red {
	color: red;
}

#cancelListTable {
	width: 900px;
	text-align: center;
}

.reservationList {
	display: flex;
	width: 900px;
	height: 250px;
}

.reservationInfo {
	display: flex;
	margin: 10px 0;
}

.ticketingNo {
	width: 200px;
	text-align: center;
/*  	line-height: 100px;  */
}

.moviePoster {
	width: 200px;
}

.ticketingNo h3 {
	width: 200px;
	margin: 80px 0 30px 0;
}

.ticketingInfo {
	border: 1px solid;
}

.h3 {
	margin: 20px 0;
	border-top: 1px solid;
	padding-top: 20px;
	width: 478px;
	height: 30px;
	position: relative;
	left: 20px;
}

#cancelTicket {
	position: absolute;
	right: 50px;
	border: none;
	border-radius: 8px;
	width: 80px;
	height: 25px;
}

.movieName {
	font-size: 20px;
	font-weight: bold;
	margin: 10px 0 10px 15px;
}

.previewInfo {
	margin: 10px 0 10px 15px;
}

th, td {
	padding: 7px;
}

.previewInfo th {
	font-weight: normal;
}

td {
	font-weight: bold;
}

#cancelTicket {
	background-color: #DCDCDC;
}

#cancelTicket:hover {
	background-color: #C8C8C8;
}

.active {
	background-color: #FF0558 !important; /* 활성화된 링크 배경 색 변경 */
	color: #fff; /* 활성화된 링크 텍스트 색 변경 */
}

.span2 {
	display: none;
}

.cancelList h2 {
	margin: 30px 0;
}

.th-style {
	background-color: #BDBDBD;
}

#cancelListTable tr:first-child td:last-child{
	color: red;
}

#cancelListTable th,
#cancelListTable td {
	padding: 15px 0;
}

</style>
</head>
<body>
	<header>
		<%@ include file="../member/mainInfo.jsp"%>
	</header>
	<main>
		<div class="flex-box">
			<div class="sidebar">
				<%@ include file="../member/sideMenu.jsp"%>
			</div>
			<div class="myInfo">
				<div class="loadReservationList">
					<h2>나의 예매 내역 <span id="ticketingCount"></span></h2>
					<p id="p1">지난 <span id="red">1개월</span>까지의 예매내역을 확인하실수 있습니다.</p>
					<p id="right">예매번호로만 티켓을 찾을 수 있으니 반드시 확인 부탁드립니다.</p>
				</div>
				<div class="cancelList">
					<h2>나의 취소내역 <span class="span2">상영일 기준 지난 7일 동안의 취소내역입니다.</span></h2>
<!-- 					<table border="1" id="cancelListTable"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<th>관람 영화/매점</th> -->
<!-- 								<th>관람 극장</th> -->
<!-- 								<th>관람 일시</th> -->
<!-- 								<th>취소일</th> -->
<!-- 								<th>결제취소 금액</th> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 						<tbody id="tbody"> -->
							
<!-- 						</tbody> -->
<!-- 					</table> -->
				</div>
			</div>
		</div>
		
	</main>
	<footer>
		
	</footer>
	<script>
		
		document.addEventListener("DOMContentLoaded", () => {
		    
			loadReservation();
			reservationcancelList();
		    
		});
		
		function loadReservation(){
			
			const token = localStorage.getItem("jwtToken");
		    const xhttp = new XMLHttpRequest();
		    
		    xhttp.onload = function() {
		        if(this.responseText != null) {
		            const reservationList = JSON.parse(this.responseText);
		            const reservationContainer = document.querySelector('.loadReservationList');
		            console.log(reservationList);
		            
		            reservationList.forEach(reservation => {
		                const reservationDiv = document.createElement('div');
		                reservationDiv.classList.add('ticketingInfo');
		                
		                const formattedRegdate = formatDate(reservation.regdate);
		                const formattedStart = formatDate(reservation.start);
		                
		                reservationDiv.innerHTML = 
		                    '<div class="reservationList">' +
		                        '<div class="ticketingNo">' +
		                            '<h3>예매번호 <span class="reservationNo">' + reservation.ticketingNo + '</span></h3>' +
		                            '<p class="reservationDate">(' + formattedRegdate + ')</p>' +
		                        '</div>' +
		                        '<div class="reservationInfo">' +
		                            '<img alt="예매한 영화 포스터" src="' + reservation.promotionPoster + '" class="moviePoster">' +
		                            '<div class="movieInfo">' +
		                                '<div class="movieName">' + reservation.movieNm + '</div>' +
		                                '<table class="previewInfo">' +
		                                    '<tr>' +
		                                        '<th>관람극장</th>' +
		                                        '<td>' + reservation.cinemaName + '</td>' + 
		                                    '</tr>' +
		                                    '<tr>' +
		                                        '<th>관람일시</th>' +
		                                        '<td>' + formattedStart + '</td>' +
		                                    '</tr>' +
		                                    '<tr>' +
		                                        '<th>관람좌석</th>' +
		                                        '<td>' + reservation.seatRow + ' ' + reservation.seatColumn + '</td>' +
		                                    '</tr>' +
		                                '</table>' +
		                                '<h3 class="h3">총 결제금액  <span class="span1">' + reservation.payTotalAmount + '원</span> <input id="cancelTicket" type="button" value="예매취소" onclick="cancelTicketing(' + reservation.ticketingNo + ')"></h3>' +
		                            '</div>' +
		                        '</div>' +
		                    '</div>' 
		                ;
		                
		                reservationContainer.appendChild(reservationDiv);
		            });
		        }
		    }
		    
		    xhttp.open("GET", "http://localhost:9001/api/v1/mem/reservation");
		    xhttp.setRequestHeader("Authorization", token);
		    xhttp.send();
			
		}
		
		function cancelTicketing(ticketingNo) {
		    if (confirm("진짜 예매를 취소하시겠습니까?")) {
		        const token = localStorage.getItem("jwtToken");
		        const xhttp = new XMLHttpRequest();
		        
		        xhttp.onload = function() {
		            alert(this.responseText);
		            if(this.responseText == "예매취소가 완료되었습니다."){
		            	window.location.href = "/member/reservationList";
		            }else {
		            	
		            }
		        }
		        
		        xhttp.open("DELETE", "http://localhost:9001/api/v1/mem/cancelReservation?ticketingNo=" + ticketingNo);
		        xhttp.setRequestHeader("Authorization", token);
		        xhttp.send();
		    }
		}

		
		function reservationcancelList() {
		    const token = localStorage.getItem("jwtToken");
		    const xhttp = new XMLHttpRequest();
		    
		    xhttp.onload = function() {
		        console.log("reservationcancelList 체크용 : " + this.responseText);
		        if (this.responseText != null) {
		            const cancelList = JSON.parse(this.responseText);
		            const reservationCancelList = document.querySelector('.cancelList');
		            
		            // 기존 테이블을 제거합니다.
		            const existingTable = document.getElementById('cancelListTable');
		            if (existingTable) {
		                existingTable.remove();
		            }
		            
		            // 테이블 생성
		            var table = document.createElement('table');
		            table.id = 'cancelListTable';
					
		            // 테이블 헤더 생성
		            var thead = document.createElement('thead');
		            var headerRow = document.createElement('tr');
		            var headers = ['관람 영화', '관람 극장', '관람 일시', '취소일', '결제취소 금액'];

		            headers.forEach(function(header) {
		                var th = document.createElement('th');
		                th.textContent = header;
		                th.classList.add('th-style');
		                headerRow.appendChild(th);
		            });

		            thead.appendChild(headerRow);
		            table.appendChild(thead);

		            // 테이블 바디 생성
		            var tbody = document.createElement('tbody');
		            tbody.id = 'tbody';

		            cancelList.forEach(function(cancel) {
		                var tr = document.createElement('tr');

		                var movieTd = document.createElement('td');
		                movieTd.textContent = cancel.movieTitle;

		                var theaterTd = document.createElement('td');
		                theaterTd.textContent = cancel.cinemaName;

		                var reservationDateTd = document.createElement('td');
		                reservationDateTd.textContent = formatDate(cancel.start);

		                var cancelDateTd = document.createElement('td');
		                cancelDateTd.textContent = formatDate(cancel.regdate);

		                var refundAmountTd = document.createElement('td');
		                refundAmountTd.textContent = cancel.payTotalAmount + '원';

		                tr.appendChild(movieTd);
		                tr.appendChild(theaterTd);
		                tr.appendChild(reservationDateTd);
		                tr.appendChild(cancelDateTd);
		                tr.appendChild(refundAmountTd);

		                tbody.appendChild(tr);
		            });

		            table.appendChild(tbody);
		            reservationCancelList.appendChild(table);
		        }
		    }
		    
		    xhttp.open("GET", "http://localhost:9001/api/v1/mem/cancelReservation");
		    xhttp.setRequestHeader("Authorization", token);
		    xhttp.send();
		}
		
		function formatDate(dateString) {
		    var date = new Date(dateString);
		    var year = date.getFullYear();
		    var month = (date.getMonth() + 1).toString().padStart(2, '0'); 
		    var day = date.getDate().toString().padStart(2, '0');
		    var hours = date.getHours().toString().padStart(2, '0');
		    var minutes = date.getMinutes().toString().padStart(2, '0');
		    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
		}
		
	</script>
</body>
</html>