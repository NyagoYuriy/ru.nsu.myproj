package ru.nsu.fit.nyago.sapper.userinterface.graphic;

import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.view.GraphicView;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.gameinterface.menus.actions.RestartGameAction;

import javax.swing.*;
import java.awt.*;

public final class GamePanel extends JPanel
{
    private static JButton smileButton;

    public static final int GAMING_STATE = 0;
    public static final int WIN_STATE = 1;
    public static final int LOOSE_STATE = 2;

    public static final int MIN_STATE = 0;
    public static final int MAX_STATE = 2;

    private static final ImageIcon [] pictureArray = {  new ImageIcon("resource\\solving.png"),
                                                        new ImageIcon("resource\\fuckYeah.png"),
                                                        new ImageIcon("resource\\fuu.png")       };

    private static final int PICTURE_WIDTH = pictureArray[0].getIconWidth();
    private static final int PICTURE_HEIGHT = pictureArray[0].getIconHeight();
    private static final int BORDER_WIDTH = 3;
    private static final int OFFSET = 2*BORDER_WIDTH;

    private static final int FRAME_WIDTH_OFFSET = 22;
    private static final int FRAME_HEIGHT_OFFSET = 65;

    private static int initialFieldWidth;
    private static int initialFieldHeight;

    private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 24);
    private static final Color TEXT_COLOR = new Color(-14348400);

    private static final Insets NO_BORDER_INSETS = new Insets(0, 0, 0, 0);
    private static final Insets FIELD_INSETS = new Insets(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);

    private static final GridBagConstraints MINE_COUNT_LABEL_PLACE = new GridBagConstraints(0, 0, 1, 1, 1, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, NO_BORDER_INSETS, 0, 0);
    private static final GridBagConstraints SMILE_BUTTON_PLACE = new GridBagConstraints(1, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, NO_BORDER_INSETS, 0, 0);
    private static final GridBagConstraints TIME_LABEL_PLACE = new GridBagConstraints(2, 0, 1, 1, 1, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, NO_BORDER_INSETS, 0, 0);
    private static final GridBagConstraints GAME_FIELD_PLACE = new GridBagConstraints(0, 1, 3, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, FIELD_INSETS, 0, 0);

    private static final String ZERO_TIME = "0:0";

    public GamePanel(GraphicView fieldView, GameFrame frame)
    {
        super(new GridBagLayout());

        initialFieldWidth = fieldView.getInitialWidth();
        initialFieldHeight = fieldView.getInitialHeight();

        JLabel mineCountLabel = createLabel("");
        mineCountLabel.setFont(TEXT_FONT);
        mineCountLabel.setForeground(TEXT_COLOR);

        JLabel timeLabel = createLabel(ZERO_TIME);
        timeLabel.setFont(TEXT_FONT);
        timeLabel.setForeground(TEXT_COLOR);

        fieldView.setTimeLabel(timeLabel);

        smileButton = new JButton(pictureArray[GamePanel.GAMING_STATE]);
        smileButton.setMinimumSize(new Dimension(PICTURE_WIDTH, PICTURE_HEIGHT));
        smileButton.setPreferredSize(smileButton.getMinimumSize());
        smileButton.addActionListener(new RestartGameAction(fieldView));

        fieldView.setMinimumSize(new Dimension(initialFieldWidth + OFFSET, initialFieldHeight + OFFSET));
        fieldView.setPreferredSize(fieldView.getMinimumSize());
        fieldView.setBorder(BorderFactory.createLineBorder(TEXT_COLOR, BORDER_WIDTH));

        add(mineCountLabel, MINE_COUNT_LABEL_PLACE);
        add(smileButton, SMILE_BUTTON_PLACE);
        add(timeLabel, TIME_LABEL_PLACE);
        add(fieldView, GAME_FIELD_PLACE);

        fieldView.addMineCounter(mineCountLabel);

        final int FRAME_MINIMUM_WIDTH = initialFieldWidth + OFFSET + FRAME_WIDTH_OFFSET;
        final int FRAME_MINIMUM_HEIGHT = initialFieldHeight + OFFSET + PICTURE_HEIGHT + FRAME_HEIGHT_OFFSET;

        frame.setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
    }

    private JLabel createLabel(String caption)
    {
        final int PREFERRED_LABEL_WIDTH = (initialFieldWidth - PICTURE_WIDTH) / 2;

        JLabel label = new JLabel(caption);
        label.setPreferredSize(new Dimension(PREFERRED_LABEL_WIDTH, PICTURE_HEIGHT));
        label.setMinimumSize(label.getPreferredSize());
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public static void setState(int state)
    {
        assert ((MIN_STATE <= state) && (MAX_STATE >= state));
        smileButton.setIcon(pictureArray[state]);
    }
}