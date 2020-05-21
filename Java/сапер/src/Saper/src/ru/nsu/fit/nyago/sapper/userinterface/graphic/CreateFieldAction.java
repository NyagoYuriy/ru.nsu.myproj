package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.nsu.fit.nyago.sapper.GraphicMain;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.GameField;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.CustomMenu;

public final class CreateFieldAction implements ActionListener
{
    public static final int MINIMUM_SIZE = 8;
    public static final int MAXIMUM_SIZE = 30;
    public static final int MINIMUM_MINE_COUNT = 1;

    private static final int [] widthArray = {9, 18, 30};
    private static final int [] heightArray = {9, 12, 16};
    private static final int [] mineCountArray = {10, 40, 99};

    private GameFrame frame;

    private int fieldWidth;
    private int fieldHeight;
    private int mineCount;

    private int gameType;
    private static GamePanel currentGamePanel;

    public CreateFieldAction(GameFrame frame)
    {
        assert (null != frame);
        this.frame = frame;
        gameType = GraphicMain.CUSTOM;
    }

    public CreateFieldAction(GameFrame frame, int gameType)
    {
        assert (null != frame);
        assert (GraphicMain.CUSTOM > gameType);

        this.frame = frame;
        this.gameType = gameType;

        fieldWidth = widthArray[gameType];
        fieldHeight = heightArray[gameType];
        mineCount = mineCountArray[gameType];

        assert ((MAXIMUM_SIZE >= fieldHeight) && (MINIMUM_SIZE <= fieldHeight)
                && (MAXIMUM_SIZE >= fieldWidth) && (MINIMUM_SIZE <= fieldWidth)
                && (fieldHeight * fieldWidth > mineCount) && (MINIMUM_MINE_COUNT <= mineCount));
    }

    public CreateFieldAction(GameFrame frame, int fieldWidth, int fieldHeight, int mineCount)
    {
        assert (null != frame);

        this.frame = frame;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.mineCount = mineCount;
    }

    public boolean getParameters()
    {
        boolean incorrectWidth = false;
        boolean incorrectHeight = false;
        boolean incorrectMineCount = false;

        try
        {
            fieldWidth = CustomMenu.getFieldWidth();
        }
        catch (NumberFormatException exception)
        {
            incorrectWidth = true;
        }

        try
        {
            fieldHeight = CustomMenu.getFieldHeight();
        }
        catch (NumberFormatException exception)
        {
            incorrectHeight = true;
        }

        try
        {
            mineCount = CustomMenu.getMineCount();
        }
        catch (NumberFormatException exception)
        {
            incorrectMineCount = true;
        }

        if ((MAXIMUM_SIZE < fieldHeight) || (MINIMUM_SIZE > fieldHeight))
        {
            incorrectHeight = true;
        }
        if ((MAXIMUM_SIZE < fieldWidth) || (MINIMUM_SIZE > fieldWidth))
        {
            incorrectWidth = true;
        }
        if ((fieldHeight * fieldWidth <= mineCount) || (mineCount < MINIMUM_MINE_COUNT))
        {
            incorrectMineCount = true;
        }

        CustomMenu.setHighlight(incorrectWidth, incorrectHeight, incorrectMineCount);
        return incorrectHeight || incorrectWidth || incorrectMineCount;
    }

    public void actionPerformed(ActionEvent event)
    {
        if (GraphicMain.CUSTOM == gameType)
        {
            boolean isParametersIncorrect = getParameters();
            if (isParametersIncorrect)
            {
                return;
            }
        }

        GameField gameField = new GameField(fieldWidth, fieldHeight, mineCount, frame, gameType);

        if (null != currentGamePanel)
        {
            frame.remove(currentGamePanel);
        }
        currentGamePanel = new GamePanel(gameField.getFieldView(), frame);
        frame.add(currentGamePanel);
        frame.pack();

        if (GraphicMain.CUSTOM == gameType)
        {
            new ReturnAction().actionPerformed(null);
        }
    }
}
