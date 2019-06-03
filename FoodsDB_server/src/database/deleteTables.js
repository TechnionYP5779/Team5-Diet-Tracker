function du(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql = "DROP TABLE users";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}

function df(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql = "DROP TABLE food";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}

function da(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql = "DROP TABLE ate";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}
module.exports = {
  deleteUsers: du,
  deleteFood: df,
  deleteAte: da
};
