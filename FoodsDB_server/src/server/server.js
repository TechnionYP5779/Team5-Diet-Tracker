var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var DB = require("../database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new DB(credInfo.host, credInfo.user, credInfo.password, "dtdb");

app.listen(8080, function() {
  console.log("Example app listening on port 8080!");
});

app.use(
  bodyParser.urlencoded({
    extended: true
  })
);
app.use(bodyParser.json());

var postAction = function(res){
  this.failure = function(err){res.json({ result: err});};
  this.success= function(){res.json({ result: "OK" })};
};

var getAction = function(res){
  this.failure = function(err){res.json({ result: err.result });};
  this.success= function(result){res.json( result[0])};
};

var deleteAction = postAction;

app.post("/user", function(req, res) {
  db.addUser(req.body,new postAction(res));
});

app.post("/food", function(req, res) {
  db.addFood(req.body,new postAction(res));
});

app.post("/ate", function(req, res) {
  db.searchFood(req.body.food_name.split(" ").filter(x=>x!==""),{
    success: function(result){
      db.addUserAte(req.body.email,result, req.body.amount, new postAction(res));
    },
    failure: function(err){
      res.json({ result: err.result });
    }
  });
});

app.get("/user", function(req, res) {
  db.getUser(req.query, new getAction(res))
});

app.get("/atetoday", function(req, res) {
  db.getAteTodayAmount(req.query.email, new getAction(res));
});


app.delete("/user",function(req,res){
  db.deleteUser(req.body,new deleteAction(res));
})

// error 404
app.get('*', function(req, res){
  res.send({result: "incorrect routing"});
});

app.post('*', function(req, res){
  res.send({result: "incorrect routing"});
});

app.delete('*', function(req, res){
  res.send({result: "incorrect routing"});
});

app.put('*', function(req, res){
  res.send({result: "incorrect routing"});
});
