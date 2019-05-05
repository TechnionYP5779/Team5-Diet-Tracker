mysql = require("mysql");

function getUser(user, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql = "SELECT * FROM users WHERE email = " + mysql.escape(user.email);
  pool.query(sql, function(err, res) {
    dbHandler({ error: err, result: res }, action);
  });
}

function getAteTodayAmount(email, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var d = new Date();
  d = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
  var start = d + " 0:0:0";
  var end = d + " 23:59:59";

  var sql =
    "(SELECT SUM(Protein*(amount/100)) AS Protein,SUM(Fat*(amount/100)) AS Fat,SUM(Carbohydrate*(amount/100)) AS Carbohydrate,SUM(Calorie*(amount/100)) AS Calorie,SUM(Lactose*(amount/100)) AS Lactose,SUM(Alcohol*(amount/100)) AS Alcohol,SUM(Caffeine*(amount/100)) AS Caffeine,SUM(Fiber*(amount/100)) AS Fiber,SUM(Calcium*(amount/100)) AS Calcium,SUM(Iron*(amount/100)) AS Iron,SUM(Sodium*(amount/100)) AS Sodium,SUM(Vitamin_A*(amount/100)) AS Vitamin_A,SUM(Vitamin_E*(amount/100)) AS Vitamin_E,SUM(Vitamin_D*(amount/100)) AS Vitamin_D,SUM(Vitamin_C*(amount/100)) AS Vitamin_C,SUM(Vitamin_B_6*(amount/100)) AS Vitamin_B_6,SUM(Vitamin_B_12*(amount/100)) AS Vitamin_B_12,SUM(Folic_acid*(amount/100)) AS Folic_acid,SUM(Cholesterol*(amount/100)) AS Cholesterol" +
    " FROM food AS F,ate AS A" +
    " WHERE A.email = " +
    mysql.escape(email) +
    " AND F.food_name = A.food_name" +
    " AND A.at_time BETWEEN " +
    mysql.escape(start) +
    " AND " +
    mysql.escape(end)+")"+
    "union "+
    "(select 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 from users where email="+mysql.escape(email)+")";

  pool.query(sql, function(err, res) {
      if(!err && res.length<2)
        err={result: "user with mail "+email+ " does not exist"};
    dbHandler({ error: err, result: (res[0].Protein===null) ? [res[1]] : [res[0]] }, action);
  });
}

module.exports = { getUser: getUser, getAteTodayAmount: getAteTodayAmount };
