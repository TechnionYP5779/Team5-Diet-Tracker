var buildTables = require("./createTables");
var dropTables = require("./deleteTables");
var inserts = require("./inserts");
var deletes= require("./deletes");
var gets = require("./gets");
var foodsearches= require("./search_food");
var mysql = require("mysql");

function makeDB(host, user, password, database) {
  this.host = host;
  this.user = user;
  this.password = password;
  this.database = database;
  this.createUsers = buildTables.createUsers;
  this.createFood = buildTables.creatFood;
  this.createAte = buildTables.createAte;
  this.createPortion = buildTables.createPortion;
  this.deleteUsers = dropTables.deleteUsers;
  this.deleteFoods = dropTables.deleteFood;
  this.deleteAte = dropTables.deleteAte;
  this.addUser = inserts.addUser;
  this.addFood = inserts.addFood;
  this.addUserAte = inserts.addUserAte;
  this.addPortion = inserts.addPortion;
  this.getUser = gets.getUser;
  this.getAteTodayAmount = gets.getAteTodayAmount;
  this.deleteUser=deletes.deleteUser;
  this.deleteFood=deletes.deleteFood;
  this.searchFood=foodsearches.searchFood;
  this.handler = function(status, action) {
    if (status.error) action.failure(status.error);
    else action.success(status.result);
  };
  this.pool = mysql.createPool({
    host: this.host,
    user: this.user,
    password: this.password,
    database: this.database
  });
}

module.exports = makeDB;
