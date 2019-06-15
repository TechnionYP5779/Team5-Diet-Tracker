var DB = require("./../src/database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../creds/production_creds.json', 'utf8'));
var db = new DB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);
var XLSX = require("xlsx");

var data = [];
var workbook = XLSX.readFile("foodPortion.xlsx");
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

var emptyAction={
    success: f=>{},
    failure: f=>{}
  };
data.forEach(function({Long_Desc,Msre_Desc,Weight}){
 db.addPortion({
    food_name: Long_Desc,
    measure: Msre_Desc,
    weight: Weight
 },emptyAction)
});