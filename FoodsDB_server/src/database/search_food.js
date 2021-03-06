mysql = require("mysql");
p = require("pluralize");

function appeances(words, subFood) {
  var points = 0;
  var foodWords = subFood.split(" ");
  for (i = 0; i < words.length; i++) {
    var filtered = foodWords.filter(
      w =>
        w.toLowerCase() === words[i] || w.toLowerCase() === p.plural(words[i])
    );
    points += filtered.length;
  }
  return points;
}

function rankSubfood(words, doubleWords, subFood) {
  var points = appeances(words, subFood);
  var foodWords = subFood.split(" ").filter(w => w !== "");
  for (i = 0; i < doubleWords.length; i++) {
    points += subFood.toLowerCase().includes(doubleWords[i]) ? 1 : 0;
  }
  points = points / foodWords.length;
  return points;
}

function countWords(food, words) {
  function add(v, a) {
    return v + a;
  }
  return food
    .split(",")
    .map(sf => appeances(words, sf))
    .reduce(add, 0);
}

function rankFood(words, doubleWords, food) {
  var num_subfoods = [food.split(",").length + 1, countWords(food, words)];
  return num_subfoods.concat(
    food.split(",").map(f => rankSubfood(words, doubleWords, f))
  );
}

function compareRanks(food1, food2) {
  var r1 = food1.rank;
  var r2 = food2.rank;

  var max = Math.max(r1[0], r2[0]);
  var min = Math.min(r1[0], r2[0]);
  r1 = r1.concat(new Array(max - min).fill(0));
  r2 = r2.concat(new Array(max - min).fill(0));
  for (i = 1; i <= max; i++) {
    if (r1[i] > r2[i]) return food1;
    if (r2[i] > r1[i]) return food2;
  }
  if (r2[0] < r1[0]) return food2;
  return food1;
}

var seachQuery = function(words) {
  var query = "";
  for (i = 0; i < words.length; i++) {
    query +=
      "(select food_name from food where food_name like concat('%'," +
      mysql.escape(words[i]) +
      ",'%'))";
    query += i !== words.length - 1 ? " union " : "";
  }
  return query;
};
var searchQueryWithPortion = function(words, portion) {
  var query = "";
  query =
    "select food_name, weight from portion where measure like concat('%'," +
    mysql.escape(portion) +
    ",'%') AND (";
  for (i = 0; i < words.length; i++) {
    query += "food_name like concat('%'," + mysql.escape(words[i]) + ",'%')";
    query += i !== words.length - 1 ? " OR " : "";
  }
  query += " )";
  return query;
};

var searchFood = function(words, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  words = words.map(w =>
    p.isPlural(w) && /^[a-zA-Z]+$/.test(w) ? p.singular(w) : w
  );
  words = words.map(w => w.toLowerCase());

  var doubleWords = [];
  for (i = 0; i < words.length - 1; i++) {
    doubleWords.push(words[i] + " " + words[i + 1]);
  }
  query = seachQuery(words);
  pool.query(query, function(err, res) {
    var res;
    if (!err) {
      var ranked = res.map(function(f) {
        return {
          name: f.food_name,
          rank: rankFood(words, doubleWords, f.food_name)
        };
      });
      var bestFood = ranked.reduce(compareRanks, {
        rank: [13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
      });
      res = bestFood.name;
    }
    dbHandler({ error: err, result: res }, action);
  });
};

var searchFoodWithPortion = function(words, portion, action) {
  var pool = this.pool;
  var dbHandler = this.handler;
  words = words.map(w =>
    p.isPlural(w) && /^[a-zA-Z]+$/.test(w) ? p.singular(w) : w
  );
  words = words.map(w => w.toLowerCase());
  portion = p.isPlural(portion) ? p.singular(portion) : portion;

  var doubleWords = [];
  for (i = 0; i < words.length - 1; i++) {
    doubleWords.push(words[i] + " " + words[i + 1]);
  }
  query = searchQueryWithPortion(words, portion);
  pool.query(query, function(err, res) {
    if (!err)
      var ranked = res.map(function(f) {
        return {
          name: f.food_name,
          rank: rankFood(words, doubleWords, f.food_name),
          weight: f.weight
        };
      });
    var bestFood = ranked.reduce(compareRanks, {
      rank: [13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    });
    if (bestFood.name === undefined) bestFood = undefined;
    dbHandler({ error: err, result: bestFood }, action);
  });
};

module.exports = {
  searchFood: searchFood,
  searchFoodWithPortion: searchFoodWithPortion
};
