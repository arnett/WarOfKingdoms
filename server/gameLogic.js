
exports.sessions = new Array();

/**
  * SESSION
  */
exports.Session = function (id) {
  this.id = id;

  this.territories = new Array();
  this.playerList  = new Array();

  this.movesInCurrentTurn         = new Array();

  this.movesInCurrentTurnConsumed = 0;
  this.movesInCurrentTurnComplete = false;
  this.playersInConflict          = new Array();
  
}

this.Session.prototype.addPlayer = function(aPlayer) {
  this.playerList.push(aPlayer);
}

this.Session.prototype.hasPlayer = function(id) {
  return this.getPlayer(id) != undefined
}

this.Session.prototype.getPlayer = function(id) {
  for (var i in this.playerList) {
    var p = this.playerList[i];

    if (p.id == id)
      return p
  }

  return undefined
}

this.Session.prototype.containsPlayerWithID = function(id) {
  for (var player in this.playerList)
    if (this.playerList[player].id == id)
      return true

  return false
}

this.Session.prototype.setTerritories = function(territories) {
  this.territories = territories;
}

this.Session.prototype.addTerritoryToPlayer = function(player_id, territory_name) {
  var territory = this.getTerritory(territory_name)

  if (territory != undefined && this.hasPlayer(player_id))
    territory.owner = player_id
}

this.Session.prototype.getPlayerTerritories = function(player_id) {
  var playerTerritories = new Array()

  for (var i in this.territories) {
    var t = this.territories[i];

    if (t.owner == player_id)
      playerTerritories.push(t)
  }

  return playerTerritories;
}

this.Session.prototype.hasPlayerTerritory = function(territory_name) {
  for (var i in this.playerTerritories) {
    var t = this.playerTerritories[i];

    if (t.name == territory_name) {
      return true
    }
  }

  return false
}

this.Session.prototype.getTerritory = function(territory_name) {
  for (var i in this.territories) {
    var t = this.territories[i];

    if (t.name == territory_name)
      return t
  }
}

this.Session.prototype.consumeMove = function() {  
  if (this.movesInCurrentTurnComplete == true)
    this.movesInCurrentTurnConsumed--

  if (this.movesInCurrentTurnConsumed == 0) {
    this.processMoves();
    this.movesInCurrentTurnComplete = false
  }
}

this.Session.prototype.processMoves = function() {
  var playersInconflict = new Array()

  console.log(this.movesInCurrentTurn)

  for (var i = 0; i < this.movesInCurrentTurn.length; i++) {
    var move = this.movesInCurrentTurn[i];
    var territory = this.getTerritory(move.targetTerritoryName);

    if (territory != undefined) {
      if (territory.actionForPlayer != undefined) {
        playersInconflict.push(territory.actionForPlayer)
        playersInconflict.push(move.player_id)

        territory.actionForPlayer = undefined;
      } else {
        territory.actionForPlayer = move.player_id;
      }
    }

  };

  for (var i = 0; i < this.territories.length; i++) {
    var territory = this.territories[i];

    if (territory.actionForPlayer != undefined) {
      territory.owner = territory.actionForPlayer
      territory.actionForPlayer = undefined
    }

  };

  this.movesInCurrentTurn = new Array()

  this.playersInConflict = playersInconflict;
  return playersInconflict;
}


/**
  * SESSION CONTROLLER
  */
exports.SessionController = function () {
  this.sessions = new Array();
}

this.SessionController.prototype.getSession = function(session_id) {
  for (var i in this.sessions) {
    var session = this.sessions[i];

    if (session.id == session_id)
      return session
  }

  return undefined
}

// TODO Recer id e nao o objeto session
this.SessionController.prototype.addNewSession = function(session) {
  this.sessions.push(session);
}

this.SessionController.prototype.addPlayerToSession = function(session_id, aPlayer) {
  var session = this.getSession(session_id)
  if (session != undefined && !session.containsPlayerWithID(aPlayer.id))
    session.addPlayer(aPlayer)
}

this.SessionController.prototype.addTerritoriesToSession = function(session_id, territories) {
  for (var i in this.sessions) {
    var session = this.sessions[i];

    if (session.id == session_id) {
      session.setTerritories(territories)
    }
  }
}

this.SessionController.prototype.addTerritoryToPlayerInSession = function(session_id, player_id, territory_name) {
  var session = this.getSession(session_id)

  if (session != undefined)
    session.addTerritoryToPlayer(player_id, territory_name)
}

this.SessionController.prototype.sessionContainsTerritory = function(session_id, territory) {
  for (var i in this.sessions) {
    var session = this.sessions[i];

    if (session.id == session_id) {
      return session.territories.contains(territory)
    }
  }

  return false
}

this.SessionController.prototype.isTerritoryOwned = function(session_id, territory_name) {
  var session = this.getSession(session_id)

  if (session != undefined) {
    var territory = session.getTerritory(territory_name)

    return territory.owner != undefined;
  }

}


/**
  * OTHERS
  */

exports.Move = function (player_id, action, target) {
  this.player_id = player_id;
	this.action = action;
	this.targetTerritoryName = target;
}

exports.Territory = function (name, owner) {
	this.name = name;
	this.owner = owner;
  this.actionForPlayer = undefined;
}

exports.Player = function (id, name) {
	this.id = id;
	this.name = name;
}

exports.addSession = function(session) {
  this.sessions.push(session);
}

exports.getPlayersInEachSession = function() {
  for (var i in this.sessions) {
    var session = this.sessions[i];
    console.log(session)
  }
  console.log("FIM")
}
