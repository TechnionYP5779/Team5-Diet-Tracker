mysql = require("mysql");

function du(user,action){
    var pool = this.pool;
    var dbHandler = this.handler;
    var sql = "DELETE FROM users WHERE email =" + mysql.escape(user.email);
    pool.query(sql, function(err, res) {
      dbHandler({ error: err, result: res }, action);
    });
}

function df(food_name,action){
  var pool = this.pool;
    var dbHandler = this.handler;
    var sql = "DELETE FROM food WHERE food_name =" + mysql.escape(food_name);
    pool.query(sql, function(err, res) {
      dbHandler({ error: err, result: res }, action);
    });
}


module.exports = { deleteUser: du, deleteFood: df };