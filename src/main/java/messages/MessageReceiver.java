package messages;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import static tools.ToStringSerializer.deserializeFromString;

public class MessageReceiver {

    private final Agent agent;
    private final Behaviour behavior;

    private MessageReceiver(Agent agent, Behaviour behavior) {
        this.agent = agent;
        this.behavior = behavior;
    }

    public static MessageReceiver listen(Agent agent, Behaviour behavior) {
        return new MessageReceiver(agent, behavior);
    }

    public void forMap(HashMapMessageContentReceiver contentReceiver) {
        ACLMessage message = agent.receive();

        if (message != null) {

            @SuppressWarnings("unchecked")
            HashMap<String, Integer> map = (HashMap<String, Integer>)
                    deserializeFromString(message.getContent());

            contentReceiver.onMessage(map);

        } else
            behavior.block();
    }
}
