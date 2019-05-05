function cu(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql =
    "CREATE TABLE users (" +
    "email VARCHAR(255) NOT NULL," +
    "PRIMARY KEY (email)" +
    ")";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}

function cf(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql =
    "CREATE TABLE food (" +
    "food_name VARCHAR(255) NOT NULL," +
    "Protein DECIMAL(8,4) ," +
    "Fat DECIMAL(8,4)," +
    "Carbohydrate DECIMAL(8,4) ," +
    "Calorie DECIMAL(8,4)," +
    "Lactose DECIMAL(8,4)," +
    "Alcohol DECIMAL(8,4)," +
    "Caffeine DECIMAL(8,4)," +
    "Fiber DECIMAL(8,4)," +
    "Calcium DECIMAL(8,4)," +
    "Iron DECIMAL(8,4)," +
    "Sodium DECIMAL(10,4)," +
    "Vitamin_A DECIMAL(10,4)," +
    "Vitamin_E DECIMAL(8,4)," +
    "Vitamin_D DECIMAL(8,4)," +
    "Vitamin_C DECIMAL(8,4)," +
    "Vitamin_B_6 DECIMAL(8,4)," +
    "Vitamin_B_12 DECIMAL(8,4)," +
    "Folic_acid DECIMAL(8,4)," +
    "Cholesterol DECIMAL(8,4)," +
    "PRIMARY KEY (food_name)" +
    ")";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}

function ca(action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql =
    "CREATE TABLE ate (" +
    "email VARCHAR(255)," +
    "food_name VARCHAR(255)," +
    "at_time DATETIME NOT NULL," +
    "amount INT NOT NULL," +
    "FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE," +
    "FOREIGN KEY (food_name) REFERENCES food(food_name) ON DELETE CASCADE" +
    ")";
  pool.query(sql, function(err) {
    dbHandler({ error: err }, action);
  });
}

function cp(action){
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql="CREATE TABLE portion (" +
  "food_name VARCHAR(255)," +
  "measure VARCHAR(255)," +
  "weight INT NOT NULL," +
  "FOREIGN KEY (food_name) REFERENCES food(food_name) ON DELETE CASCADE" +
  ")";;
  pool.query(sql,function(err){
    dbHandler({error: err},action);
  })
}

module.exports = { createUsers: cu, creatFood: cf, createAte: ca ,createPortion: cp};
