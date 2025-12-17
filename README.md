
프로젝트: web5
================

간단 소개
---------
`web5`는 Spring Boot 기반의 웹 애플리케이션 샘플 프로젝트입니다. 템플릿 기반의 서버 렌더링과 정적 자원(css, js, 이미지)을 포함하며, 회원/게시판/공지 기능의 기본 구조를 제공합니다.

요구사항
-------
- Java 17 이상
- Gradle (프로젝트에 포함된 `gradlew` 사용 권장)

기술 스택
--------
- 백엔드: Java 17, Spring Boot, Gradle
- 템플릿 엔진: Thymeleaf (서버 사이드 렌더링)
- 프론트엔드: HTML, CSS, JavaScript (정적 리소스는 `src/main/resources/static`에 위치)
- 데이터베이스: 관계형 DB 사용 (초기 스키마는 `src/main/resources/schema.sql`에 포함)

기술 스택 (상세)
----------------
- 백엔드
	- 언어: Java 17 (Gradle toolchain 설정)
	- 프레임워크: Spring Boot 3.5.3
	- 웹: `spring-boot-starter-web`
	- 템플릿: `spring-boot-starter-thymeleaf`, `thymeleaf-extras-springsecurity6` (서버사이드 렌더링)
	- 보안: `spring-boot-starter-security` (Spring Security)
	- 데이터 접근: `spring-boot-starter-data-jpa` (JPA/Hibernate)
	- DB 드라이버: MySQL (`com.mysql:mysql-connector-j`, runtime-only)
	- 유틸: `lombok`(compileOnly), `spring-boot-devtools`(developmentOnly)

- 빌드 및 플러그인
	- 빌드 도구: Gradle (권장: 프로젝트 포함 `gradlew` 사용)
	- 플러그인: `io.spring.dependency-management` 1.1.7

- 테스트
	- `spring-boot-starter-test`, `spring-security-test`, JUnit Platform

- 프론트엔드
	- 렌더링 방식: 서버 사이드 렌더링(Thymeleaf)
	- 정적 리소스: HTML/CSS/JavaScript (경로: `src/main/resources/static`)

- 파일 업로드
	- 로컬 저장: `uploads/` 폴더 (테스트/개발용)

참고
- 실제 의존성 및 버전은 [build.gradle](build.gradle#L1)에서 확인하였고, 필요시 의존성 버전 업데이트를 권장합니다.

빠른 시작 (Windows)
------------------
1. 의존성 빌드

```powershell
.
gradlew.bat clean build
```

2. 애플리케이션 실행

```powershell
gradlew.bat bootRun
# 또는
java -jar build\libs\<생성된-jar>.jar
```

프로젝트 구조 (주요)
-------------------
- `src/main/java` : Java 소스
- `src/main/resources/templates` : Thymeleaf(또는 템플릿) 뷰
- `src/main/resources/static` : 정적 자원 (css, js, image)
- `uploads/` : 업로드 파일 저장소(로컬 테스트용)

개발 참고
---------
- 설정 파일: `src/main/resources/application.properties`
- DB 초기 스키마: `src/main/resources/schema.sql`

테스트
----
Gradle로 테스트 실행:

```powershell
gradlew.bat test
```
