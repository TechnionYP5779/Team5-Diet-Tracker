var makeDB = require("./DietTrackerDB");
var db= new makeDB("localhost","root","PassWord555","tests");
//db.buildTables();
//db.dropTables();

//db.addUser({ email: "orfe@gmail.com", password: "password" },callback);

var food={
    food_name: "Beef, cured, pastrami",
    Protein: 4,
    Fat: 1.2
}

//db.addFood(food,callback);

db.addUserAte("orfe@gmail.com","pastrami",150,callback);

function callback(res){
   console.log(res);
}

//db.getUser({ email: "orfe@gmail.com", password: "password" },callback);


//db.getAteTodayAmount("orfe@gmail.com",callback);