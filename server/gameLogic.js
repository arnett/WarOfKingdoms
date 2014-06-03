exports.Move = function (action, target) {

	this.action = action;
	this.targetTerritory = target;
}

exports.Territory = function (name, owner) {
	this.name = name;
	this.owner = owner;
}

exports.Player = function (id, name) {
	this.id = id;
	this.name = name;
}

exports.Session = function (id) {
  this.id = id;
  this.playerList = new Array();
  this.addPlayer = function(aPlayer) {
    this.playerList.push(aPlayer);
  }
}

exports.sessions = new Array();

exports.addSession = function(session) {
  this.sessions.push(session);
}

exports.addPlayerToSession = function(session_id, aPlayer) {
  for (var i in this.sessions) {
    var session = this.sessions[i];
    if (session.id == session_id) {
      session.addPlayer(aPlayer)
    }
  }
}

exports.getPlayersInEachSession = function() {
  for (var i in this.sessions) {
    var session = this.sessions[i];
    console.log(session)
  }
  console.log("FIM")
}
