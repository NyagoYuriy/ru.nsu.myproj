package ru.my.operations;

import ru.my.exception.CalcException;
import ru.my.source.Context;
import ru.my.exception.DivisionByZeroException;

public class DivisionOperation implements Operation {
    @Override
    public void doAction(Context context, String[] arguments) throws CalcException {
        double value1 = context.pop();
        double value2 = context.pop();
        if (value2 == 0)
            throw new DivisionByZeroException("Division by zero");
        context.push(value1 / value2);
    }
}
