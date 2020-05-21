package ru.nsu.fit.nyago.sapper.userinterface.graphic;


public final class GIThread implements Runnable
{
    private String windowName;

    public GIThread(String windowName)
    {
        this.windowName = windowName;
    }

    @Override
    public void run()
    {
        new GameFrame(windowName);
    }
}
