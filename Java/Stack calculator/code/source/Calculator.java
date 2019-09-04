package ru.my.source;

import ru.my.exception.CalcException;
import ru.my.exception.UnknownCommandException;
import ru.my.factory.Fact;
import ru.my.operations.Operation;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Calculator {
    public void calculate(String[] args) {
        Fact factory = Fact.getInstance();
        Context context = new Context();
        Parser parser = new Parser();
        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(args.length == 0 ? "" : args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String newLine = "#";
        while (newLine != "") {
            try {
                newLine = tokenizer.nextLine();
                String[] arguments = parser.parseLine(newLine);
                if (!arguments[0].substring(0, 1).equals("#")) {
                    Operation currentOperation = factory.createOperation(arguments[0]);
                    currentOperation.doAction(context, arguments);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (UnknownCommandException e) {
                System.err.println(e.getMessage());
            } catch (CalcException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
