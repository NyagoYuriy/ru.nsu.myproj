package ru.my.source;

import ru.my.exception.NoSuchVariableException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Context {
    private Map<String, Double> variables = new HashMap<>();
    private Stack<Double> stack = new Stack<>();

    public void setVariable(String name, double value) {
        variables.put(name, value);
    }
    public double getVariable(String name) throws NoSuchVariableException {
        if (variables.get(name) == null)
            throw new NoSuchVariableException("No such variable");
        else
            return variables.get(name);
    }
    private boolean isDigit(String s) {
        try{
            Double.parseDouble(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public void removePeek() {
        stack.pop();
    }
    public double pop() {
        return stack.pop();
    }
    public void push(String value) throws NoSuchVariableException {
        if (isDigit(value))
            stack.push(Double.parseDouble(value));
        else
            stack.push(getVariable(value));
    }
    public void push(double value) {
        stack.push(value);
    }

    public double getStackPeek() {
        return stack.peek();
    }
    public int stackSize() {return stack.size(); }
}
