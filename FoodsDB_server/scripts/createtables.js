var dbMaker= require("../src/database/DietTrackerDB")
var db = new dbMaker("localhost", "root", "PassWord555", "tests");

var action={
    success: function(){},
    failure: function(err){console.log(err);}
};

db.createUsers(action);
db.createFood(action);
db.createAte(action);