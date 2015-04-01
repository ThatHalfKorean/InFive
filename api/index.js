var Hapi = require('hapi'),
	Config = require('./config'),
	Mongoose = require('mongoose');

var mongo_url = 'mongodb://' + Config.mongo.url +'/'+ Config.mongo.database;
Mongoose.connect(mongo_url);

// Create a server with a host and port
var server = new Hapi.Server();
server.connection({ 
    host: 'localhost', 
    port: 8000 
});

// Add the route
server.route({
    method: 'GET',
    path:'/hello', 
    handler: function (request, reply) {
       reply('hello world');
    }
});

// Start the server
server.start(function(){
	console.log("Woo woooo");
});
