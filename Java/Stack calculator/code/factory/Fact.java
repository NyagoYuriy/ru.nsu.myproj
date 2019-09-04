package ru.my.factory;

import ru.my.exception.CalcException;
import ru.my.exception.UnknownCommandException;
import ru.my.operations.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Fact {
    final String path = "config.properties";
    Properties properties = new Properties();
    Map<String, Operation> operationMap = new HashMap<>();

    private static Fact ourInstance = new Fact();

    public static Fact getInstance() {
        return ourInstance;
    }

    private Fact() {
        init();
    }
    public void init() {
        InputStream is = Fact.class.getResourceAsStream(path);
        try {
            properties.load(is);
            properties.forEach((k,v) -> {
                try {
                    operationMap.put(k.toString(), (Operation) Class.forName(v.toString()).newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Operation createOperation(String name) throws CalcException {
        if (properties.getProperty(name) == null)
            throw new UnknownCommandException("No such command");
            return operationMap.get(name);
    }
}


