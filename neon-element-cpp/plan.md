# Neon-Element: Java to C++20 Conversion — Progress & Continuation Plan

## Overall Status

**Phases 0–8: COMPLETE** | **Phase 9: NOT STARTED**

No code has been compiled or tested yet. All files have been written but not built.

---

## Completed Work (Phases 0–8)

### Phase 0: Project Scaffolding — DONE
- `CMakeLists.txt` — C++20, SFML 2.5+, GLOB_RECURSE, asset copy post-build
- `cmake/CompilerFlags.cmake` — GCC/Clang/MSVC warnings, optional sanitizers
- `src/main.cpp` — full menu system → game launch flow using UIManager
- `tests/CMakeLists.txt` — Catch2 via FetchContent
- Assets copied: 19 audio files → `assets/audio/`, background.png → `assets/textures/`

### Phase 1: Core Math & Enums — DONE
Files in `src/core/` and `src/engine/model/enums/`:
- `Vec2.hpp` — `using Vec2 = sf::Vector2f` + helpers
- `Rect.hpp` — center-based AABB with intersection
- `Circle.hpp` — circle-rect and circle-circle collision
- `Transform2D.hpp` — rotated rect for attack hitboxes
- `Clock.hpp` — GameClock with pause/resume, deltaTime, elapsedMs
- Enums: `Action.hpp`, `Elements.hpp`, `ObjectType.hpp`, `PowerUpType.hpp`, `Directions.hpp`
- `CooldownSystem.hpp`, `AttackTimes.hpp`
- `tests/test_core.cpp` — unit tests for all core types

### Phase 2: Entity Hierarchy + Physics — DONE
Files in `src/engine/entities/`, `src/engine/physics/`, `src/engine/calculations/`:
- `PhysicsObject.hpp/.cpp` — abstract base
- `Character.hpp/.cpp` — movement, combat, cooldowns, elements, timestamp-based timers
- `Player.hpp/.cpp` — concrete player with update() state machine
- `Wall.hpp/.cpp`, `PowerUp.hpp/.cpp`
- `CollisionDetector.hpp` — AABB + circle-AABB
- `PhysicsController.hpp/.cpp` — clientLoop/serverLoop/dumbClientLoop
- `DamageCalculation.hpp` — constexpr element/shield matrices

### Phase 3: Game Model Layer — DONE
Files in `src/engine/model/`, `src/engine/controller/`:
- `GameState.hpp/.cpp`, `ClientGameState.hpp/.cpp`, `ServerGameState.hpp/.cpp`
- `GameType.hpp` + 4 subclasses: `FirstToXKillsGame`, `TimedGame`, `HillGame`, `Regicide`
- `Map.hpp`, `ScoreBoard.hpp/.cpp`
- `MapGenerator.hpp`, `GameStateGenerator.hpp`
- `GameTypeHandler.hpp`, `PowerUpController.hpp`, `RespawnController.hpp`

### Phase 4: AI System — DONE
Files in `src/engine/ai/`:
- Enums: `AiType.hpp`, `AiStates.hpp`
- Calculations: `TimeCalculations.hpp`, `MovementCalculations.hpp/.cpp`, `PlayersCalculations.hpp/.cpp`, `PowerupCalculations.hpp/.cpp`, `AiCalculations.hpp/.cpp` (+ HillCalculations, RegicideCalculations)
- Actions: `AiActions.hpp/.cpp`, `AiStateActions.hpp/.cpp` (+ Kills, Timed, Hill, Regicide subclasses)
- FSMs: `FSM.hpp/.cpp` (+ KillsFSM, TimedFSM, HillFSM, RegicideFSM)
- Controllers: `AiController.hpp/.cpp`, `AiControllersManager.hpp/.cpp`
- **Note**: `AiControllersManager` has `registerExistingAi()` added for pre-created players

### Phase 5: Rendering Engine — DONE
Files in `src/graphics/`:
- `ISOConverter.hpp` — 45° rotation + 0.5 Y scale
- `ColorSwitch.hpp` — element/powerup color mapping
- `textures/Sprites.hpp`, `textures/TextureLoader.hpp`
- `Renderer.hpp/.cpp` — background, stars, map, walls, powerups, Y-sorted players, attack animations, camera deadzone

### Phase 6: Input + Audio + Game Loop — DONE
Files in `src/client/`, `src/audio/`:
- `InputHandler.hpp` — WASD/arrows, mouse, clicks, number keys, P, Tab
- `AudioManager.hpp` — 16-channel sound pool, music streaming
- `Sound.hpp`, `Music.hpp` — enums with asset paths
- `GameClient.hpp/.cpp` — full game loop with AI integration, HUD, PauseOverlay, GameOverScreen, **online mode support**

### Phase 7: UI System — DONE
Files in `src/ui/`:
- `Widget.hpp/.cpp` — Button, Label, Slider, RadioGroup with neon styling
- `Screen.hpp` — abstract base for screens
- `UIManager.hpp/.cpp` — screen stack (push/pop/replace), **online game callback**
- `MenuScreen.hpp/.cpp` — PLAY, HELP, OPTIONS, EXIT
- `ModeScreen.hpp/.cpp` — LOCAL / ONLINE
- `LocalSetupScreen.hpp/.cpp` — enemy count, difficulty per enemy, game mode selection, START
- `OnlineModeScreen.hpp/.cpp` — HOST / JOIN
- `LobbyScreen.hpp/.cpp` — player list, start button (host only), **network integration**
- `OptionsScreen.hpp/.cpp` — music/effects volume sliders
- `HelpScreen.hpp/.cpp` — Controls, Game Modes, Elements tabs
- `HUD.hpp/.cpp` — health bar, K/D stats, cooldown indicators, tab leaderboard, mode info
- `PauseOverlay.hpp/.cpp` — RESUME, OPTIONS, QUIT with semi-transparent overlay
- `GameOverScreen.hpp/.cpp` — ranked leaderboard table, PLAY AGAIN, QUIT

### Phase 8: Networking Layer — DONE
Files in `src/networking/`, `src/server/`:

**Core networking:**
- `Constants.hpp` — port numbers (53582), buffer size (128 bytes)
- `PacketType.hpp` — enum with all 27 packet type IDs + factory lookup
- `Packet.hpp` — base Packet class, PacketBuffer (big-endian read/write), Sender, PacketToServer, PacketToClient
- `PacketFactory.hpp` — deserializes raw bytes → correct packet subclass
- `Network.hpp` — abstract base: `std::jthread` receive loop with `sf::UdpSocket`
- `NetworkDispatcher.hpp` — thread-safe send via shared `sf::UdpSocket`

**Packet types (grouped into 5 header files + 1 handler .cpp):**
- `packets/HelloPackets.hpp` — HelloPacket, HelloAckPacket
- `packets/ConnectPackets.hpp` — ConnectPacket, ConnectAckPacket, ConnectBroadcast
- `packets/DisconnectPackets.hpp` — DisconnectPacket, DisconnectAckPacket, DisconnectBroadcast
- `packets/StatePackets.hpp` — LocationStatePacket, ActionStatePacket, ElementStatePacket, ReadyStatePacket, PowerUpPacket, InitialGameStateAckPacket
- `packets/BroadcastPackets.hpp` — ReadyStateBroadcast, LocationStateBroadcast, ElementStateBroadcast, HealthStateBroadcast, PowerUpPickUpBroadcast, PowerUpBroadcast, ActionStateBroadcast, RespawnBroadcast, ScoreBroadcast
- `packets/GamePackets.hpp` — InitialGameStateBroadcast, GameStartBroadcast, GameOverBroadcast
- `packets/PacketHandlers.cpp` — all handle() method implementations

**Client networking:**
- `client/ClientNetwork.hpp` — owns dispatcher + handler, binds to any port, starts receive thread
- `client/ClientNetworkDispatcher.hpp` — sendHello, sendConnect, sendLocationState, sendActionState, sendElementState, etc.
- `client/ClientNetworkHandler.hpp/.cpp` — handles all server→client packets, updates ClientGameState

**Server networking:**
- `server/PlayerConnection.hpp` — wraps Player + IP:port + hasInitialState flag
- `server/ConnectedPlayers.hpp` — tracks all connections, thread-safe with mutex
- `server/ServerNetwork.hpp` — owns dispatcher + handler + connected players, binds to port 53582
- `server/ServerNetworkDispatcher.hpp` — broadcast methods for all server→client packets
- `server/ServerNetworkHandler.hpp/.cpp` — handles all client→server packets

**Game server:**
- `server/GameServer.hpp/.cpp` — two-phase game loop: wait for players → 15ms game tick, integrates physics/powerup/respawn controllers

**Integration updates:**
- `GameStateGenerator.hpp` — added `createServerGameState()` and `createEmptyClientGameState()`
- `UIManager.hpp` — added `setOnStartOnlineGame()` callback
- `LobbyScreen.hpp/.cpp` — wired to trigger online game via UIManager callback
- `GameClient.hpp/.cpp` — added online mode: separate `gameLoopOnline()` that sends input via network and uses `dumbClientLoop()` for physics
- `main.cpp` — handles both local and online game launch paths

**Key C++ design decisions:**
- `sf::UdpSocket` replaces Java `DatagramSocket`
- `std::jthread` for receive threads (auto-join on destruction)
- `std::mutex` in NetworkDispatcher and ConnectedPlayers for thread safety
- `PacketBuffer` class with big-endian read/write (matches Java's ByteBuffer)
- Visitor pattern: each packet has `handle(Handler&)` for type-safe dispatch
- Online GameClient uses `dumbClientLoop()` physics, sends state via ClientNetworkDispatcher

---

## Remaining Work

### Phase 9: Polish + Testing (~10 files) — NOT STARTED

1. **GLSL bloom/glow shader** — vertex + fragment shader for neon glow effect
2. **Motion blur** — trail rendering for speed-boosted players
3. **Debug overlay** — FPS counter, player positions, entity count
4. **Expanded unit tests**:
   - `test_collision.cpp` — collision detection edge cases
   - `test_player.cpp` — player state machine, damage, cooldowns
   - `test_scoreboard.cpp` — scoring, leaderboard sorting
   - `test_packets.cpp` — packet round-trip serialization
   - `test_ai.cpp` — AI state transitions, decision making
   - `test_game.cpp` — full headless game simulation
5. **Build verification** — compile with zero warnings on GCC/Clang/MSVC
6. **Memory audit** — AddressSanitizer, valgrind clean
7. **Performance** — target 60 FPS, profile hotspots

---

## Known Issues / Notes

1. **TimedFSM::normalAiFetchAction()** has dead code creating a temporary `KillsFSM` with nullptr — harmless but should be cleaned up
2. **InputHandler::handleEvents()** is no longer called — GameClient now polls events directly in gameLoop(). The method can be removed.
3. **Font loading** happens in multiple places (HUD, PauseOverlay, GameOverScreen, each Screen). Could be centralized into a FontManager. Low priority.
4. **Nothing has been compiled yet.** First build will likely surface include path issues, missing forward declarations, etc. Budget time for build fixes.

---

## How to Resume

1. Start a new Claude Code session in `/home/roekasak/Neon-Element`
2. Say: "Continue implementing the Neon-Element C++20 conversion. Phases 0-8 are complete. Start Phase 9 (Polish + Testing). Read this plan: `neon-element-cpp/plan.md`"
3. Claude should:
   - Begin with build verification (attempt to compile, fix errors)
   - Add expanded unit tests
   - Add shader effects and debug overlay
   - Run memory/performance audits

Alternatively, to just build and fix compile errors first:
- "Build the C++ project at `neon-element-cpp/` and fix any compile errors. Phases 0-8 code is written but untested."
