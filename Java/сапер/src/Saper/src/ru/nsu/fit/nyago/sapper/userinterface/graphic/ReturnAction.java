package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ReturnAction implements ActionListener
{
    public void actionPerformed(ActionEvent event)
    {
        NewWindowPanel.getParentDialog().dispose();
    }
}
