package ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.actions;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.DataSavingExit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ExitAction implements ActionListener
{
    public void actionPerformed(ActionEvent event)
    {
        new DataSavingExit().windowClosing(null);
    }
}
