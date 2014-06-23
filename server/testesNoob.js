var mapModule = require('./mapController.js');
var playerModule = require('./playerController.js');

map = new mapModule.Map()
console.log(map)
map.addOwnerInTerritory("Owner", "A")
console.log(map)
map.addOwnerInTerritory("Owner2", "A")
console.log(map)

players = new playerModule.PlayerController()
console.log(players)
players.add(1, "Andre", "Stark")
console.log(players)
console.log(players.get(0))
players.add(2, "Andre2", "Lannister")
console.log(players)