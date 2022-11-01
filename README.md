## 구현스펙
- Language : Java 8
- Framework : Springboot 2.6.7
- DB : embedded H2
- TEST : Junit5
- ETC : JPA, Spring security
- TOOL : Gradle, Intellij

## 실행방법
압축을 푼 소스코드 루트로 이동해 
```
./gradlew build
cd build/libs/
java -jar user-0.0.1.jar
```

## 기능
- 전화번호인증
- 회원가입
- 로그인
- 내 정보 보기
- 비밀번호 재설정

## API
- Swagger를 적용시켜 놓아서 모든 API는 http://localhost:8080/swagger-ui.html 에서 테스트가 가능합니다.
- 모든 API의 response 형식은 통일되어있으며, 형태는 아래와 같습니다.
```
{
  "data": {},
  "message": "string",
  "status": true
}
```
- data : 반환할 데이터가 있는 경우 이 항목에 담겨서 반환됩니다.
- message : API가 정상적으로 작동되었을 경우 SUCCESS를, 예외가 발생했을 경우 에러메세지를 반환합니다.
- status : API가 정상적으로 작동되었을 경우 true를, 예외 발생시 false를 반환합니다.

## DB
- DB의 경우 소스를 실행 후 http://localhost:8080/h2-console/ 로 접속할 수 있습니다.
- 아래와 같이 설정 후 접속하시면 됩니다.
```
Saved Setting : Generic H2 (Embedded)
Setting Name : Generic H2 (Embedded)
Driver Class : org.h2.Driver
JDBC URL : jdbc:h2:~/test
User Name : sa
```

## 테스트

##### 연락처인증요청
- 인증코드를 요청하는 API 입니다.
- 기본적인 핸드폰번호 유효성검증을 진행하게 구현했습니다.
- 요청시엔 "-", "."가 포함될 수 있지만, 검증 진행시엔 자동적으로 제거되도록 구현했습니다. (DB 저장시엔 숫자만 저장)
```
주소 : http://localhost:8080/user/certificate/request
형식 : POST

parameter : 
{
  "phone" : "연락처"
}

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": "인증코드"
}
```

##### 연락처인증
- 위 API에서 요청했던 전화번호와 인증코드로 인증을 진행합니다.
- 인증코드를 발송한 뒤 5분이 지났을 때 요청정보가 사라지도록 구현하였습니다.
```
주소 : http://localhost:8080/user/certificate
형식 : POST

parameter : 
{
  "phone" : "연락처"
  "code" : "인증코드"
}

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": "인증이 완료된 연락처"
}
```

##### 회원가입
- 회원가입을 처리하는 API 입니다.
- 연락처는 인증요청 API에서 유효성 검사를 실시했기때문에 연락처에 대한 검증은 생략하였습니다.
- 연락처 인증 후 5분이 지나면 회원가입이 진행되지 않도록 구현하였습니다.
- 이메일, 닉네임, 이름의 형식과 비밀번호 형식에 대한 유효성 검증을 구현하였습니다.
- 닉네임, 이름에 대한 유효성 검증은 공백 문자를 포함한 문자열을 거르도록 했습니다. 
- 비밀번호 형식은 알파벳, 숫자, 특수문자가 1개 이상씩 포함된 8자리 이상의 문자열입니다.
- 이메일과 닉네임, 연락처에 대한 중복검사를 포함하고있습니다.
```
주소 : http://localhost:8080/user/signup
형식 : POST

parameter : 
{
  "email": "이메일",
  "name": "이름",
  "nickname": "닉네임",
  "password": "비밀번호",
  "phone": "연락처"
}

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": null
}
```

##### 로그인
- 로그인을 진행하는 API 입니다.
- 회원가입시 중복 체크를 했던 이메일, 닉네임, 연락처로 로그인이 가능하도록 구현했습니다.
- "내 정보 보기" API 통신을 위해 로그인시 jwt 토큰을 발급하도록 구현하였습니다.
```
주소 : http://localhost:8080/user/login
형식 : POST

parameter : 
{
  "value": "이메일 or 닉네임 or 연락처",
  "password": "비밀번호"
}

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": "jwt 토큰"
}
```

##### 내 정보 보기
- 내 정보를 보는 API 입니다.
- Spring security를 통해 권한이 없는 접근을 차단하도록 구현하였습니다.
```
주소 : http://localhost:8080/info/my
형식 : GET

header : 
  "Authorization" : "jwt 토큰"

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": "비밀번호를 제외한 나의 정보"
}
```

##### 비밀번호 재설정
- 비밀번호를 재설정하는 API 입니다.
- 연락처 인증을 진행 한 후 요청이 가능합니다.
```
주소 : http://localhost:8080/user/reset/password
형식 : POST

parameter : 
{
  "phone": "연락처"
}

response : 
{
  "status": true,
  "message": "SUCCESS",
  "data": "새로 발급된 비밀번호"
}
<<<<<<< HEAD
```
=======
```
>>>>>>> 6fea0dcc39fd4486166bbdf3fb73594c58f3d13d
