package behaviours;

import agents.MyAgent;
import jade.core.behaviours.TickerBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.ContainerKiller;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static messages.MessageBuilder.inform;
import static messages.MessageReceiver.listen;

public class FindAverage extends TickerBehaviour {
    private static final Logger LOGGER = LogManager.getLogger(FindAverage.class);

    private static String mapToString(HashMap<String, Integer> map) {
        return Arrays.toString(map.entrySet().toArray());
    }

    private enum State {
        Send, Receive, End
    }

    private final MyAgent agent;
    private State state = State.Send;
    private int step = 0;

    public FindAverage(MyAgent agent) {
        super(agent, TimeUnit.SECONDS.toMillis(1));
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        step++;

        switch (state) {
            case Send:
                send();
                break;
            case Receive:
                receive();
                break;
            case End:
                end();
                break;
            default:
                block();
        }
    }

    private void send() {
        LOGGER.info(step + ") Sending " + mapToString(agent.freshAgentsNumbers)
                + " to " + Arrays.toString(agent.linkedAgents));

        agent.send(inform()
                .toLocal(agent.linkedAgents)
                .withContent(agent.freshAgentsNumbers)
                .build());

        agent.agentsNumbers.putAll(agent.freshAgentsNumbers);
        agent.freshAgentsNumbers.clear();

        state = State.Receive;
    }

    private void receive() {
        for (int i = 0; i < agent.linkedAgents.length; i++) {

            listen(agent, this).forMap(map -> {
                LOGGER.info(step + ") Received " + mapToString(map));

                map.keySet().removeAll(agent.agentsNumbers.keySet());
                agent.freshAgentsNumbers.putAll(map);
            });
        }

        state = agent.freshAgentsNumbers.size() > 0
                ? State.Send
                : State.End;
    }

    private void end() {
        String name = agent.getAID().getLocalName();

        if (name.equals(agent.agentsNumbers.keySet().stream()
                .max(Comparator.naturalOrder()).orElse(name))) {

            int sum = agent.agentsNumbers.values().stream().mapToInt(i -> i).sum();
            double average = (double) sum / agent.agentsNumbers.size();

            DecimalFormat df = new DecimalFormat("#.##");
            LOGGER.info(step + ") " + name
                    + " calculated average : " + df.format(average));

            ContainerKiller.killContainerOf(agent);
        }
    }
}
