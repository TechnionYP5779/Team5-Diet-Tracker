var db = require("./DietTrackerDB");
var XLSX = require("xlsx");

tagMeaning = {
  PROCNT: "Protein",
  FAT: "Fat",
  CHOCDF: "Carbohydrate",
  ENERC_KCAL: "Calorie",
  LACS: "Lactose",
  ALC: "Alcohol",
  CAFFN: "Caffeine",
  FIBTG: "Fiber",
  CA: "Calcium",
  FE: "Iron",
  NA: "Sodium",
  VITA_RAE: "Vitamin_A",
  TOCPHA: "Vitamin_E",
  VITD: "Vitamin_D",
  VITC: "Vitamin_C",
  VITB6A: "Vitamin_B_6",
  VITB12: "Vitamin_B_12",
  FOLAC: "Folic_acid",
  CHOLE: "Cholesterol"
};

// read and parse foods.xslx

var data = [];
var workbook = XLSX.readFile("foods.xlsx");
var sheet_name_list = workbook.SheetNames;
sheet_name_list.forEach(function(y) {
  var worksheet = workbook.Sheets[y];
  var headers = {};
  data = [];
  for (z in worksheet) {
    if (z[0] === "!") continue;
    //parse out the column, row, and value
    var tt = 0;
    for (var i = 0; i < z.length; i++) {
      if (!isNaN(z[i])) {
        tt = i;
        break;
      }
    }
    var col = z.substring(0, tt);
    var row = parseInt(z.substring(tt));
    var value = worksheet[z].v;

    //store header names
    if (row == 1 && value) {
      headers[col] = value;
      continue;
    }

    if (!data[row]) data[row] = {};
    data[row][headers[col]] = value;
  }
  //drop those first two rows which are empty
  data.shift();
  data.shift();
});

// done reading and parsing

var currFood = {};
var foods = [];
var i = 0;

data.forEach(function f(foodInfo) {
  if (foodInfo["Long_Desc"] !== currFood["food_name"]) {
    if (currFood !== {}) {
      foods[i] = currFood;
      i++;
    }
    currFood = { food_name: foodInfo["Long_Desc"] };
  }

  currFood[tagMeaning[foodInfo["Tagname"]]] = foodInfo["Nutr_Val"];
});

foods[i] = currFood;
for (var j = 1; j <= i; j++) {
  db.addFood(foods[j]);
}

db.closeCon();
