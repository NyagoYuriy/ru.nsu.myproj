package ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.actions;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.view.GraphicView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.GamePanel;

public final class RestartGameAction implements ActionListener
{
    private GraphicView fieldView;

    public RestartGameAction(GraphicView fieldView)
    {
        assert (null != fieldView);
        this.fieldView = fieldView;
    }

    public void actionPerformed(ActionEvent event)
    {
        fieldView.reset();
        GamePanel.setState(GamePanel.GAMING_STATE);
    }
}