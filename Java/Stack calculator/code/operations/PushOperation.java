package ru.my.operations;

import ru.my.exception.CalcException;
import ru.my.source.Context;
import ru.my.exception.FewArgException;
import ru.my.exception.ManyArgException;
import ru.my.exception.NoSuchVariableException;

public class PushOperation implements Operation {

    @Override
    public void doAction(Context context, String[] arguments) throws CalcException {
        if (arguments.length < 2)
            throw new FewArgException("Few arguments");
        else if (arguments.length > 2)
            throw new ManyArgException("Many arguments");
        else
            context.push(arguments[1]);
    }
}
