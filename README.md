alien8

# Mechanics

https://git-teaching.cs.bham.ac.uk/mod-team-proj-2018/alien8

## Tasks
- Enemy class can only be ai not player controlled enemies. Sort this out- Improve players from walking into each other and getting stuck into each other
- Dealing damage to other players with damage calculation
- Sends things to the server
- Changing States to be added
- Integrate with Server
- Integrate with UI


## Ideas
- Have the aiController that controls a Player object rather than being an entities its self
- Player will be tagged enemies, opponents, ai controlled etc...
- Everything will happen client side first and then confirmed by the server. Which then syncs all the game clients to be like server game state


