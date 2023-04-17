# dividend

## 참고사항

스프링 부트 버전 업으로 'Basic' attribute type should not be a container 정책이 적용되어 강의 코드로 동일하게 진행했을 때 에러가 났습니다.
signup 시 아래와 같이 보내주셔야 정상 동작 가능합니다 !

### ROLE_WRITE 의 경우
{
    "username" : "haden123",
    "password" : "haden123",
    "role" : "ROLE_WRITE"
}

### ROLE_READ의 경우
{
    "username" : "haden123",
    "password" : "haden123",
    "role" : "ROLE_READ"
}

<img width="413" alt="스크린샷 2023-04-17 오후 7 32 57" src="https://user-images.githubusercontent.com/80521474/232460597-eca49070-8905-4892-bde9-26fef18cd198.png">

<img width="529" alt="스크린샷 2023-04-17 오후 7 35 52" src="https://user-images.githubusercontent.com/80521474/232461039-bbd9c878-57b5-4289-bc23-65bddbc100db.png">


