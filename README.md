프로젝트 : 카드 추천 서비스
=========
##### 카드를 추천하는 알고리즘 구현 , 
##### 상세 카드정보는 실제 카드 사이트 URL 과 연결
***
## TEAM INFO
  ### 🧑‍💻민성 
  ### 🧑‍💻해준
  ### 🧑‍💻지예
  ### 🧑‍💻채연
  ### 🧑‍💻인혁    
***
  > #### 활용 언어 ####
  ![Python.svg](HTML5.svg)   
  ![JAVA_Script.svg](JAVA_Script.svg)   
  ![CSS3.svg](CSS3.svg)   
  ![PYTHON.svg](PYTHON.svg)
  > #### 활용 프레임워크 ####
 ![spring-svgrepo-com (4).svg](spring-svgrepo-com%20%284%29.svg)
***
## 로직 설계   
### 홈 -> 카드 추천 or 카드 필터 페이지
### 카드 추천 -> 카드 추천 알고리즘 로직 -> 카드 리스트 -> 카드 페이지
### 카드 필터 페이지 -> 카드의 발급사 and 카드의 이름 and (...) - 카드의 혜택카테고리 (쇼핑 , 교통 , 통신 , 병원 , 등..)
### -> 카드 필터 리스트  -> 카드 페이지
## 카드 카테고리 (삼성 , 현대 , 롯데 , 우리 ,하나 ,국민 , 농협 , IBK ,..등등) 외 기타
### 
***
ENTITY 설계
===
***
### 카드
  - 카드 id | id
  - 카드 이름 | name
  - 카드 카테고리 | category_EnumList
  - 카드 설명 | description
  - 카드 연회비 | annual_fee
  - 카드 신청 페이지 | apply_link 
  - 카드 이미지 | image_url
  - 카드 상세정보 페이지 | detail_url
  - 등록 날짜 | createAt
#### 카드 DTO
  - 카드 발급사 | issuer
#### 카테고리 Enum 
  - 통신
  - 쇼핑
  - 할인 및 적립
  - 적립
  - 교통
  - 주유
  - 음식
  - 항공
  - 기타
#### 카드 추천 Request
  - 카드 발급사 
  - 카드 카테고리
