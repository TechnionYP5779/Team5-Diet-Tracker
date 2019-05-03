var makeDB = require("../../src/database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new makeDB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);
var assert = require("assert");

// test objects

  var user = {
    email: "r784eh34t84ctng437tgcngcwaintg@gmail.com",
  };
  
  // end of object tests


  var deleteUsersTest = {
    success: function() {
      db.pool.end();
      console.log("SUCCESS!");
    },
    failure: function() {
      assert.fail("failed at deleting users table");
    }
  };
  
  var deleteFoodTest = {
    success: function() {
      db.deleteUsers(deleteUsersTest);
    },
    failure: function() {
      assert.fail("failed at deleting food table");
    }
  };
  
  var deleteAteTest = {
      success: function() {
        db.deleteFood(deleteFoodTest);
      },
      failure: function() {
        assert.fail("failed at deleting ate table");
      }
    };


  var values4NonExistingUserTest = {
    success: function() {
        assert.fail("a query for non existing user should not success");
      },
      failure: function() {
        db.deleteAte(deleteAteTest);
      }
  }

  var createAteTest = {
  success: function() {
    db.getAteTodayAmount(user.email,values4NonExistingUserTest);
  },
  failure: function() {
    assert.fail("failed at creating ate table");
  }
};

var createFoodTest = {
  success: function() {
    db.createAte(createAteTest);
  },
  failure: function() {
    assert.fail("failed at creating food table");
  }
};

var createUsersTest = {
  success: function() {
    db.createFood(createFoodTest);
  },
  failure: function() {
    assert.fail("failed at creating users table");
  }
};

db.createUsers(createUsersTest);