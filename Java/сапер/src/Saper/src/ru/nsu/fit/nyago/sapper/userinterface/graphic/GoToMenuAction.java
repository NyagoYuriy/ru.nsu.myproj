package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.GameDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class GoToMenuAction implements ActionListener
{
    private JPanel destinationMenu;

    public GoToMenuAction(JPanel destinationMenu)
    {
        assert (null != destinationMenu);

        this.destinationMenu = destinationMenu;
    }

    public void actionPerformed(ActionEvent event)
    {
        switchPanel(destinationMenu);
    }

    public static void switchPanel(JPanel newPanel)
    {
        assert (null != newPanel);
        new GameDialog(newPanel);
    }
}
