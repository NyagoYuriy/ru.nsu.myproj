package ru.nsu.fit.nyago.sapper.GameException;

public final class GameException extends Exception
{
    public static final int GAME_OVER = 0;
    public static final int GAME_WON = 1;

    private int type;

    public int getType()
    {
        return type;
    }

    public GameException(int exceptionType)
    {
        type = exceptionType;
    }
}
