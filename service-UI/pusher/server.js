var express = require('express');
var path = require('path');
var cors = require('cors');
var bodyParser = require('body-parser');
var expressSession = require('express-session');
var cookieParser = require('cookie-parser');
const port = process.env.PORT || 4000;

var Pusher = require('pusher');

var pusher = new Pusher({
    encrypted: true,
    appId: '791580',
    key: '33169ca8c59c1f7f97cd',
    secret: 'fabf1e67ee01ec107f46',
    cluster: 'ap3'
});

var app = express();

var corsOptions = {
    origin: 'http://localhost:8081',
    credentials: true
};

app.use(cors(corsOptions));
app.use(expressSession({
    key: 'key', // 세션키
    secret: "secret", // 비밀키
    resave: true,
    saveUninitialized: true,
}))
;

// must use cookieParser before expressSession
app.use(cookieParser());

// app.all('/*', function(req, res, next) {
//     res.header("Access-Control-Allow-Origin", "*");
//     res.header("Access-Control-Allow-Headers", "X-Requested-With");
//     next();
// });

// app.use(expressSession({secret: 'test', ));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static(path.join(__dirname, 'public')));

app.listen(port, () => {
    console.log(`Server started on port ${port}`);
});

app.post('/register', function (req, res) {
    res.setHeader('Access-Control-Allow-Credentials', 'true')
    console.log("userName : " + req.body.userName + " & userId: " + req.body.userId)
    if (req.body.userName && req.body.userId) {
        var newMember = {
            userName: req.body.userName,
            userId: req.body.userId
        }
        req.session.user = newMember;
        res.json({
            success: true,
            error: false
        });
    } else {
        res.json({
            success: false,
            error: true,
            message: 'Incomplete information: username and status are required'
        });
    }
});

app.post('/usersystem/auth', function (req, res) {
    res.setHeader('Access-Control-Allow-Credentials', 'true')
    console.log(req.session)
    var sess = req.session;
    var socketId = req.body.socket_id;
    var channel = req.body.channel_name;
    var currentMember = req.session.user;
    var presenceData = {
        user_id: socketId,
        user_info: {
            userName: req.session.userName,
            userId: req.session.userId
        }
    };
    console.log(new Date().toLocaleString() + "Authenticating user: " + socketId + ":" + channel);
    console.log("With userName = " + req.session.userName + " and userID = " + req.session.userId);
    var auth = pusher.authenticate(socketId, channel, presenceData);
    console.log("response: " + '\n' + auth);
    res.send(auth);
});

app.get('/isLoggedIn', function (req, res) {
    if (req.session.user) {
        res.send({
            authenticated: true
        });
    } else {
        res.send({authenticated: false});
    }
});

app.get('/logout', function (req, res) {
    if (req.session.user) {
        req.session.user = null;
    }
});

app.post('/paint', (req, res) => {
    pusher.trigger('presence-event', 'draw', req.body);
    res.json(req.body);
});

// Error Handler for 404 Pages
// app.use(function(req, res, next) {
//     var error404 = new Error('Route Not Found');
//     error404.status = 404;
//     next(error404);
// });

module.exports = app;


