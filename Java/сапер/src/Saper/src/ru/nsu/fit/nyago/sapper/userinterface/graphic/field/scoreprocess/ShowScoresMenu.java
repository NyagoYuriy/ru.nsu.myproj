package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.scoreprocess;

import ru.nsu.fit.nyago.sapper.GraphicMain;
import ru.nsu.fit.nyago.sapper.scoreprocessor.ScoreProcessor;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.ReturnAction;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.NewWindowPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public final class ShowScoresMenu extends NewWindowPanel
{
    private static final String [] GAME_TYPE_NAMES = {"Novice", "Medium", "Expert"};
    private static final int BORDER_WIDTH = 60;

    private CardLayout layout;

    public ShowScoresMenu ()
    {
        super(new BorderLayout());
        layout = new CardLayout();

        final JPanel content = new JPanel(layout);
        for (int gameType = GraphicMain.NOVICE; gameType <= GraphicMain.EXPERT; gameType++)
        {
            content.add(GAME_TYPE_NAMES[gameType], createScoreLabel(ScoreProcessor.getTextScores(gameType)));
        }
        add(content, BorderLayout.CENTER);

        final JComboBox<String> gameTypeBox = new JComboBox<String>(GAME_TYPE_NAMES);
        gameTypeBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent event)
            {
                if (ItemEvent.SELECTED == event.getStateChange())
                {
                    layout.show(content, (String)gameTypeBox.getSelectedItem());
                }
            }
        });
        add(gameTypeBox, BorderLayout.NORTH);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(new ReturnAction());
        add(returnButton, BorderLayout.SOUTH);
    }

    private JLabel createScoreLabel(String content)
    {
        JLabel label = new JLabel(content);
        label.setPreferredSize(new Dimension(label.getPreferredSize().width + BORDER_WIDTH, label.getPreferredSize().height + BORDER_WIDTH));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}
