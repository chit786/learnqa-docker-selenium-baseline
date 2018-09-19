// Require express and create an instance of it
var express = require('express');
var app = express();

// on the request to root (localhost:3000/)

var delay = 500;

app.post('/start', function (req, res) {
    setTimeout((function() {res.send("430")}), delay);
    // res.send("430");
});

// start the server in the port 3000 !
app.listen(3000, function () {
    console.log('rest client listening on port 3000.');
});

