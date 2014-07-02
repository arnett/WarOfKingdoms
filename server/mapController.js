var utilsModule = require('./utils.js');


Territory = function (name, region, owner) {
    this.name   = name;
    this.region = region;
    this.owner  = owner;
    this.neighbors;
}

exports.Map = function () {
    this.map = this.createMap();
}

this.Map.prototype.addOwnerInTerritory = function(owner, territoryName) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].name == territoryName)
            this.map[i].owner = owner;
}

this.Map.prototype.get = function(i) {
    return this.map[i];
}

this.Map.prototype.length = function() {
    return this.map.length;
}

this.Map.prototype.getByName = function(name) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].name == name)
            return this.map[i]

    return null
}

this.Map.prototype.update = function(conflicts, nonConflicting) {
    for (var i = 0; i < nonConflicting.length; i++)
        this.getByName(nonConflicting[i].target.name).owner = nonConflicting[i].origin.owner;

    for (var i = 0; i < conflicts.length; i++) {
        var biggestDiceValue = utilsModule.getBiggestDiceValueIndex(conflicts[i].diceValues);

        if (biggestDiceValue != -1) {
            this.getByName(conflicts[i].territory.name).owner = conflicts[i].houses[biggestDiceValue];
        }
    }
}

this.Map.prototype.isLastSurvivor = function(ownerName) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].owner != null && this.map[i].owner.name != ownerName)
            return false;

    return true;
}

this.Map.prototype.doPlayerKeepTerritory = function(ownerName, territoryName) {
    return this.getByName(territoryName).owner.name == ownerName;
}

this.Map.prototype.getNumberOfTerritoriesConqueredInRegion = function(ownerName, region) {
    var num = 0;
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].region == region && this.map[i].owner != null && this.map[i].owner.name == ownerName)
            num++;

    return num;
}

this.Map.prototype.createMap = function () {
	var territory = null;
	var territoriesArray = new Array();

	// NORTH
	territory = new Territory("A", "N", null);
	territory.neighbors = "B C";
	territoriesArray.push(territory);

	territory = new Territory("B", "N", null);
	territory.neighbors = "A C";
	territoriesArray.push(territory);

	territory = new Territory("C", "N", null);
	territory.neighbors = "A B D E F";
	territoriesArray.push(territory);

	territory = new Territory("D", "N", null);
	territory.neighbors = "C G";
	territoriesArray.push(territory);

	territory = new Territory("E", "N", null);
	territory.neighbors = "C F";
	territoriesArray.push(territory);

	territory = new Territory("F", "N", null);
	territory.neighbors = "C E G I";
	territoriesArray.push(territory);

	territory = new Territory("G", "N", null);
	territory.neighbors = "D F I J";
	territoriesArray.push(territory);

	territory = new Territory("H", "N", null);
	territory.neighbors = "I K";
	territoriesArray.push(territory);

	// CENTER
	territory = new Territory("I", "C", null);
	territory.neighbors = "F G H J K L";
	territoriesArray.push(territory);

	territory = new Territory("J", "C", null);
	territory.neighbors = "G I L M";
	territoriesArray.push(territory);

	territory = new Territory("K", "C", null);
	territory.neighbors = "H I L N";
	territoriesArray.push(territory);

	territory = new Territory("L", "C", null);
	territory.neighbors = "I J K M N O P";
	territoriesArray.push(territory);

	territory = new Territory("M", "C", null);
	territory.neighbors = "J L P Q";
	territoriesArray.push(territory);

	territory = new Territory("N", "C", null);
	territory.neighbors = "K L O";
	territoriesArray.push(territory);

	territory = new Territory("O", "C", null);
	territory.neighbors = "L N P R S";
	territoriesArray.push(territory);

	territory = new Territory("P", "C", null);
	territory.neighbors = "L M O Q S T";
	territoriesArray.push(territory);

	territory = new Territory("Q", "C", null);
	territory.neighbors = "M P T";
	territoriesArray.push(territory);

	// SOUTH
	territory = new Territory("R", "S", null);
	territory.neighbors = "O S U Y";
	territoriesArray.push(territory);

	territory = new Territory("S", "S", null);
	territory.neighbors = "L M O Q S T";
	territoriesArray.push(territory);

	territory = new Territory("T", "S", null);
	territory.neighbors = "P Q S V X";
	territoriesArray.push(territory);

	territory = new Territory("U", "S", null);
	territory.neighbors = "R S V Z";
	territoriesArray.push(territory);

	territory = new Territory("V", "S", null);
	territory.neighbors = "S T U X Z";
	territoriesArray.push(territory);

	territory = new Territory("X", "S", null);
	territory.neighbors = "T V Z";
	territoriesArray.push(territory);

	territory = new Territory("Z", "S", null);
	territory.neighbors = "U V X Y";
	territoriesArray.push(territory);

	territory = new Territory("Y", "S", null);
	territory.neighbors = "R Z";
	territoriesArray.push(territory);

    territory = new Territory("Z", "S", null);
    territory.neighbors = "U V X Y";
    territoriesArray.push(territory);

    return territoriesArray;
}

