var Joi = require('joi'),
	User = require('./models/user').User,
	accounts = require('./helpers/accounts');
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
	}
]
module.exports = routes