# How to update a user's status in realtime in JavaScript

"Hey, what's up?" is not a phrase we need to ask someone these days. These days knowing what someone is up to has become so easy that we keep seeing updated status for all our friends on Whatsapp, Snapchat, Facebook etc. In this blog post, we will learn how can we build a *Realtime user's status update* component along with a list of all members who are online.

We will be using **NodeJS** as the application server, **Vanilla JS** in the front end and **Pusher** for realtime communication between our server and front end.

We will build an app which will be like your friends list or a common chat room where you can see who's online and their latest status update in realtime. In the blog post, we will learn about Pusher's **presence** channel and how to know about the online members to this channel.

![](https://dl.dropboxusercontent.com/s/af8xcqhhpa4v4rf/build-users-status-app-animated.gif)

We will be building the following components during this blog post:

- NodeJS Server using **ExpressJS Framework**
    - **/register** API - In order to register/login a new user to our channel and server by creating their session and saving their info
    - **/isLoggedIn** API - To check if a user is already logged in or not in case of refreshing the browser
    - **/usersystem/auth** API - Auth validation done by Pusher after registering it with our app and on subscribing to a presence or private channel
    - **/logout** API - To logout the user and remove the session
 
- Front End App using **Vanilla Javascript**
    - Register/Login Form - To register/login a new user by filling in their username and initial status
    - Members List - To see everyone who is online and their updated status
    - Update Status - To click on the existing status and update it on blur of the status text edit control
 
 
# Introduction to Pusher

Pusher is a platform which abstracts the complexities of implementing a realtime system on our own using Websockets or Long Polling. We can instantly add realtime features to our existing web applications using Pusher as it supports a wide variety of SDKs. Integration kits are available for variety of front end libraries like **Backbone, React, Angular, jQuery etc** and also backend platforms/languages like **.NET, Java, Python, Ruby, PHP, GO etc**.

## Signing up with Pusher

You can create a free account in Pusher [here](http://pusher.com/signup). After you signup and login for the first time, you will be asked to create a new app as seen in the picture below. You will have to fill in some information about your project and also the front end library or backend language you will be building your app with.

![](https://dl.dropboxusercontent.com/s/4d5nlhj9akuh5tl/build-users-status-update-create_app.png)

For this particular blog post, we will be selecting **Vanilla JS** for the front end and **NodeJS** for the backend as seen in the picture above. This will just show you a set of starter sample codes for these selections, but you can use any integration kit later on with this app.

![](https://dl.dropboxusercontent.com/s/xqqfwqe48x1lfeh/build-users-status-update-getting_started_view.png)

# NodeJS Server

NodeJS should be installed in the system as a prerequisite to this. Now let us begin building the NodeJS server and all the required APIs using **Express**. Initialise a new node project by the following command

``` bash
npm init
```
## Installing Dependencies

We will be installing the required dependencies like Express, express-session, Pusher, body-parser, cookie-parser by the following command:

``` bash
npm install express express-session body-parser cookie-parser --save
```
## Foundation Server

We will now create the basic foundation for Node Server and also enable sessions in that using express-session module.

``` javascript
var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');
var expressSession = require('express-session');
var cookieParser = require('cookie-parser');

var app = express();

// must use cookieParser before expressSession
app.use(cookieParser());

app.use(expressSession({
  secret:'<some-secret-token-here>',
  resave: true, 
  saveUninitialized: true
}));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

// Error Handler for 404 Pages
app.use(function(req, res, next) {
    var error404 = new Error('Route Not Found');
    error404.status = 404;
    next(error404);
});

module.exports = app;

app.listen(9000, function(){
  console.log('Example app listening on port 9000!')
});
```
In the above code, we have created a basic Express server and using the method **.use** we have enabled cookie-parser, body-parser and a static file serving from **public** folder. We have also enabled sessions using **express-session** module. This will enable us to save user information in the appropriate request session for the user.

## Adding Pusher

Pusher has an open source NPM module for **NodeJS** integrations which we will be using. It provides a set of utility methods to integrate with **Pusher** APIs using a unique appId, key and a secret. We will first install the Pusher `npm` module using the following command:

``` bash
npm install pusher --save
```
Now, we can use 'require' to get the Pusher module and to create a new instance passing an options object with important keys to initialise our integration. For this blog post, I have put random keys; you will have to obtain it for your app from the Pusher dashboard.

``` javascript
var Pusher = require('pusher');

var pusher = new Pusher({
  appId: '30XXX64',
  key: '82XXXXXXXXXXXXXXXXXb5',
  secret: '7bXXXXXXXXXXXXXXXX9e',
  encrypted: true
});

var app = express();
...
```
You will have to replace the **appId**, **key** and a **secret** with values specific to your own app. After this, we will write code for a new API which will be used to create a new comment.

## Register/Login API

Now, we will develop the first API route of our application through which a new user can register/login itself and make itself available on our app.

``` javascript
app.post('/register', function(req, res){
  console.log(req.body);
  if(req.body.username && req.body.status){
    var newMember = {
      username: req.body.username,
      status: req.body.status
    }
    req.session.user = newMember;
    res.json({  
      success: true,
      error: false
    });
  }else{
    res.json({  
      success: false,
      error: true,
      message: 'Incomplete information: username and status are required'
    });
  }
});
```
In the above code, we have exposed a POST API call on the route **/register** which would expect **username** and **status** parameters to be passed in the request body. We will be saving this user info in the request session.

## User System Auth API

In order to enable any client subscribing to Pusher **Private** and **Presence** channels, we need to implement an auth API which would authenticate the user request by calling **Pusher.authenticate** method at the server side. Add the following code in the server in order to fulfil this condition:

``` javascript
app.post('/usersystem/auth', function(req, res) {
  var socketId = req.body.socket_id;
  var channel = req.body.channel_name;
  var currentMember = req.session.user;
  var presenceData = {
    user_id: currentMember.username,
    user_info: {
      status: currentMember.status,
    }
  };
  var auth = pusher.authenticate(socketId, channel, presenceData);
  res.send(auth);
});
```
We need to provide the specific route in the initialisation of **Pusher Client** side library which we will see later in this blog post. The Pusher client library will automatically call this route and pass in the channel_name and socket_id properties. We will simultaneously get the user information from the user session object and pass it as presenceData to the **Pusher.authenticate** method call.

## IsLoggedIn and Logout API

If the user refreshes the browser, the client side app should detect if the user is already registered or not. We will implement an **isLoggedIn** API route for this. Also, we need a **logout** route to enable any user to logout from the app.

``` javascript
app.get('/isLoggedIn', function(req,res){
  if(req.session.user){
    res.send({ 
      authenticated: true 
    });
  }else{
    res.send({ authenticated: false });
  }
});

app.get('/logout', function(req,res){
  if(req.session.user){
    req.session.user = null;
  }
  res.redirect('/');
});
```
# Front End App using Vanilla JS

We will be developing the front end app now to register a new user with an initial status, see the members who are online and their statuses. We will also build the feature for the logged in user to update their users and all other users will see the updated status in realtime.

## Step 1: Create a folder named public and create an index.html

We have already written code in our `server.js` to serve static content from `public` folder, so we will write all our front end code in this folder.

Please create a new folder `public` and also create an empty `index.html` for now.

## Step 2: Add Boilerplate Code to our index.html

We will be adding some basic boilerplate code to set up the base structure for our web app like Header, Sections where registration form and the members list can be placed.

``` javascript
<!DOCTYPE>
<html>
    <head>
        <title>Whats Up ! Know what other's are up to in Realtime !</title>
        <link rel="stylesheet" href="https://unpkg.com/purecss@0.6.2/build/pure-min.css" integrity="sha384-UQiGfs9ICog+LwheBSRCt1o5cbyKIHbwjWscjemyBMT9YCUMZffs6UqUTd0hObXD" crossorigin="anonymous">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway:200">
        <link rel="stylesheet" href="./style.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <header>
            <div class="logo">
                <img src="./assets/pusher-logo.png" />
            </div>
            <div id="logout" class="logout">
               <a href="/logout">Logout</a>
            </div>
        </header>
        <section class="subheader">
            <img class="whatsup-logo" src="./assets/whatsup.png" />
            <h2>Whats Up ! Know what other's are up to in Realtime !</h2>
        </section>
        <section>
           <div id="loader" class="loader">
           </div>
           <script id="member-template" type="text/x-template">
            </script>
           <div id="me" class="me">
           </div>
           <div id="membersList" class="members-list">
           </div>
           <div id="signup-form" class="tab-content">
                <div class="header">
                    <div><img src="./assets/comments.png"></div>
                    <div class="text">First Time Sign Up !</div>
                </div>
                <form class="pure-form" id="user-form">
                    <div class="signup-form">
                        <div class="left-side">
                            <div class="row">
                                <input type="text" required placeholder="enter a username or displayname" id="display_name">
                            </div>
                            <div class="row">
                                <textarea placeholder="enter initial status text" required id="initial_status" rows="3"></textarea>
                            </div>
                        </div>
                        <div class="right-side">
                            <button 
                                type="submit" 
                                class="button-secondary pure-button">Signup/Login</button>
                        </div>
                    </div>
                </form>
            </div>
        </section>
        <script src="https://js.pusher.com/4.0/pusher.min.js"></script>
        <script type="text/javascript" src="./app.js"></script>
    </body>
</html>
```
In the above boilerplate code, we have referenced our main Javascript file **app.js** and the Pusher client side JS library. We also have a script tag where we will place the template for a member row in the member list. Also, we have two empty div tags with ids **me** and **membersList** to contain the logged in member name and info, as well as the list of all other members with their statuses.

### Step3: Style.css

Important to note that we will be showing the signup form for the first time and the MembersList and Logout button will be hidden by default initially. Please create a new file called **style.css** and add the following css to it:

``` css
body{
    margin:0;
    padding:0;
    overflow: hidden;
    font-family: Raleway;
}

header{
    background: #2b303b;
    height: 50px;
    width:100%;
    display: flex;
    color:#fff;
}


.loader,
.loader:after {
  border-radius: 50%;
  width: 10em;
  height: 10em;
}
.loader {
  margin: 60px auto;
  font-size: 10px;
  position: relative;
  text-indent: -9999em;
  border-top: 1.1em solid rgba(82,0,115, 0.2);
  border-right: 1.1em solid rgba(82,0,115, 0.2);
  border-bottom: 1.1em solid rgba(82,0,115, 0.2);
  border-left: 1.1em solid #520073;
  -webkit-transform: translateZ(0);
  -ms-transform: translateZ(0);
  transform: translateZ(0);
  -webkit-animation: load8 1.1s infinite linear;
  animation: load8 1.1s infinite linear;
}
@-webkit-keyframes load8 {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes load8 {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}


.subheader{
    display: flex;
    align-items: center;
    margin: 0px;
}

.whatsup-logo{
    height:60px;
    border-radius: 8px;
    flex:0 60px;
    margin-right: 15px;
}

.logout{
    flex:1;
    justify-content: flex-end;
    padding:15px;
    display: none;
}

.logout a{
    color:#fff;
    text-decoration: none;
}

#signup-form{
    display: none;
}

input, textarea{
  width:100%;
}


section{
    padding: 0px 15px;
}

.logo img{
    height: 35px;
    padding: 6px;
    margin-left: 20px;
}

#updateStatus{
    display: none;
}

.members-list{
    display: none;
    flex-direction: column;
}

.me {
  display: none;
}

```
Please try to open the URL **http://localhost:9000** in your browser and the application will load with the basic register or login form with username and status. The output will look like the screenshot below:

![](https://dl.dropboxusercontent.com/s/bwsl0wqek8k22gc/build-users-status-signup-form2.png)

### Step 4: Add app.js basic code

Now we will add our Javascript code to have basic utility elements inside a self invoking function to create a private scope for our app variables. We do not want to pollute JS global scope.

``` javascript
// Using IIFE for Implementing Module Pattern to keep the Local Space for the JS Variables
(function() {
    // Enable pusher logging - don't include this in production
    Pusher.logToConsole = true;

    var serverUrl = "/",
        members = [],
        pusher = new Pusher('73xxxxxxxxxxxxxxxdb', {
          authEndpoint: '/usersystem/auth',
          encrypted: true
        }),
        channel,
        userForm = document.getElementById("user-form"),
        memberTemplateStr = document.getElementById('member-template').innerHTML;

    function showEle(elementId){
      document.getElementById(elementId).style.display = 'flex';
    }

    function hideEle(elementId){
      document.getElementById(elementId).style.display = 'none';
    }

    function ajax(url, method, payload, successCallback){
      var xhr = new XMLHttpRequest();
      xhr.open(method, url, true);
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.onreadystatechange = function () {
        if (xhr.readyState != 4 || xhr.status != 200) return;
        successCallback(xhr.responseText);
      };
      xhr.send(JSON.stringify(payload));
    }

    ajax(serverUrl+"isLoggedIn","GET",{},isLoginChecked);

    function isLoginChecked(response){
      var responseObj = JSON.parse(response);
      if(responseObj.authenticated){
        channel = pusher.subscribe('presence-whatsup-members');
        bindChannelEvents(channel);
      }
      updateUserViewState(responseObj.authenticated);
    }

    function updateUserViewState(isLoggedIn){
      document.getElementById("loader").style.display = "none";
      if(isLoggedIn){
        document.getElementById("logout").style.display = "flex";
        document.getElementById("signup-form").style.display = "none";
      }else{
        document.getElementById("logout").style.display = "none";
        document.getElementById("signup-form").style.display = "block";
      }
    }

    function showLoader(){
        document.getElementById("loader").style.display = "block";
        document.getElementById("logout").style.display = "none";
        document.getElementById("signup-form").style.display = "none";
    }

    // Adding a new Member Form Submit Event
    userForm.addEventListener("submit", addNewMember);


    function addNewMember(event){
      event.preventDefault();
      var newMember = {
        "username": document.getElementById('display_name').value,
        "status": document.getElementById('initial_status').value
      }
      showLoader();
      ajax(serverUrl+"register","POST",newMember, onMemberAddSuccess);
    }

    function onMemberAddSuccess(response){
        // On Success of registering a new member
        console.log("Success: " + response);
        userForm.reset();
        updateUserViewState(true);
        // Subscribing to the 'presence-members' Channel
        channel = pusher.subscribe('presence-whatsup-members');
        bindChannelEvents(channel);
    }
})();
```
In the above code, we have referenced all the important variables we will be requiring. We will also initialise the Pusher library using **new Pusher** and passing the api key as the first argument. The second argument contains an optional config object in which we will add the key **authEndpoint** with the custom node api route **/usersystem/auth** and also add the key **encrypted** setting it to value true.

We will create a couple of generic functions to show or hide an element passing its unique id. We have also added a common method named **ajax** to make ajax requests using XMLHttp object in vanilla Javascript.

At the load of the page we make an ajax request to check if the user is logged in or not. If the user is logged in, we will directly use the Pusher instance to subscribe the user to a presence channel named **presence-whatsup-members** , you can have this as the unique chat room or app location where you want to report/track the online members.

We have also written a method above to **addNewMember** using an ajax request to the **register** api route we have built in NodeJS. We will be passing the name and initial status entered into the form.

We also have a method to update the user view state based on the logged in status. This method does nothing but updates the visibility of members list, logout button and signup form. We have used a **bindChannelEvents** method when the user is logged in which we will be implementing later in the blog post.

Please add the following css in **style.css** file to display the **me** element appropriately with the username and the status of the logged in user.

``` css

.me {
    border:1px solid #aeaeae;
    padding:10px;
    margin:10px;
    border-radius: 10px;
}

.me img{
    height: 40px;
    width: 40px;
}

.me .status{
    padding:5px;
    flex:1;
}

.me .status .username{
    font-size:13px;
    color: #aeaeae;
    margin-bottom:5px;
}

.me .status .text{
    font-size: 15px;
    width:100%;
    -webkit-transition: all 1s ease-in 5ms;
    -moz-transition: all 1s ease-in 5ms;
    transition: all 1s ease-in 5ms;
}
```
## Step 5: Add code to render the members list and bindChannelEvents

Now, after subscribing to the channel, we need to bind certain events so that we can know whenever a new member is added to the channel or removed from it. We will also bind to a custom event to know whenever someone updates their status.

Add the following code to the **app.js** file:

``` javascript
// Binding to Pusher Events on our 'presence-whatsup-members' Channel

  function bindChannelEvents(channel){
      channel.bind('client-status-update',statusUpdated);
      var reRenderMembers = function(member){
        renderMembers(channel.members);
      }
      channel.bind('pusher:subscription_succeeded', reRenderMembers);
      channel.bind('pusher:member_added', reRenderMembers);
      channel.bind('pusher:member_removed', reRenderMembers);
    }


```
In the above **bindChannelEvents** method, we use the **channel.bind** method to bind event handlers for 3 internal events - **pusher:subscription_succeeded**, **pusher:member_added**, **pusher:member_removed** and 1 custom event - **client-status-update**.

Now we will add the Javascript code to render the list of members. It is important to know that the object which i returned from the **.subscribe** method has a property called **members** which can be used to know the information about the logged in user referred by the key **me** and other members by key **members**. Add the following code to **app.js** file

``` javascript

// Render the list of members with updated data and also render the logged in user component

    function renderMembers(channelMembers){
      var members = channelMembers.members;
      var membersListNode = document.createElement('div');
      showEle('membersList');

      Object.keys(members).map(function(currentMember){
        if(currentMember !== channelMembers.me.id){
          var currentMemberHtml = memberTemplateStr;
          currentMemberHtml = currentMemberHtml.replace('{{username}}',currentMember);
          currentMemberHtml = currentMemberHtml.replace('{{status}}',members[currentMember].status);
          currentMemberHtml = currentMemberHtml.replace('{{time}}','');
          var newMemberNode = document.createElement('div');
          newMemberNode.classList.add('member');
          newMemberNode.setAttribute("id","user-"+currentMember);
          newMemberNode.innerHTML = currentMemberHtml;
          membersListNode.appendChild(newMemberNode);
        }
      });
      renderMe(channelMembers.me);
      document.getElementById("membersList").innerHTML = membersListNode.innerHTML;
    }


    function renderMe(myObj){
      document.getElementById('myusername').innerHTML = myObj.id;
      document.getElementById('mystatus').innerHTML = myObj.info.status;
    }
```
We have added the event handler for new member add/remove event to re-render the members list so that it remains updated with the online members only. In order to show the members list we need to add the following style into our file **style.css**

``` css

.member{
    display: flex;
    border-bottom: 1px solid #aeaeae;
    margin-bottom: 10px;
    padding: 10px;
}

.member .user-icon{
    flex:0 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.member .user-icon img{
    width:50px;
    height:50px;
}

.member .user-info{
    padding:5px;
    margin-left:10px;
}

.member .user-info .name{
    font-weight: bold;
    font-size: 16px;
    padding-bottom:5px;
}

.member .user-info .status{
    font-weight: normal;
    font-size:13px;
}

.member .user-info .time{
    font-weight: normal;
    font-size:10px;
    color:#aeaeae;
}
```
Now we will write the code, to trigger a client event on our channel to notify all users about the status change of the logged in user. Add the following code to your **app.js** file

``` javascript
 // On Blur of editting my status update the status by sending Pusher event
    document.getElementById('mystatus').addEventListener('blur',sendStatusUpdateReq);

    function sendStatusUpdateReq(event){
      var newStatus = document.getElementById('mystatus').innerHTML;
      var username = document.getElementById('myusername').innerText;
      channel.trigger("client-status-update", {
        username: username,
        status: newStatus
      });
    }

    // New Update Event Handler
    // We will take the Comment Template, replace placeholders and append to commentsList
    function statusUpdated(data){
      var updatedMemberHtml = memberTemplateStr;
          updatedMemberHtml = updatedMemberHtml.replace('{{username}}',data.username);
          updatedMemberHtml = updatedMemberHtml.replace('{{status}}',data.status);
          updatedMemberHtml = updatedMemberHtml.replace('{{time}}','just now');
      document.getElementById("user-"+data.username).style.color = '#1B8D98';    
      document.getElementById("user-"+data.username).innerHTML=updatedMemberHtml;
      setTimeout(function(){
        document.getElementById("user-"+data.username).style.color = '#000';
      },500);
    }
```
***IMPORTANT***: When we run this code in our browsers, update the status and blur out of the status control, we will get an error in the Javascript console for the Pusher library. To fix this, go to the console at **Pusher.com** website, go to settings and enable sending events from clients directly.

We can only send events from client sdirectly for Presence or Private channels. Link to the official documentation - [https://Pusher.com/docs/client_api_guide/client_events#trigger-events](https://pusher.com/docs/client_api_guide/client_events#trigger-events)

``` javascript
Pusher : Error : {
  "type":"WebSocketError",
  "error":{
    "type":"PusherError",
    "data":
      {
        "code":null,
        "message":"To send client events, you must enable this feature in the Settings page of your dashboard."
      }
   }
}
```
Link to the Github Repository for reference:

[https://github.com/mappmechanic/whats-up-realtime-status-update](https://github.com/mappmechanic/whats-up-realtime-status-update)

# Conclusion

We have built an application which will display all the online members for a particular presence channel and their statuses. If any of the online user updates their status, every user will be notified about the updated status.

This component or code can be used for developing a social networking section in most of the web apps these days. It is an important use case where the user needs to know about other available participants. For example: an online classroom app can see the other participants and the status can correspond to any question any participant wants to ask the presenter.

![](https://dl.dropboxusercontent.com/s/af8xcqhhpa4v4rf/build-users-status-app-animated.gif)

We have just used **NodeJS** and **Vanilla JS** to implement the above functionality. You can use the Javascript for front end code with any popular framework like **ReactJS** or **AngularJS** etc. The backend can also be **Java** or **Ruby**. Please refer to the Pusher docs for more information on this.
