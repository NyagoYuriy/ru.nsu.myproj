package ru.my.source;

public class Parser {
    public String[] parseLine(String line) {
        String[] arguments = line.split(" ");
        return arguments;
    }

}
