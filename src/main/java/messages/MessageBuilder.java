package messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import static tools.ToStringSerializer.serializeToString;

public class MessageBuilder {
    private final ACLMessage message;

    private MessageBuilder(int performative) {
        this.message = new ACLMessage(performative);
    }

    public static MessageBuilder inform() {
        return new MessageBuilder(ACLMessage.INFORM);
    }

    public MessageBuilder toLocal(String... otherAgentNames) {
        for (String agentName : otherAgentNames) {
            AID address = new AID(agentName, AID.ISLOCALNAME);
            message.addReceiver(address);
        }
        return this;
    }

    public MessageBuilder withContent(String content) {
        message.setContent(content);
        return this;
    }

    public ACLMessage build() {
        return message;
    }

    public MessageBuilder withContent(HashMap<String, Integer> map) {
        return withContent(serializeToString(map));
    }
}
