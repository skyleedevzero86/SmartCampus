# SmartCampus - 우리동네 중고거래 플랫폼

> AI 기반 상품 설명 생성과 실시간 채팅을 지원하는 중고거래 플랫폼

## 📋 프로젝트 개요

SmartCampus는 Spring Boot 기반의 마이크로서비스 아키텍처로 구현된 중고거래 플랫폼입니다. Spring AI를 활용한 상품 설명 자동 생성, 실시간 채팅, 스마트 검색 등 AI 기능을 통합하여 사용자 경험을 향상시킨 프로젝트입니다.

### 주요 특징

- 🤖 **AI 기반 상품 설명 생성**: GLM-4.7 모델을 활용한 상품 설명 자동 생성
- 💬 **실시간 AI 채팅**: SSE(Server-Sent Events) 기반 스트리밍 채팅 서비스
- 🔍 **스마트 검색**: AI 기반 자연어 검색 및 상품 추천
- 📦 **마이크로서비스 아키텍처**: API, Batch, Chat 서비스 분리
- 🏗️ **헥사고날 아키텍처**: 도메인 중심 설계로 유지보수성 향상

## 🛠️ 기술 스택

### Backend

| 기술              | 버전/설명                      |
| ----------------- | ------------------------------ |
| **Language**      | Java 21                        |
| **Framework**     | Spring Boot 4.0.0              |
| **Architecture**  | Hexagonal Architecture, MSA    |
| **Database**      | MySQL, Redis                   |
| **ORM**           | JPA, MyBatis                   |
| **AI**            | Spring AI 1.0.0, GLM-4.7       |
| **Security**      | JWT, Jasypt (민감 정보 암호화) |
| **Storage**       | MinIO (로컬), AWS S3 (운영)    |
| **Documentation** | Spring REST Docs               |
| **Build Tool**    | Gradle (Kotlin DSL)            |

### Frontend

| 기술                | 버전            |
| ------------------- | --------------- |
| **Framework**       | Next.js 16.0.10 |
| **Language**        | TypeScript 5    |
| **UI Library**      | React 19.2.1    |
| **Styling**         | Tailwind CSS 4  |
| **Package Manager** | pnpm 9.10.0     |

### Infrastructure

- **Database Migration**: Flyway
- **Monitoring**: Spring Actuator, Prometheus
- **HTTP Client**: OkHttp 4.12.0
- **Utilities**: Hutool, Apache Commons Lang3

## 🏗️ 아키텍처

### 시스템 구조

```
┌─────────────┐
│   Frontend  │  Next.js (Port: 7082)
│  (Next.js)  │
└──────┬──────┘
       │
       ├─────────────────┬──────────────────┐
       │                 │                  │
┌──────▼──────┐  ┌───────▼──────┐  ┌───────▼──────┐
│ market-api  │  │ market-chat  │  │ market-batch │
│  (Port:     │  │  (Port:      │  │  (Port:      │
│   8080)     │  │   8082)      │  │   8081)      │
└──────┬──────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                  │
       └─────────────────┴──────────────────┘
                         │
              ┌──────────┴──────────┐
              │                     │
         ┌────▼────┐          ┌─────▼─────┐
         │  MySQL  │          │   Redis   │
         │  :3306  │          │   :6379   │
         └─────────┘          └───────────┘
```

### 모듈별 역할

#### 1. **market-api** (메인 API 서버)

- 상품 관리 (CRUD)
- 회원 인증/인가 (JWT)
- 게시판/댓글 시스템
- 쿠폰/바우처 관리
- AI 기반 상품 추천 및 설명 생성
- 파일 업로드 (MinIO/S3)
  - 로컬 환경: MinIO 사용
  - 운영 환경: AWS S3 사용

#### 2. **market-chat** (AI 채팅 서버)

- 실시간 AI 채팅 (SSE)
- RAG(Retrieval-Augmented Generation) 지원
- 인터넷 검색 연동
- 벡터 스토어 (Redis Vector Store)
- MCP(Model Context Protocol) 클라이언트

#### 3. **market-batch** (배치 서버)

- 스케줄 작업 관리
- 메일 발송 (재발송, 삭제)
- 이벤트 기반 비동기 처리

## 📁 프로젝트 구조

```
SmartCampus/
├── backend/
│   ├── market-api/          # 메인 API 서버
│   │   ├── src/main/java/com/sleekydz86/server/
│   │   │   ├── market/
│   │   │   │   ├── product/     # 상품 도메인
│   │   │   │   ├── member/      # 회원 도메인
│   │   │   │   ├── board/       # 게시판 도메인
│   │   │   │   ├── comment/     # 댓글 도메인
│   │   │   │   ├── coupon/      # 쿠폰 도메인
│   │   │   │   └── category/    # 카테고리 도메인
│   │   │   └── global/          # 공통 설정
│   │   └── src/main/resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   │
│   ├── market-chat/          # AI 채팅 서버
│   │   └── src/main/java/com/sleekydz86/chat/
│   │       └── ai/              # AI 채팅 관련
│   │
│   └── market-batch/         # 배치 서버
│       └── src/main/java/com/sleekydz86/alerm/
│           └── batch/           # 배치 작업
│
└── frontend/                 # Next.js 프론트엔드
    ├── app/                  # App Router
    ├── public/               # 정적 파일
    └── package.json
```

## 🚀 주요 기능

### 1. AI 기반 상품 관리

- **상품 설명 자동 생성**: 상품명, 카테고리, 가격을 기반으로 AI가 상품 설명 생성
- **가격 분석**: 시장 가격과 비교하여 적정 가격 제안
- **상품 추천**: 사용자 관심사 기반 개인화 추천
- **스마트 검색**: 자연어 질의를 통한 상품 검색

### 2. 실시간 AI 채팅

- **SSE 기반 스트리밍**: 실시간 응답 스트리밍
- **다중 모드 지원**:
  - `DIRECT`: 일반 대화
  - `KNOWLEDGE_BASE`: RAG 기반 지식 검색
  - `INTERNET_SEARCH`: 인터넷 검색 연동
- **벡터 스토어**: Redis Vector Store를 활용한 문서 검색

### 3. 상품 거래 시스템

- 상품 등록/수정/삭제
- 카테고리별 상품 조회
- 좋아요 기능
- 쿠폰 적용 구매
- 조회수 추적

### 4. 커뮤니티 기능

- 게시판 CRUD
- 댓글 시스템
- 좋아요 기능
- 이미지 업로드 (MinIO/S3)

### 5. 배치 작업

- 스케줄 작업 관리
- 메일 발송 및 재발송
- 오류 처리 및 재시도 로직

## 🔧 환경 설정

### 필수 환경 변수

```bash
# 암호화 키
ENCRYPT_KEY=your-encryption-key

# AI API 설정
GLM47_API_KEY=your-api-key
GLM47_BASE_URL=https://api.z.ai/api/paas/v4
GLM47_MODEL=glm-4.7
GLM47_TEMPERATURE=1.0
GLM47_TOP_P=0.95
GLM47_MAX_TOKENS=131072

# 검색 엔진 설정
SEARCH_ENGINE_URL=http://localhost:6080/search
SEARCH_ENGINE_COUNTS=25
```

### 데이터베이스 설정

각 모듈의 `application-dev.yml` 또는 `application-prod.yml`에서 데이터베이스 설정을 확인하세요.

### 파일 스토리지 설정

#### 로컬 환경 (MinIO)

로컬 개발 환경에서는 MinIO를 사용합니다. MinIO를 실행하려면:

```bash
docker run -p 9000:9000 -p 9001:9001 \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  minio/minio server /data --console-address ":9001"
```

기본 설정:

- Endpoint: `http://localhost:9000`
- Access Key: `minioadmin`
- Secret Key: `minioadmin`
- Console: `http://localhost:9001`

#### 운영 환경 (AWS S3)

운영 환경에서는 AWS S3를 사용합니다. `application-prod.yml`에서 S3 설정을 확인하세요.

### 민감 정보 암호화

모든 민감 정보는 Jasypt를 사용하여 암호화됩니다:

- 데이터베이스 연결 정보 (URL, 사용자명, 비밀번호)
- Redis 호스트 정보
- MinIO/S3 인증 정보 (Access Key, Secret Key, Endpoint)
- 기타 민감한 설정값

암호화된 값을 생성하려면:

```bash
# JasyptUtilTest 실행
cd backend/market-api
./gradlew test --tests JasyptUtilTest

# 콘솔에 출력된 암호화된 값을 application.yml에 적용
# 예: username: ENC(암호화된값)
```

## 🏃 실행 방법

### Backend

```bash
# market-api 실행
cd backend/market-api
./gradlew bootRun --args='--spring.profiles.active=dev'

# market-chat 실행
cd backend/market-chat
./gradlew bootRun --args='--spring.profiles.active=dev'

# market-batch 실행
cd backend/market-batch
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Frontend

```bash
cd frontend
pnpm install
pnpm dev
```

## 📊 API 엔드포인트

### 상품 API

- `GET /api/categories/{categoryId}/products` - 카테고리별 상품 조회
- `POST /api/categories/{categoryId}/products` - 상품 등록
- `GET /api/categories/{categoryId}/products/{productId}` - 상품 상세 조회
- `GET /api/ai/products/generate-description` - AI 상품 설명 생성
- `GET /api/ai/products/recommendations` - 상품 추천
- `GET /api/ai/products/smart-search` - 스마트 검색

### AI 채팅 API

- `GET /api/ai/sse/connect` - SSE 연결
- `POST /api/ai/chat/send` - 채팅 메시지 전송
- `POST /api/ai/rag/upload` - 문서 업로드 (RAG)

### 인증 API

- `POST /api/auth/login` - 로그인
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/refresh` - 토큰 갱신

## 🎯 핵심 기술 구현

### 1. Hexagonal Architecture

도메인 로직과 인프라를 분리하여 테스트 가능성과 유지보수성 향상:

```java
// Domain Layer
public interface ProductPersistencePort {
    Product save(Product product);
    Optional<Product> findById(Long productId);
}

// Application Layer
@Service
public class ProductService {
    private final ProductPersistencePort persistencePort;
    // 비즈니스 로직
}

// Infrastructure Layer
@Repository
public class ProductRepository implements ProductPersistencePort {
    // JPA 구현
}
```

### 2. Event-Driven Architecture

이벤트 기반 비동기 처리로 서비스 간 결합도 감소:

```java
@EventListener
public void handleProductCreated(ProductCreatedEvent event) {
    // 비동기 처리
}
```

### 3. AI 통합

Spring AI를 활용한 LLM 통합:

- **ChatClient**: 스트리밍 채팅
- **Vector Store**: 문서 검색 및 RAG
- **MCP Client**: 외부 도구 연동

### 4. SSE 기반 실시간 통신

```java
@GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter connect(@RequestParam String userId) {
    return sseEventService.connect(userId);
}
```

## 📈 성능 최적화

- **Connection Pool**: HikariCP 최적화
- **Query Counter**: N+1 쿼리 문제 감지 및 모니터링
- **Redis Caching**: 자주 조회되는 데이터 캐싱
- **비동기 처리**: @Async를 활용한 비동기 작업 처리
- **Pessimistic Lock**: 동시성 제어

## 🔒 보안

- **JWT 인증**: 토큰 기반 인증
- **Jasypt 암호화**:
  - 모든 민감 정보를 ENC() 형식으로 암호화
  - 각 모듈별 JasyptUtil 유틸리티 클래스 제공
  - JasyptUtilTest를 통한 암호화/복호화 테스트 지원
- **환경별 스토리지 분리**:
  - 로컬: MinIO (개발 환경)
  - 운영: AWS S3 (프로덕션 환경)
- **CORS 설정**: Cross-Origin 요청 제어
- **SQL Injection 방지**: PreparedStatement 사용

## 📝 문서화

- **Spring REST Docs**: API 문서 자동 생성
- **프로파일별 설정**: dev/prod 환경 분리
- **한국어 로그**: 모든 로그 및 에러 메시지 한국어화

## 📄 라이선스

이 프로젝트는 개인 포트폴리오 프로젝트입니다.

---

**개발 기간**: 2025년  
**주요 기술**: Spring Boot, Spring AI, Next.js, MySQL, Redis, MinIO, AWS S3  
**아키텍처**: MSA, Hexagonal Architecture  
**보안**: Jasypt 암호화, JWT 인증, 환경별 스토리지 분리
