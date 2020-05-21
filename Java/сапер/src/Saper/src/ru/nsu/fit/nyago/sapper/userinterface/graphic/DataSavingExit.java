package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import ru.nsu.fit.nyago.sapper.GraphicMain;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.GameField;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public final class DataSavingExit implements WindowListener
{
    private static final int EXIT_SUCCESS = 0;

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e)
    {
        PrintWriter printWriter = null;

        try
        {
            printWriter = new PrintWriter(GameFrame.OPTION_FILE);

            int gameType = GameField.getGameType();
            printWriter.println(gameType);

            if (GraphicMain.CUSTOM == gameType)
            {
                printWriter.println(GameField.getFieldWidth());
                printWriter.println(GameField.getFieldHeight());
                printWriter.println(GameField.getMineCount());
            }
        }
        catch (FileNotFoundException catchValue)
        {
            System.err.println("Error while writing score file");
        }
        finally
        {
            if (null != printWriter)
            {
                printWriter.close();
            }
        }

        System.exit(EXIT_SUCCESS);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
