# 다담다
대형 이커머스의 많은 기능 중 검색, 검색어 자동완성, 판매량 기준으로 상품을 추천하는 기능에서 발생하는 기술적 문제를 고민하였습니다.

## 기술 스택
`Kotlin`, `Spring boot 3.5.0`, `MySQL 8.4`, `Elasticsearch 8.13.4`

## 이런 문제를 고민했어요!
>다담다 프로젝트는 많은 쇼핑몰에서 기본적으로 제공하는 `검색`, `검색어 자동완성` 기능에서 발생하는 문제를 고민했습니다. 

### 검색
![검색 (판매량 순 정렬) 초기 아키텍처](https://github.com/user-attachments/assets/76f7c3ed-03b6-464c-886d-3660b52a6fab)

### 검색어 자동완성
![검색어 자동완성 초기 아키텍처](https://github.com/user-attachments/assets/ede09b37-7058-4094-aaf2-aaaf06abd4a5)


## 아키텍처
### 검색
![검색 아키텍처](https://github.com/user-attachments/assets/862ea500-ae00-473b-b9b0-57996f0614e5)

### 검색어 자동완성
![검색 시 로그데이터 저장 아키텍처](https://github.com/user-attachments/assets/87ba48e5-da84-4c41-9c0e-e2634b760981)

## ERD
![dadamda](https://github.com/user-attachments/assets/a94df162-990a-40aa-95a1-f96f6372f14b)
