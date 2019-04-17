function addFood(food, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var sql =
    "INSERT INTO food (food_name,Protein,Fat,Carbohydrate,Calorie,Lactose,Alcohol,Caffeine,Fiber,Calcium,Iron,Sodium,Vitamin_A,Vitamin_E,Vitamin_D,Vitamin_C,Vitamin_B_6,Vitamin_B_12,Folic_acid,Cholesterol) VALUES ?";
  var values = 
    [
      food["food_name"],
      food["Protein"],
      food["Fat"],
      food["Carbohydrate"],
      food["Calorie"],
      food["Lactose"],
      food["Alcohol"],
      food["Caffeine"],
      food["Fiber"],
      food["Calcium"],
      food["Iron"],
      food["Sodium"],
      food["Vitamin_A"],
      food["Vitamin_E"],
      food["Vitamin_D"],
      food["Vitamin_C"],
      food["Vitamin_B_6"],
      food["Vitamin_B_12"],
      food["Folic_acid"],
      food["Cholesterol"]
    ].map((nut)=>(
     nut === undefined ? 0 : nut 
    ));
  values=[values]
  pool.query(sql, [values], function(err) {
    dbHandler({ error: err }, action);
  });
}

function addUser(user, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  sql = "INSERT INTO users (email) VALUES ?";
  var values = [[user.email]];
  pool.query(sql, [values], function(err) {
    dbHandler({ error: err }, action);
  });
}

function addUserAte(email, food_name, amount, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  var d = new Date();
  sql = "INSERT INTO ate (email, food_name, at_time, amount) VALUES ?";
  var values = [
    [
      email,
      food_name,
      d.getFullYear() +
        "-" +
        (d.getMonth() + 1) +
        "-" +
        d.getDate() +
        " " +
        d.getHours() +
        ":" +
        d.getMinutes() +
        ":" +
        d.getSeconds(),
      amount
    ]
  ];
  pool.query(sql, [values], function(err) {
    dbHandler({ error: err }, action);
  });
}

module.exports = {
  addUser: addUser,
  addFood: addFood,
  addUserAte
};
