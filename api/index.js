var Hapi = require('hapi'),
	routes = require('./routes')
	Config = require('./config'),
	Mongoose = require('mongoose');

var mongo_url = 'mongodb://' + Config.mongo.url +'/'+ Config.mongo.database;
Mongoose.connect(mongo_url);

// Create a server with a host and port
var server = new Hapi.Server();
server.connection({ 
    port: 7777 
});

// Add the route
server.route(routes);
server.start(function () {
  console.log("Server started on localhost:7777");
});

// Start the server
server.start(function(){
	console.log("Woo woooo");
});
