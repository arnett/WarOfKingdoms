
exports.House = function (name) {
		this.name = name;
}

exports.Move = function (origin, target, action) {
  	this.origin = origin;
	this.target = target;
	this.action = action;
}

exports.Territory = function (name, owner) {
	this.name = name;
	this.owner = owner;
}

exports.Player = function (id, name, house) {
	this.id = id;
	this.name = name;
  	this.house = house;
}

exports.Conflict = function (territory, houses, diceValues) {
  this.territory = territory;
  this.houses = houses;
  this.diceValues = diceValues
}

exports.GameState = function (isGameEnd, winnerList, currentTurn, totalTurns) {
  this.isGameEnd   = isGameEnd;
  this.winnerList  = winnerList;
  this.currentTurn = currentTurn;
  this.totalTurns  = totalTurns;
}