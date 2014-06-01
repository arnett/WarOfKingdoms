===============================================================================
JSON Examples:

{"id":"64"} - player

[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}] - players

{"name":"wintefell"} - territory

{"territory":{"name":"wintefell"},"players":[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}]} - conflict

[{"territory":{"name":"wintefell"},"players":[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}]},
 {"territory":{"name":"riverun"},"players":[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}]},
 {"territory":{"name":"whatever"},"players":[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}]},
 {"territory":{"name":"tsst"},"players":[{"id":"64"},{"id":"32"},{"id":"16"},{"id":"8"}]}] - conflicts

[{"action":"Attack","targetTerritory":{"name":"A"}},{"action":"Attack","targetTerritory":{"name":"B"}},{"action":"Attack","targetTerritory":{"name":"C"}},{"action":"Attack","targetTerritory":{"name":"D"}}]

===============================================================================
* As Java Code:

"{\"id\":\"64\"}"

"[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]"

"{\"territory\":{\"name\":\"wintefell\"},\"players\":[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]}"

"[{\"territory\":{\"name\":\"wintefell\"},\"players\":[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]},{\"territory\":{\"name\":\"riverun\"},\"players\":[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]},{\"territory\":{\"name\":\"whatever\"},\"players\":[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]},{\"territory\":{\"name\":\"tsst\"},\"players\":[{\"id\":\"64\"},{\"id\":\"32\"},{\"id\":\"16\"},{\"id\":\"8\"}]}]"
