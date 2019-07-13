public class SearchTracker {

    Search.Direction direction;
    boolean repeated;
    Node agent;
    int counter;

    /**
     *
     * @param direction FORWARD or BACKWARD, denotes the travel direction of
     *                  the repeated A* search agent.
     * @param repeated Whether or not we're performing repeated A* search.
     * @param agent If repeated, the agent's current position.
     * @param counter If repeated, the algorithm's counter.
     */
    public SearchTracker(Search.Direction direction, boolean repeated,
                         Node agent, int counter) {
        this.direction = direction;
        this.repeated = repeated;
        this.agent = agent;
        this.counter = counter;
    }
}
