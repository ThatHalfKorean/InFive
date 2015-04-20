var User = require('../models/user').User,
    Event = require('../models/event').Event,
    tokens = require('./tokens'),
    auth = require('./authentication');

var eventFuncs = {
  createEvent: function (request, reply) {
    var inevent = request.payload;
    User.findOne({authToken: inevent.Authorization}, function (err, existingUser) {
      if (auth.checkAuthToken(existingUser, reply)) {
        var username = existingUser.username;
        var newEvent = new Event({
          eventTitle: inevent.eventTitle,
          address: inevent.address,
          host: username,
          recipients: inevent.recipients,
          eventDate: inevent.eventDate
        });
        newEvent.save();
        reply({
          message: "Success",
          id: inevent._id,
          author: username,
          recipients: inevent.recipients,
          response: "Created a new event"
        });
      }
    });
  },

  getEventsCreatedByUser: function (request, reply) {
      User.findOne({authToken: request.headers.authorization}, function (err, existingUser) {
        if (auth.checkAuthToken(existingUser, reply)) {
          var username = existingUser.username;
          Event.find({ host: username }, function (err, events) {
            var sortedEvents = events.sort(function (a, b) {
              var dateA = a.eventDate.getTime(),
                  dateB = b.eventDate.getTime();
              if (dateA < dateB) {
                return -1;
              }
              if (dateA > dateB) {
                return 1;
              }
              return 0;
            });
            reply({
              message: "Success",
              response: sortedEvents
            });
          });
        }
      });
    },

    getEventsFeed: function (request, reply) {
      User.findOne({authToken: request.headers.authorization}, function (err, user) {
        if (auth.checkAuthToken(user, reply)) {
          var username = user.username;
          Event.find({ recipients: username }, function (err, events) {
            var viewableEvents = [];
            var index = 0;
            events.forEach(function (inevent) {
              viewableEvents[index] = inevent;
              index++;
            });
            var sortedEvents = viewableEvents.sort(function (a, b) {
              var dateA = a.eventDate.getTime(),
                  dateB = b.eventDate.getTime();
              if (dateA > dateB) {
                return -1;
              }
              if (dateA < dateB) {
                return 1;
              }
              return 0;
            });
            reply({
              message: "Success",
              response: sortedEvents
            });
          });
        }
      });
    }

};

module.exports = eventFuncs;