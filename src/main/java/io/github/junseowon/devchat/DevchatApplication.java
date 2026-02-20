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
│   ├── network/                    # ⚡ [핵심] NIO 네트워크 계층 (가장 집중할 곳)
│   │   ├── NioEventLoop.java       # Selector를 돌며 이벤트를 감지하는 무한 루프
│   │   ├── ChannelHandler.java     # Accept, Read, Write 이벤트를 분기하고 처리
│   │   └── SessionManager.java     # 현재 접속 중인 클라이언트(SocketChannel) 목록 관리
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