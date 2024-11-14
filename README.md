## 👨‍🏫 프로젝트 소개
MQTT Protocol 을 이용한 꽃가루농도 조회 서비스 

## ⏲️ 개발 기간
- 2023.11.01 ~ 2024.12.13
  - 프로젝트에 대한 이해 및 학습
  - 공공 데이터 포털에서 개인 인증키 발급 및 요청변수 할당
  - 프로젝트 환경 구성
    - JAVA JDK 환경변수 
    - Publisher JAVA API 사용을 위한 JSON 설치
    - Broker Server Mosquitto Set
    - Subscriber MongoDB Set
    - Print HTML Set
  
## 💻 개발환경
- JAVA jdk-8u281
- Node.js.16.15.0 LTS
- Mosquitto.1.6.9
- MongoDB.5.0.8
- HTML5

## 📌 프로젝트개요
"MQTT Protocol을 활용한 꽃가루 농도 조회 서비스" 프로젝트는 일상에서 기상청을 통해 날씨 정보는 쉽게 제공받지만, 꽃가루 농도에 관한 정보는 실시간으로 확인하기 어렵다는 문제의식에서 시작되었습니다. <br>
꽃가루 농도에 예민한 사람들은 이러한 정보 부족으로 인해 외출 시 불편함을 겪곤 합니다. 이 서비스는 특히 알레르기 환자나 꽃가루 민감자를 위한 것으로, 간단한 조회만으로도 기온 및 날씨 정보를 조회할 수 있는 것 처럼 꽃가루 농도 지수를 제공하여 외출 시 미리 대비할 수 있도록 돕고자 개발되었습니다.

## 💿 프로젝트 아키텍처
![MQTT Protocol Architecture](https://github.com/user-attachments/assets/9d5949c0-8dc1-4bd0-ab17-11a4cc87b368)

## 📱 기능 설명
![image](https://github.com/user-attachments/assets/58abfe18-bb9a-46b9-8ff7-731c89b474cf) <br>
성공적으로 DB 정보가 전달이 되면 구현된 HTML 홈페이지가 인코딩되어 화면에 출력 <br>

![image](https://github.com/user-attachments/assets/da9f5bd9-600f-4ce8-b0a5-347d5b2f834c) <br>
농도 값은 해당 이미지 처럼 0~2 형태로 출력 <br>

![image](https://github.com/user-attachments/assets/3ea16916-0255-4a5b-b621-ec5391fb0965) <br>
주의할점은 공공데이터포털에서 현재시간 기준에서 최근 1일간의 자료만 제공하기에 그 범위를 넘어선 날짜의 데이터를 받을 경우 Error 발생 <br>

![image](https://github.com/user-attachments/assets/85c81dbc-f6de-442e-a828-9b89afb206cb) <br>
N/A Error.
