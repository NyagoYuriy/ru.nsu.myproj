package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.view;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller.GraphicController;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GoToMenuAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GameFrame;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess.getusername.GetNameMenu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class GameMouseListener implements MouseListener
{
    private int fieldWidth;
    private int fieldHeight;

    private GraphicController fieldController;
    private GraphicView graphicView;

    public GameMouseListener(GraphicView graphicView, GraphicController fieldController, GameFrame gameFrame)
    {
        assert (null != graphicView);
        assert (null != fieldController);
        assert (null != gameFrame);

        fieldWidth = graphicView.getFieldWidth();
        fieldHeight = graphicView.getFieldHeight();

        this.fieldController = fieldController;
        this.graphicView = graphicView;
    }

    public void mouseReleased(MouseEvent event)
    {
        if (false == fieldController.isGameEnded())
        {
            final int cellX = event.getX() / GraphicView.getCellWidth();
            final int cellY = event.getY() / GraphicView.getCellHeight();

            if ((cellX >= fieldWidth) || (cellX < 0) || (cellY >= fieldHeight) || (cellY < 0))
            {
                return;
            }

            if (MouseEvent.BUTTON1 == event.getButton())
            {
                fieldController.rightClick(cellX, cellY);
                graphicView.repaintIfGameEnded();

                if (fieldController.isScoreAchieved())
                {
                    GoToMenuAction.switchPanel(new GetNameMenu(fieldController));
                }
            }
            else if (MouseEvent.BUTTON3 == event.getButton())
            {
                fieldController.leftClick(cellX, cellY);
            }

            graphicView.repaint();
        }
    }

    public void mouseEntered(MouseEvent e)
    {
        graphicView.setCursor(Cursor.getDefaultCursor());
    }

    public void mousePressed(MouseEvent e){}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}