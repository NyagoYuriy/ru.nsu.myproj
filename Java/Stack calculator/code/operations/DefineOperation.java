package ru.my.operations;

import ru.my.exception.CalcException;
import ru.my.source.Context;
import ru.my.exception.FewArgException;
import ru.my.exception.ManyArgException;

public class DefineOperation implements Operation{

    @Override
    public void doAction(Context context, String[] arguments) throws CalcException {
        if (arguments.length < 3)
            throw new FewArgException("Few arguments");
        else if (arguments.length > 3)
            throw new ManyArgException("Many arguments");
        else
            context.setVariable(arguments[1], Double.valueOf(arguments[2]));
    }
}

