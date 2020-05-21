package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import javax.swing.*;
import java.awt.*;

public class NewWindowPanel extends JPanel
{
    private static JDialog parentDialog;

    public NewWindowPanel(LayoutManager layout)
    {
        super(layout);
    }

    public static void setParentDialog(JDialog newParentDialog)
    {
        assert (null != newParentDialog);
        parentDialog = newParentDialog;
    }

    public static JDialog getParentDialog()
    {
        return parentDialog;
    }
}
