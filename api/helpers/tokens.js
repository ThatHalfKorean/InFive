var MILLISECONDS_IN_A_WEEK = 604800000;

var tokenFuncs = {
  setExpiration: function () {
    return new Date().getTime() + MILLISECONDS_IN_A_WEEK;
  },

  isExpired: function (tokenExpiration) {
    var currentTime = new Date().getTime();
    return currentTime > new Date(tokenExpiration).getTime();
  },

  setTokenToNull: function (user) {
    user.authToken = null;
    user.tokenExpiration = null;
  }

};

module.exports = tokenFuncs;