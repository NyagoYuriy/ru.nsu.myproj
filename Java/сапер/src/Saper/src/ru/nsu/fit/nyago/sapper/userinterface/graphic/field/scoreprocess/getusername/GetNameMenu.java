package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess.getusername;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller.GraphicController;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.NewWindowPanel;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.ReturnAction;

import javax.swing.*;
import java.awt.*;

public final class GetNameMenu extends NewWindowPanel
{
    private static final int BORDER_WIDTH = 3;
    private static final int NAME_FIELD_LENGTH = 10;

    public GetNameMenu(GraphicController controller)
    {
        super(new BorderLayout());

        assert (null != controller);

        JLabel textLabel = new JLabel("Congratulations! Your name:");
        textLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, BORDER_WIDTH));
        add(textLabel, BorderLayout.NORTH);

        JTextField playerNameField = new JTextField(NAME_FIELD_LENGTH);
        add(playerNameField, BorderLayout.CENTER);

        JButton readyButton = new JButton("Ok");
        readyButton.addActionListener(new GetNameAction(playerNameField, controller));
        readyButton.addActionListener(new ReturnAction());
        add(readyButton, BorderLayout.SOUTH);
    }
}
