package interpreter.util;

import java.util.HashMap;
import java.util.Map;

import interpreter.value.Value;

public class Memory {

    private static Map<String, Value<?>> memory = new HashMap<String, Value<?>>();

    public static Value<?> read(String name) {
        return memory.get(name);
    }

    public static void write(String name, Value<?> value) {
        memory.put(name, value);
    }

}