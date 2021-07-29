package gamestates;

public class StateMaster {

    private static State state = null;

    /**
     *
     * @return the currently active state
     */
    public static State getState() {
        return state;
    }

    /**
     *
     * @param newState - the new state of the game
     */
    public static void setState(State newState) {
        state = newState;
    }
}
