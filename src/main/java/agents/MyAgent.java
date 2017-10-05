package agents;

import behaviours.FindAverage;
import jade.core.Agent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;

public class MyAgent extends Agent {
    private static final Logger LOGGER = LogManager.getLogger(MyAgent.class);

    public String[] linkedAgents;
    public HashMap<String, Integer> agentsNumbers = new HashMap<>();
    public HashMap<String, Integer> freshAgentsNumbers = new HashMap<>();

    @Override
    public void setup() {
        int myNumber = Integer.valueOf((String) getArguments()[0]);
        LOGGER.info("0) number = " + myNumber);

        String myName = getAID().getLocalName();
        freshAgentsNumbers.put(myName, myNumber);

        linkedAgents = Arrays.copyOfRange(getArguments(), 1,
                getArguments().length, String[].class);

        addBehaviour(new FindAverage(this));
    }
}
