package ru.my.operations;

import ru.my.source.Context;

public class MultiplyOperation implements Operation {
    @Override
    public void doAction(Context context, String[] arguments){
        context.push(context.pop() * context.pop());
    }
}
