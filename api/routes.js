var Joi = require('joi'),
	User = require('./models/user').User,
	events = require('./helpers/events'),
	accounts = require('./helpers/accounts'),
	notifications = require('./helpers/notifications'),
	users = require('./helpers/users');

var routes = [
	{
	    method: 'POST',
	    path: '/signup',
	    config: {
	      validate: {
	        payload: Joi.object({
	            username: Joi.string().min(3).max(20).required(),
	            password: Joi.string().alphanum().min(8).max(50).required(),
	            confirmPassword: Joi.string().alphanum().min(8).max(50).required(),
	           	email: Joi.string().email().min(5).max(50).required()
	        }).unknown(false)
	      }
	    },
	    handler: accounts.signup
	},
	{
    	method: 'POST',
    	path: '/sessions',
    	config: {
      	  validate: {
        	payload: Joi.object({
            	username: Joi.string().min(3).max(20).required(),
            	password: Joi.string().alphanum().required()
        	}).unknown(false)
      	  }
    	},
    	handler: accounts.login
  },
  {
    	method: 'DELETE',
    	path: '/sessions',
    	handler: accounts.logout
  },
    {
    method: 'POST',
    path: '/events',
    config: {
      validate: {
        payload: Joi.object({
            Authorization: Joi.string().required(),
            eventTitle: Joi.string().min(1).max(30).required(),
            address: Joi.string().min(8).max(50).required(),
            recipients: Joi.array().items(Joi.string().min(1).required()),
            eventDate: Joi.date().iso().required()
        }).unknown(false)
      }
    },
    handler: events.createEvent
  },

  {
    method: 'GET',
    path: '/events',
    handler: events.getEventsCreatedByUser
  },

  {
    method: 'GET',
    path: '/eventsFeed',
    handler: events.getEventsFeed
  },
     {
    method: 'POST',
    path: '/notifications',
    config: {
      validate: {
        payload: Joi.object({
            Authorization: Joi.string().required(),
            content: Joi.string().min(1).max(300).required(),
            recipients: Joi.array().items(Joi.string().min(1).required())
        }).unknown(false)
      }
    },
    handler: notifications.createNotification
  },

  {
    method: 'GET',
    path: '/notifications',
    handler: notifications.getNotificationsCreatedByUser
  },

  {
    method: 'GET',
    path: '/notificationsFeed',
    handler: notifications.getNotificationsFeed
  },
  {
    	method: 'GET',
    	path: '/users',
    	handler: users.searchForUsers
  },
  {
    	method: 'POST',
    	path: '/friends',
    	config: {
      	  validate: {
        	payload: Joi.object({
            	Authorization: Joi.string().required(),
            	newFriend: Joi.string().required()
        	}).unknown(false)
      	  }
    	},
    	handler: users.addFriend
  },
  {
    	method: 'GET',
    	path: '/friends',
    	handler: users.getFriends
  }
]
module.exports = routes