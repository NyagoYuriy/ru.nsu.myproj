package ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface;

import javax.swing.*;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.NewWindowPanel;

import java.awt.*;

public final class GameDialog extends JDialog
{
    public GameDialog(JPanel content)
    {
        super(new JFrame(), true);

        assert (null != content);

        super.setResizable(false);
        add(content);
        pack();
        setCenterPosition();
        NewWindowPanel.setParentDialog(this);
        setVisible(true);
    }

    public void setCenterPosition()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int VERTICAL_OFFSET = (int) screenSize.getHeight() / 5;
        final int HORIZONTAL_OFFSET = (int) screenSize.getWidth() * 2 / 5;

        this.setLocation(HORIZONTAL_OFFSET, VERTICAL_OFFSET);
    }
}
