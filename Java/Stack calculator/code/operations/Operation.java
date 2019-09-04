package ru.my.operations;

import ru.my.exception.*;
import ru.my.source.Context;

public interface Operation {
    void doAction(Context context, String[] arguments) throws CalcException;
}
