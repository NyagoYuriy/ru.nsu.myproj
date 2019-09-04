package ru.my.operations;

import ru.my.exception.CalcException;
import ru.my.exception.FewArgException;
import ru.my.source.Context;

public class PlusOperation implements Operation{

    @Override
    public void doAction(Context context, String[] arguments) throws CalcException {
        if (context.stackSize() < 2)
            throw new FewArgException("Few arguments");
        else
            context.push(context.pop() + context.pop());
    }
}
