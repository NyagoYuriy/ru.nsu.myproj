package ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus;

import javax.swing.*;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.CreateFieldAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.NewWindowPanel;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.ReturnAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GameFrame;

import java.awt.*;

public final class CustomMenu extends NewWindowPanel
{
    private static final JTextField widthEditor = new JTextField();
    private static final JTextField heightEditor = new JTextField();
    private static final JTextField mineCountEditor = new JTextField();

    private static JLabel widthLabel = new JLabel("Width (between " + CreateFieldAction.MINIMUM_SIZE + " and "
                                                  + CreateFieldAction.MAXIMUM_SIZE + "):");
    private static JLabel heightLabel = new JLabel("Height (between " + CreateFieldAction.MINIMUM_SIZE + " and "
                                                   + CreateFieldAction.MAXIMUM_SIZE + "):");
    private static JLabel mineCountLabel = new JLabel("Mine count (less than cell count):");

    private static final Font USUAL_FONT = new Font(Font.SERIF, Font.PLAIN, 15);
    private static final Color USUAL_COLOR = Color.BLACK;
    private static final Color HIGHLITING_COLOR = Color.RED;

    private static final int GRID_HEIGHT = 4;
    private static final int GRID_WIDTH = 2;

    public CustomMenu(GameFrame gameFrame)
    {
        super(new GridLayout(GRID_HEIGHT, GRID_WIDTH));

        assert (null != gameFrame);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(new CreateFieldAction(gameFrame));

        JButton returnButton = new JButton("Cancel");
        returnButton.addActionListener(new ReturnAction());

        widthLabel.setFont(USUAL_FONT);
        heightLabel.setFont(USUAL_FONT);
        mineCountLabel.setFont(USUAL_FONT);
        setHighlight(false, false, false);

        add(widthLabel);
        add(widthEditor);
        add(heightLabel);
        add(heightEditor);
        add(mineCountLabel);
        add(mineCountEditor);
        add(startGameButton);
        add(returnButton);
    }

    public static int getFieldWidth() throws NumberFormatException
    {
        return Integer.parseInt(widthEditor.getText());
    }

    public static int getFieldHeight() throws NumberFormatException
    {
        return Integer.parseInt(heightEditor.getText());
    }

    public static int getMineCount() throws NumberFormatException
    {
        return Integer.parseInt(mineCountEditor.getText());
    }

    public static void setHighlight(boolean widthHighlight, boolean heightHighlight, boolean mineCountHighlight)
    {
        widthLabel.setForeground(widthHighlight ? HIGHLITING_COLOR : USUAL_COLOR);
        heightLabel.setForeground(heightHighlight ? HIGHLITING_COLOR : USUAL_COLOR);
        mineCountLabel.setForeground(mineCountHighlight ? HIGHLITING_COLOR : USUAL_COLOR);
    }
}
