# 개인 블로그 프로젝트(Jelog)

Java와 Spring Boot를 기반으로 한 개인 블로그 개발 프로젝트입니다. 현재는 진행 중인 사이드 프로젝트로, 개인적인 블로그 서비스 제공을 목표로 하고 있습니다.

## 🚀 프로젝트 소개

개인 블로그 서비스를 제공하기 위해 시작된 이 프로젝트는 Java를 주 언어로 사용하며, Spring Data JPA를 활용하여 개발되고 있습니다. 앱 통합까지 계획하고 있는 단계별 프로젝트입니다.

### 현재 기능
- 사용자 페이지 추적
- 회원가입, 로그인
- 게시물 검색, 열람
- 댓글 기능

### 계획 중인 기능
- **관리자 페이지**: 사용자 접속 통계, 게시물 관리
- **댓글 기능**: 게시물에 대한 사용자 대댓글 작성 지원
- **마이그레이션**: 블로그 카테고리 Global Enum → DB 데이터 저장, UI/UX 수정
- **약관동의**: page 하드코딩 → DB 데이터 저장 및 버전 관리
- **보안**: 보안 강화 (**XSS**: img, video, iframe등 차단)

## 🛠 사용 기술
- **Java**
- **Spring Boot**
- **JPA Spring Data**
- **Thymeleaf**: 템플릿 엔진
- **MySQL**: 데이터베이스
- **HTML/CSS/JavaScript**: 프론트엔드

## 📚 참고 문서
- [Spring Boot 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf 공식 문서](https://www.thymeleaf.org/documentation.html)

## 📄 라이센스
본 프로젝트는 [Creative Commons Attribution-NonCommercial (CC BY-NC)](https://creativecommons.org/licenses/by-nc/4.0/) 라이센스 하에 배포됩니다. 비상업적 사용에 한해 자유롭게 공유 및 수정이 가능합니다.


---
## 🔄 Update Log

### v1.2.0 - 2024-05-09
#### 🆕 Added
- 새로운 댓글 기능 추가.

#### 🛠️ Fixed
- 사용자 댓글 기능에서 랜덤 닉네임 관련 일부 버그 수정.
- 상세 페이지의 조회수 카운팅 오류 수정.

### v1.1.0 - 2023-06-07 ~ 2024-05-09
#### 🆕 Added
- 회원가입 기능 추가.
- 로그인 기능 추가.
- 게시물 검색 및 열람 기능 추가.
- JSP 개발 (**Front-End**).
- 글 작성 에디터 기능 추가.
- Thymeleaf 개발 (**Front-End**).

#### 🛠️ Fixed
- 프론트엔드 템플릿을 `JSP`에서 `Thymeleaf`로 변경.
- `UserRole` 접근 관련 버그 수정.
- 메인 페이지 이미지 검색 기능 수정.
- 게시물 리스트 검색 기능에서의 버그 수정.

#### ✨ Improvements
- 로그인 및 회원가입 페이지 디자인 개선.
- 메인 페이지 및 상세 페이지 디자인 개선.
- 반응형 웹 디자인 구현:
    - 미디어 쿼리(CSS) 추가.
- 메인 페이지의 디자인 깨짐 문제 해결.

### v1.0.0 - 2023-06-07
#### 🎉 Initial Release
- 프로젝트 첫 출시 및 기능 업로드.
