var DB = require("../database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new DB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);

var deleteUsers = {
    success: function() {
      db.pool.end();
    },
    failure: function() {
      assert.fail("failed at deleting users table");
    }
  };
  
  var deleteFood = {
    success: function() {
      db.deleteUsers(deleteUsers);
    },
    failure: function() {
      assert.fail("failed at deleting food table");
    }
  };
  
  var deleteAte = {
      success: function() {
        db.deleteFood(deleteFood);
      },
      failure: function() {
        assert.fail("failed at deleting ate table");
      }
    };


db.deleteAte(deleteAte);