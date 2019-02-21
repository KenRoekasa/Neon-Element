package engine.gameTypes;

public abstract class GameType {

    public static enum Type {
        FirstToXKills(0, FirstToXKillsGame.class), TimedGame(1, TimedGame.class);

        private byte id;
        private Class<? extends GameType> gameType;
    
        Type(int id, Class<? extends GameType> gameType) {
            this.id = (byte) id;
            this.gameType = gameType;
        }
        
        public Class<? extends GameType> getGameType() {
            return this.gameType;
        }

        public byte getId() {
            return this.id;
        }

        public static Type getById(byte id) {
            for (Type t : Type.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }
}
