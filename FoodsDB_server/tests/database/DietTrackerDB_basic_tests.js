var makeDB = require("../../src/database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new makeDB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);
var assert = require("assert");

// test objects

var food = {
  food_name: "Beef, cured, pastrami",
  Protein: 4,
  Fat: 1.2
};

var user = {
  email: "orfe@gmail.com",
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

  var getAteTodayAmountTest = {
    success: function(res) {
      db.deleteAte(deleteAteTest);
    },
    failure: function() {
        assert.fail("failed at getting amount of food");
    }
  };

var getUserTest = {
  success: function(res) {
      if(res[0].email != user.email || res[0].password != user.password)
        assert.fail("failed at getting user");
    db.getAteTodayAmount(user.email,getAteTodayAmountTest);
  },
  failure: function() {
    assert.fail("failed at getting user");
  }
};

var addAteTest={
    success: function() {
        db.getUser(user,getUserTest);
      },
      failure: function() {
        assert.fail("failed at adding ate");
      }
}

var addFoodTest={
    success: function() {
        db.addUserAte(user.email,food.food_name,100,addAteTest)
      },
      failure: function() {
        assert.fail("failed at adding food");
      }
}

var addUserTest={
    success: function() {
        db.addFood(food,addFoodTest);
      },
      failure: function() {
        assert.fail("failed at adding user");
      }
}

var createAteTest = {
  success: function() {
    db.addUser(user,addUserTest);
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
