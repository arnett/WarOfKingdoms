var gameModule = require('./gameLogic.js');

Array.prototype.contains = function(elem) {
   for (var i in this)
       if (this[i] == elem)
        return true;
   
   return false;
}

exports.generateMoves = function(house, currentMap) {
    var territoriesWithMoves = new Array();
    var moves = new Array();

    for (var i = 0; i < currentMap.length(); i++) {
        var territory = currentMap.get(i);

        if (isMyTerritory(house, territory) && !territoriesWithMoves.contains(territory)) {
            var neighbors = territory.neighbors.split(" ");

            for (var j = 0; j < neighbors.length; j++) {
                var neighbor = currentMap.getByName(neighbors[j]);
                if (!isMyTerritory(house, neighbor) && !territoriesWithMoves.contains(neighbor)) {
                    moves.push(new gameModule.Move(territory, neighbor, "attack"));
                    territoriesWithMoves.push(neighbor);
                    territoriesWithMoves.push(territory);
                    break;
                }
            }
        }
    }

    console.log("AI moves for house " + house + " are:");
    console.log(moves);
    return moves;
}

isMyTerritory = function(house, territory) {
    return territory.owner == house;
}

/*
@Override
public List<Move> getNextMoves(House house, List<Territory> currentMap) {
	RulesChecker checker = RulesChecker.getInstance();
	List<Territory> territoriesWithMoves = new ArrayList<Territory>();
	List<Move> nextMoves = new ArrayList<Move>();
	for (Territory t : currentMap) {
		if (isMyTerritory(house, t) && !territoriesWithMoves.contains(t)) {
			for (Territory neighbor : checker.getNeighbors(t)) {
				if (!isMyTerritory(house, neighbor)
						&& !territoriesWithMoves.contains(neighbor)) {
					nextMoves.add(new Move(t, neighbor, Action.ATTACK));
					territoriesWithMoves.add(neighbor);
					territoriesWithMoves.add(t);
					break;
				}
			}
		}
	}
	return nextMoves;
}

private boolean isMyTerritory(House house, Territory t) {
	return t.getOwner() != null && t.getOwner().equals(house);
} */
