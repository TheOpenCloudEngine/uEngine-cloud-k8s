# k8s-UI

## node.js install
https://nodejs.org/ko/download/

필요에 따라서 node.js 설치.


<<<<<<< HEAD
## Project setup
1. 
1. 1. aa  

=======
### Project setup
```
npm install
```
>>>>>>> refs/remotes/origin/master

1. aaaa  
aa 
aa 
aa  
aa
1. aa  aa
   aa
### Compiles and hot-reloads for development
```
npm run serve
```

### EventStroming

#### 1. Event  
  1.1. 이벤트, '발생된 일' 이라고하며, 보통 ~됨 이라 과거 시제를 가집니다.  
  
    cf) 상품이 등록됨  
        상품이 삭제됨  
        상품이 조회됨  
        상품이 수정됨    
  
#### 2. Policy  
  2.1 규칙, 이벤트 스토밍에 협력을 통한 규칙을 정함을 위해 존재합니다.  
  
    cf) 상품정보는 상품명, 가격, 재고만 조회 된다.   
  
#### 3. Command
  3.1 명령, 이벤트 및 사용자가 발생시키는 명령을 나타낼때 사용됩니다.  
  
    cf) 상품 정보 조회,
        상품 정보 삭제
        상품 정보 추가
        
#### 4. Aggregate
   4.1 집합, 이벤트와 정보에 관하여 관리및 저장합니다.  
   
     cf) 상품 정보 List
     
#### 5. External
   5.1 외부 서비스, 외부로 송수신 정보를 나타내기 위한 기능을 합니다.
   
     cf) 상품의 정보제공은 각 고객사의 정보 수신
     
#### 6. View
   6.1 조회, 이벤트의 정보를 볼 수 있습니다.
   
    cf) 상품 목록 조회
        상품 재고 조회
       
#### 7. Bounded Context  
   7.1 경계선, 하나의 서비스를 묶이 위하여, 사용 됩니다.  
   
    cf) 상품 관리 하는 서비스


### Event
#### 내용
  1.1 상품 추가, '상품이 추가됨'이라는 Event 로 추가 되었을때 발생 되는 Evnet.   
  이벤트 명을 작성하고, API을 설정 해야합니다. (*자동적으로 aggregate 연결)
   
![image](https://user-images.githubusercontent.com/48536615/59992844-b51ff580-9688-11e9-8ac5-e8ad86f73132.png)

### Policy
#### 내용
  1.1 서비스 규칙, 각각의 서비스 및 이벤트 사이에서 규칙을 정하기 위한 Policy   
  규칙을 정의하고, API을 설정 해야합니다. (*자동적으로 aggregate 연결)
  
![image](https://user-images.githubusercontent.com/48536615/59993669-07aee100-968c-11e9-8957-dce225c29456.png)

### Command
#### 내용
  1.1 명령어, '상품이 추가'라는 Command 실행및 명령어 수행 하기 위한 Command   
  이벤트 명을 작성하고, API을 설정 해야합니다. (*자동적으로 aggregate 연결)
   
![image](https://user-images.githubusercontent.com/48536615/59994259-29a96300-968e-11e9-8384-809ba3236867.png)

### Aggregate
#### 내용
  1.1 집합, 상품이라는 정보를 저장및 사용하기 위한 집합체로 하나의 서비스의 DB 같은 역할을 하는 Aggregate    
  집함명 작성, API을 설정, 정보저장 Entity 설정, 연결 정보를 보여줍니다.
   
![image](https://user-images.githubusercontent.com/48536615/59988931-df1cec00-9677-11e9-8918-bd73c35f9739.png)   
![image](https://user-images.githubusercontent.com/48536615/59986310-4a16f480-9671-11e9-9f3a-25c678f5b063.png)

### Exteranl
#### 내용
  1.1 외부 서비스, 상품 서비스에서 외부 서비스를 필요 하거나, 정보가 필요 할때 사용 하는 External    
  외부 서비스 명 작성 (*자동적으로 aggregate 연결)
  
 ![image](https://user-images.githubusercontent.com/48536615/59994703-cf110680-968f-11e9-9313-4bfd6ef0ebff.png)

### View
#### 내용
  1.1 조회, 상품을 리스트를 조회하거나, Read 를 할 때 필요한 View   
   View 명을 작성하고, API(GET) 을 설정 해야합니다. (*자동적으로 aggregate 연결)
   
![image](https://user-images.githubusercontent.com/48536615/59994853-52caf300-9690-11e9-853a-79d05f5bc8bf.png)

### Bounded Context
#### 내용
  1.1 서비스 단위, 하나의 서비스를 묶이기 위한 Boundary.
  Bounded 명을 작성 해야합니다. 
 
![image](https://user-images.githubusercontent.com/48536615/59994972-c0771f00-9690-11e9-89cd-9c2d93721f67.png)



### Common Function  

#### Save  
    1. 정보를 저장하기 위하여 JSON 형식으로 값 저장.  
![image](https://user-images.githubusercontent.com/48536615/59987060-53559080-9674-11e9-939c-c46e503ca6c8.png)  

#### Upload  
    1. JSON 저장 값을 Upload  
![image](https://user-images.githubusercontent.com/48536615/59987015-2e611d80-9674-11e9-8315-bfbdba6da41c.png)  

#### undo & redo  
  1.1 undo: ctrl +C  
  1.2 redo: Ctrl + shift + C  
 
#### How to Connect Elements  
![image](https://user-images.githubusercontent.com/48536615/59995182-6aef4200-9691-11e9-9ab3-fbc328be3cf4.png)  


### Build  
#### Bulid
 ZIP 파일 생성.
```
<img src=" " width="90%"></img>

```
