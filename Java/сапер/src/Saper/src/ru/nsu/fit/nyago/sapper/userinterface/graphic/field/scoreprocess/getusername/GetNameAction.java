package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess.getusername;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.GoToMenuAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller.GraphicController;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess.ShowScoresMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class GetNameAction implements ActionListener
{
    private JTextField textField;
    GraphicController controller;

    public GetNameAction(JTextField textField, GraphicController controller)
    {
        assert (null != textField);

        this.textField = textField;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event)
    {
        controller.saveScore(textField.getText());
        GoToMenuAction.switchPanel(new ShowScoresMenu());
    }
}
