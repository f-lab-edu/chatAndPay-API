# chatAndPay-API

## 프로젝트 목표

- **카카오톡+카카오페이와 유사한 메신저+송금 서비스**를 구현해내는 게 목표입니다.
- ChatAndPay Repo중 API는 **카카오페이 위주**의 구현을 진행합니다.
- ChatAndPay Repo중 [Chat](https://github.com/f-lab-edu/chatAndPay-WS)은 **카카오톡 위주의** 구현을 진행합니다.
- 동시성 처리, 외부 API 통신 이슈 등 실 환경에서 발생할 수 있는 로직 처리와, 나아가 대용량 서비스를 고려한 성능 개선까지 진행하고자 합니다.

## Chat and Pay 서버 구조도

![image](https://github.com/f-lab-edu/chatAndPay-API/assets/131243317/b6dc78ad-16ee-434e-b203-2b10908173b7)

- Kotiln
- Spring Boot 2.7.11 / Spring JPA / QueryDSL
- MySQL / Redis / MongoDB

## ERD

![ERD](https://github.com/f-lab-edu/chatAndPay-API/assets/131243317/3caf1122-1e06-4812-a06b-026147ed0a11)

- [ERD](https://b-log.notion.site/ERD-8dd4491e18d343e98b84c811bf72b268?pvs=21)

## 주요 기능 순서도

![순서도](https://github.com/f-lab-edu/chatAndPay-API/assets/131243317/fe35ae34-290a-4ced-84dd-2011eb797f9b)


- [순서도](https://drive.google.com/file/d/1Q22-uHQH_zKGrU6yRtzkptuMOEKJvRyo/view?usp=sharing)


## Docs

- [Logic: 유저 탈퇴 시 타 은행 계좌는 어떻게 할까?](https://b-log.notion.site/Logic-1aeb715a1f944560a3eb7ca81cfa8221?pvs=21)
- [Logic: 유저 탈퇴 시 미결 거래 / 지갑 잔고가 남아 있으면 어떻게 할까?](https://b-log.notion.site/Logic-f12384db4d384318aeb716f94d3e0d1c?pvs=21)
- [Logic: Refresh Token이 탈취당했을 때?](https://b-log.notion.site/Logic-Refresh-Token-611efd10837346c38b74c1e8505419f8?pvs=21)
- [Logic: ‘송금’에서 마주친 동시성 이슈](https://b-log.notion.site/Logic-e9639d803ab24e88a8746ba3a30cb004?pvs=21)
- [Architecture: 클래스 별 하나의 DTO vs 중첩 클래스 DTO](https://b-log.notion.site/Architecture-DTO-vs-DTO-b04d9e22dbff4fd3bee746e2dba48a73?pvs=21)
- [Logic: Validation 처리하기](https://b-log.notion.site/Logic-Validation-1ccff0f615954ccb914458153ac3d5c7?pvs=21)
- [Logic: 토큰 검증 시 데이터 캐싱하기](https://b-log.notion.site/Logic-f41f554b1d6a4cd488f65f29b92e3b21?pvs=21)
- [Test: PK - Auto Increment vs UUID](https://b-log.notion.site/Test-PK-Auto-Increment-vs-UUID-87a951b979ce4959940d8e4cb225f4b2?pvs=21)
- [Test: WireMock으로 Mock 서버 만들기](https://b-log.notion.site/Test-WireMock-Mock-0e4e56b4237a49faac9d40c159b306e3?pvs=21)

---

- [시나리오](https://github.com/f-lab-edu/chatAndPay-API/wiki/%5B시나리오%5D-01.회원:-채팅-회원-시나리오)
- [기능 명세서](https://github.com/f-lab-edu/chatAndPay-API/wiki/%5B기능-명세%5D)
- [REST API](https://github.com/f-lab-edu/chatAndPay-API/wiki/%5BREST-API%5D)
