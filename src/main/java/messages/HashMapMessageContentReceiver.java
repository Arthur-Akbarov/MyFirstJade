package messages;

import java.util.HashMap;

public interface HashMapMessageContentReceiver {
    void onMessage(HashMap<String, Integer> message);
}
