# alien8

https://git-teaching.cs.bham.ac.uk/mod-team-proj-2018/alien8

#Do not forget
- Threads need to be killed properly and gracefully 

# Mechanics
## Tasks
- Integrate with Server
- Integrate with UI
- Cooldown is not very polished apparently
  - cooldown is wayyyy to slow for light attacks
- When shielded move slower, or have meter
- Heavy attacks only hit one person
- make ai attacks speed limited, whilst player attacks should be uncapped
- Make more game modes


# Renderer
## Tasks
- Improve performance by calculating relative locations once per tick
- Make things look nice


# Gui
## Tasks
- Ensure that when exiting a local game in the pause menu that the game thread stops
## Ideas
- Everything will happen client side first and then confirmed by the server. Which then syncs all the game clients to be like server game state
    - Will probably change
- When you die become a ghost that doesn't collide with anyone but can spectate, opacity turned down
- Constantly check for people in hit area and place into an array and when heavy/light attack is called damage all players in that array


