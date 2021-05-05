package example;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Hello3 {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String hello3(Map<String, Object> event) {
        var response = new HashMap<String, Object>();
		response.put("message", "Hello3");
		return gson.toJson(response);
    }
}
