package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import ru.nsu.fit.nyago.sapper.GraphicMain;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.actions.ExitAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.CustomMenu;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess.ShowScoresMenu;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public final class GameFrame extends JFrame
{
    public static final String OPTION_FILE = "resource\\data\\options";
    private static final Font MENU_FONT = new Font("Verdana", Font.PLAIN, 11);

    public GameFrame(String windowName)
    {
        super(windowName);

        assert (null != windowName);

        JFrame.setDefaultLookAndFeelDecorated(true);

        addWindowListener(new DataSavingExit());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createInterface();
        setCenterPosition();
        createStartField();
        pack();
        setVisible(true);
    }

    public void setCenterPosition()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int VERTICAL_OFFSET = (int) screenSize.getHeight() / 4;
        final int HORIZONTAL_OFFSET = (int) screenSize.getWidth() / 3;

        setLocation(HORIZONTAL_OFFSET, VERTICAL_OFFSET);
    }

    public void createInterface()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setFont(MENU_FONT);

        JMenu newMenu = new JMenu("New");
        newMenu.setFont(MENU_FONT);
        gameMenu.add(newMenu);

        addStartGameItem("Novice", GraphicMain.NOVICE, newMenu);
        addStartGameItem("Medium", GraphicMain.MEDIUM, newMenu);
        addStartGameItem("Expert", GraphicMain.EXPERT, newMenu);

        JMenuItem customItem = new JMenuItem("Custom");
        customItem.addActionListener(new GoToMenuAction(new CustomMenu(this)));
        customItem.setFont(MENU_FONT);
        newMenu.add(customItem);

        JMenuItem scoresItem = new JMenuItem("Scores");
        scoresItem.addActionListener(new GoToMenuAction(new ShowScoresMenu()));
        scoresItem.setFont(MENU_FONT);
        gameMenu.add(scoresItem);

        gameMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(MENU_FONT);
        gameMenu.add(exitItem);

        exitItem.addActionListener(new ExitAction());

        menuBar.add(gameMenu);

        this.setJMenuBar(menuBar);
    }

    private void addStartGameItem(String name, int gameType, JMenu parent)
    {
        JMenuItem newItem = new JMenuItem(name);
        newItem.addActionListener(new CreateFieldAction(this, gameType));
        newItem.setFont(MENU_FONT);
        parent.add(newItem);
    }

    private void createStartField()
    {
        Reader reader = null;

        try
        {
            reader = new InputStreamReader(new FileInputStream(OPTION_FILE));

            int mode = readInt(reader);

            CreateFieldAction startField = null;

            if (GraphicMain.CUSTOM == mode)
            {
                try
                {
                    startField = new CreateFieldAction(this, readInt(reader), readInt(reader), readInt(reader));
                }
                catch (NumberFormatException exception)
                {
                    mode = GraphicMain.NOVICE;
                }
            }

            if (null == startField)
            {
                startField = new CreateFieldAction(this, mode);
            }

            startField.actionPerformed(null);
        }
        catch (IOException catchValue)
        {
            System.err.println("Error while reading file: " + catchValue.getLocalizedMessage());
        }
        finally
        {
            if (null != reader)
            {
                try
                {
                    reader.close();
                }
                catch (IOException catchValue)
                {
                    catchValue.printStackTrace(System.err);
                }
            }
        }
    }

    public static int readInt(Reader reader) throws IOException, NumberFormatException
    {
        StringBuilder stringResult = new StringBuilder();

        for(;reader.ready();)
        {
            char readingChar = (char)reader.read();

            if (Character.isDigit(readingChar))
            {
                stringResult.append(readingChar);
                break;
            }
        }

        for(;reader.ready();)
        {
            char readingChar = (char)reader.read();

            if (false == Character.isDigit(readingChar))
            {
                break;
            }

            stringResult.append(readingChar);
        }

        return Integer.parseInt(stringResult.toString());
    }
}

