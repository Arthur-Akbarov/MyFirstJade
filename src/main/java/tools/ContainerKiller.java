package tools;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class ContainerKiller {
    public static void killContainerOf(Agent agent) {

        AgentContainer container = agent.getContainerController();

        agent.doDelete();

        new Thread(() -> {
            try {
                container.kill();
            } catch (StaleProxyException ignored) {
            }
        }).start();
    }
}
