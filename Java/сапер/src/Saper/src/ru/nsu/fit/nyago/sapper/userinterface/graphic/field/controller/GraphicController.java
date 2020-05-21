package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller;

import ru.nsu.fit.nyago.sapper.GraphicMain;
import ru.nsu.fit.nyago.sapper.model.field.Field;
import ru.nsu.fit.nyago.sapper.GameException.GameException;
import ru.nsu.fit.nyago.sapper.scoreprocessor.ScoreProcessor;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GamePanel;
import ru.nsu.fit.nyago.sapper.timer.GameTimer;

import javax.swing.*;

public final class GraphicController
{
    private Field fieldModel;
    private GameTimer timer;

    private boolean gameEnded = false;
    private boolean gameOver = false;
    private boolean scoreAchieved = false;

    private int gameType;

    public GraphicController(Field fieldModel, int gameType)
    {
        assert (null != fieldModel);

        this.fieldModel = fieldModel;
        this.gameType = gameType;
        timer = new GameTimer();
    }

    public boolean isGameEnded()
    {
        return gameEnded;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public boolean isScoreAchieved()
    {
        return scoreAchieved;
    }

    public void rightClick(int x, int y)
    {
        try
        {
            fieldModel.changeOpenStatus(x, y);

            if ((0 < fieldModel.getOpenedCount()) && (false == timer.isRunning()))
            {
                timer.start();
            }
        }
        catch (GameException exception)
        {
            gameEnded = true;
            timer.stop();

            if (GameException.GAME_OVER == exception.getType())
            {
                GamePanel.setState(GamePanel.LOOSE_STATE);
                gameOver = true;
            }
            else
            {
                GamePanel.setState(GamePanel.WIN_STATE);

                if ((gameType != GraphicMain.CUSTOM) && ScoreProcessor.isScore(timer.getTime(), gameType))
                {
                    scoreAchieved = true;
                }
            }
        }
    }

    public void leftClick(int x, int y)
    {
        fieldModel.changeCheckStatus(x,y);
    }

    public int getUncheckedMineCount()
    {
        return fieldModel.countUncheckedMines();
    }

    public void saveScore(String name)
    {
        ScoreProcessor.addScore(GameTimer.getCurrentMinute(), GameTimer.getCurrentSecond(), name, gameType);
    }

    public void resetField()
    {
        gameEnded = false;
        gameOver = false;

        fieldModel.reset();
        timer.stop();
        timer.reset();
    }

    public void setTimeLabel(JLabel timeLabel)
    {
         timer.setTimeLabel(timeLabel);
    }
}
