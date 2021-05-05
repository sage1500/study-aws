package example;

import java.util.HashMap;
import java.util.Map;

public class Hello4 {
    public Map<String, Object> hello4(Map<String, Object> event) {
        var response = new HashMap<String, Object>();
		response.put("message", "Hello4 " + event.get("name"));
		return response;
    }
}
