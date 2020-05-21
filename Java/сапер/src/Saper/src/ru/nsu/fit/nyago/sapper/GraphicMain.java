package ru.nsu.fit.nyago.sapper;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.GIThread;

public final class GraphicMain
{
    private static final String WINDOW_NAME = "Sapper";
    public static final int NOVICE = 0;
    public static final int MEDIUM = 1;
    public static final int EXPERT = 2;
    public static final int CUSTOM = 3;

    public static void main(String args[])
    {
        try
        {
            new Thread(new GIThread(WINDOW_NAME)).run();
        }
        catch (Throwable exception)
        {
            System.out.println("Unknown error was found.");
            exception.printStackTrace();
        }
    }
}
