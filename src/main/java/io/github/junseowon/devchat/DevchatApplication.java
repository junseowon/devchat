package io.github.junseowon.devchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevchatApplication.class, args);
    }

}
/*
nio-chat-server/
├── src/main/java/com/devchat/
│   ├── ServerApplication.java      # 서버를 실행하는 메인 클래스 (진입점)
│   │
│   ├── config/                     # 설정 및 환경 변수 관리
│   │   ├── EnvLoader.java          # .env 파일에서 비밀키, DB 정보를 읽어오는 클래스
│   │   └── DatabaseConfig.java     # MongoDB 등 데이터베이스 연결 설정
│   │
│   ├── network/
│   │   ├── SessionManager.java  # 👥 접속 중인 유저 명단 관리 및 단체 메시지 전송
│   │   ├── ClientSession.java   # ✉️ 개별 유저의 데이터 수신(Read)/송신(Write) 담당 (기존 ChannelHandler 역할)
│   │   └── AioServer.java       # 🚀 서버 포트 개방 및 새 유저 접속(Accept) 대기 (기존 NioEventLoop 역할)
│   │
│   ├── protocol/                   # 통신 규약 및 데이터 변환 계층
│   │   ├── MessageCodec.java       # ByteBuffer(바이트) ↔ Java 객체(DTO) 변환 로직
│   │   ├── PacketType.java         # 메시지 종류 (LOGIN, CHAT, DISCONNECT 등)
│   │   └── payload/                # 주고받을 데이터 객체들 (DTO)
│   │       ├── LoginRequest.java
│   │       └── ChatMessage.java
│   │
│   ├── service/                    # 🧠 비즈니스 로직 계층 (네트워크와 독립적으로 동작)
│   │   ├── AuthService.java        # 손수 구현할 회원가입, 로그인 검증, 비밀번호 해싱(Salt)
│   │   └── ChatService.java        # 메시지 브로드캐스팅, 채팅방 관리
│   │
│   └── repository/                 # 데이터베이스(DB) 접근 계층
│       ├── UserRepository.java     # 유저 정보 저장/조회
│       └── ChatRepository.java     # 채팅 기록 저장/조회
│
├── .env                            # 🔒 DB 주소, 암호화 Salt 키 등 (절대 깃허브에 올리지 않음!)
├── .gitignore                      # .env 파일과 빌드 결과물을 깃허브에서 제외
└── build.gradle                    # 의존성 관리 (MongoDB 드라이버, dotenv 라이브러리 등)
*/