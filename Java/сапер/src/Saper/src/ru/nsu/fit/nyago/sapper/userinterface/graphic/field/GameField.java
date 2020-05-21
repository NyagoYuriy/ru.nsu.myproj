package ru.nsu.fit.nyago.sapper.userinterface.graphic.field;

import ru.nsu.fit.nyago.sapper.model.field.Field;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller.GraphicController;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.view.GraphicView;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GameFrame;

public final class GameField
{
    private static int gameType;
    private static GraphicView fieldView;
    private static Field fieldModel;

    public GameField(int fieldWidth, int fieldHeight, int mineCount, GameFrame frame, int type)
    {
        assert (null != frame);

        gameType = type;
        fieldModel = new Field(fieldWidth, fieldHeight, mineCount);
        GraphicController fieldController = new GraphicController(fieldModel, type);
        fieldView  = new GraphicView(fieldController, fieldModel, frame);
    }

    public GraphicView getFieldView()
    {
        return fieldView;
    }

    public static int getFieldWidth()
    {
        return fieldModel.getXSize();
    }

    public static int getFieldHeight()
    {
        return fieldModel.getYSize();
    }

    public static int getMineCount()
    {
        return fieldModel.getMineCount();
    }

    public static int getGameType()
    {
        return gameType;
    }
}
