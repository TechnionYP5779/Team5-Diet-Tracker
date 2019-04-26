mysql = require("mysql");

function du(user,action){
    var pool = this.pool;
    var dbHandler = this.handler;
    var sql = "DELETE FROM users WHERE email =" + mysql.escape(user.email);
    pool.query(sql, function(err, res) {
      dbHandler({ error: err, result: res }, action);
    });
}


module.exports = { deleteUser: du };