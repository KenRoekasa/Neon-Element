alien8

# Mechanics

https://git-teaching.cs.bham.ac.uk/mod-team-proj-2018/alien8

## Tasks
- Enemy class can only be engine.ai not player controlled enemies. Sort this out 
- Dealing damage to other players with damage calculation
- Sends things to the server
- Integrate with Server
- Integrate with UI
- Fix collision bug
- Cooldown is not very polished apparently
- When shielded move slower, or have meter
- Implement Scoreboard
- Implement respawning
- doCollisionDetection(); doHitDetection(); deathHandler(); make one for loop where allplayers is passed in
- Heavy attacks only hit one person




## Ideas
- Have the aiController that controls a Player object rather than being an engine.entities its self
- Player will be tagged enemies, opponents, engine.ai controlled etc...
- Everything will happen client side first and then confirmed by the server. Which then syncs all the game clients to be like server game state
    - Will probably change
- Change boundaries when colliding so it will be a diagonal boundary
- When you die become a ghost that doesn't collide with anyone but can spectate, opacity turned down
- Constantly check for people in hit area and place into an array and when heavy/light attack is called damage all players in that array


