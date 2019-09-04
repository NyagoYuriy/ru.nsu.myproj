package ru.my.operations;

import ru.my.exception.CalcException;
import ru.my.source.Context;
import ru.my.exception.FewArgException;

public class PrintOperation implements Operation {
    @Override
    public void doAction(Context context, String[] arguments) throws CalcException {
        if (context.stackSize() == 0)
            throw new FewArgException("Stack has few arguments");
        else
            System.out.println(context.getStackPeek());
    }
}
