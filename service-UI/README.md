# Event-Storming-tool

## node.js install
https://nodejs.org/ko/download/

필요에 따라서 node.js 설치.

### Project setup
```
npm install
```
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


## Detail
### Event
#### 내용
  1.1 상품 추가, '상품이 추가됨'이라는 Event 로 추가 되었을때 발생 되는 Evnet.   
  이벤트 명을 작성하고, API을 설정 해야합니다.
  <img width="1680" alt="Event" src="https://user-images.githubusercontent.com/54785805/67353053-95dc9a80-f58c-11e9-9c88-95b9f4b90890.png">
   
### Policy
#### 내용
  1.1 서비스 규칙, 각각의 서비스 및 이벤트 사이에서 규칙을 정하기 위한 Policy   
  규칙을 정의하고, API을 설정 해야합니다.
  <img width="1678" alt="Policy" src="https://user-images.githubusercontent.com/54785805/67353056-95dc9a80-f58c-11e9-8789-13fd7002bbd0.png">

### Command
#### 내용
  1.1 명령어, '상품이 추가'라는 Command 실행및 명령어 수행 하기 위한 Command   
  이벤트 명을 작성하고, API을 설정 해야합니다.
   
<img width="1374" alt="스크린샷 2019-10-23 오전 11 28 52" src="https://user-images.githubusercontent.com/54785805/67352119-10f08180-f58a-11e9-88fc-3bfea3c5d12e.png">

### Aggregate
#### 내용
  1.1 집합, 상품이라는 정보를 저장및 사용하기 위한 집합체로 하나의 서비스의 DB 같은 역할을 하는 Aggregate    
  집함명 작성, API을 설정, 정보저장 Entity 설정, 연결 정보를 보여줍니다.

<img width="1679" alt="스크린샷 2019-10-23 오후 12 03 37" src="https://user-images.githubusercontent.com/54785805/67353291-2d41ed80-f58d-11e9-8832-560f41cbffee.png">

### Exteranl
#### 내용
  1.1 외부 서비스, 상품 서비스에서 외부 서비스를 필요 하거나, 정보가 필요 할때 사용 하는 External    
  외부 서비스 명 작성
  <img width="1678" alt="Exteranl" src="https://user-images.githubusercontent.com/54785805/67353057-95dc9a80-f58c-11e9-93c9-3613c2639b65.png">

### View
#### 내용
  1.1 조회, 상품을 리스트를 조회하거나, Read 를 할 때 필요한 View   
   View 명을 작성하고, API(GET) 을 설정 해야합니다.
   

### Bounded Context
#### 내용
  1.1 서비스 단위, 하나의 서비스를 묶이기 위한 Boundary.
  Bounded 명을 작성 해야합니다. 
 
![image](https://user-images.githubusercontent.com/54785805/67352982-6b8add00-f58c-11e9-8312-1202ded275e9.png)



## Eelement Function
### Entitiy
![addEntitiy](https://user-images.githubusercontent.com/54785805/67350163-efd96200-f584-11e9-97ff-4ad62cc4d814.gif)

### Aggregate Connect
<img width="1370" alt="스크린샷 2019-10-23 오전 11 47 36" src="https://user-images.githubusercontent.com/54785805/67352456-008cd680-f58b-11e9-93a8-58a545f9ecbb.png">

### Automtion Entity Add when connect Aggregate.
<img width="1373" alt="스크린샷 2019-10-23 오전 11 51 58" src="https://user-images.githubusercontent.com/54785805/67352672-8e68c180-f58b-11e9-941e-076dbe42621e.png">



## Tool Function  
### Zoom In& Out
![resizeImage](https://user-images.githubusercontent.com/54785805/67350165-f071f880-f584-11e9-8b60-d90bb31b6007.gif)

### Save &  Upload  
    1. 정보를 저장하기 위하여 JSON 형식으로 값 저장.  
![Save](https://user-images.githubusercontent.com/54785805/67350166-f071f880-f584-11e9-893c-9103e67404b2.gif)
 
### undo & redo  
  1.1 undo: ctrl +C  
  1.2 redo: Ctrl + shift + C  
 
### Bulid
![archive](https://user-images.githubusercontent.com/54785805/67350164-f071f880-f584-11e9-89a2-9e9d1645b221.gif)
