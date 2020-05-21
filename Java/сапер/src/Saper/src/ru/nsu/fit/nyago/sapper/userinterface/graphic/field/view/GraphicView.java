package ru.nsu.fit.nyago.sapper.userinterface.graphic.field.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import ru.nsu.fit.nyago.sapper.model.field.Field;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.field.controller.GraphicController;
import ru.nsu.fit.nyago.sapper.model.cell.Cell;
import ru.nsu.fit.nyago.sapper.userinterface.graphic.GameFrame;
import sun.awt.image.ToolkitImage;

public final class GraphicView extends JPanel
{
    private static final int DIGIT_COUNT = 9;
    private static final int BORDER_WIDTH = 3;
    private static final int BORDER_HEIGHT = 3;

    private static final int PICTURE_COUNT = 13;
    private static final int CELL_IMAGE = 9;
    private static final int MINE_IMAGE = 10;
    private static final int QUESTION_IMAGE = 11;
    private static final int FLAG_IMAGE = 12;

    private static final String RESOURCE_PATH = "resource\\";
    private static final String FILE_TYPE = ".jpg";
    private static final String DIGIT_IMAGE_NAME = "n";

    private static final String CELL_PATH = RESOURCE_PATH + "cell" + FILE_TYPE;
    private static final String FLAG_PATH = RESOURCE_PATH + "flag" + FILE_TYPE;
    private static final String MINE_PATH = RESOURCE_PATH + "mine" + FILE_TYPE;
    private static final String QUESTION_PATH = RESOURCE_PATH + "question" + FILE_TYPE;

    private static final int INITIAL_CELL_WIDTH = 20;
    private static final int INITIAL_CELL_HEIGHT = 20;

    private static int currentCellWidth = INITIAL_CELL_WIDTH;
    private static int currentCellHeight = INITIAL_CELL_HEIGHT;

    private static BufferedImage[] INITIAL_PICTURES = new BufferedImage[PICTURE_COUNT];
    private static ToolkitImage[] images = new ToolkitImage[PICTURE_COUNT];

    private static int fieldWidth;
    private static int fieldHeight;
    private static GraphicController fieldController;
    private static Field fieldModel;

    private static JLabel mineCountLabel;

    private static boolean picturesInitialased = false;

    public GraphicView(GraphicController fieldController, Field fieldModel, GameFrame gameFrame)//, int gameType)
    {
        assert (null != fieldController);
        assert (null != fieldModel);
        assert (null != gameFrame);

        if (false == picturesInitialased)
        {
            try
            {
                for (int i = 0 ; i < DIGIT_COUNT; i++)
                {
                    INITIAL_PICTURES[i] = ImageIO.read(new File(RESOURCE_PATH + DIGIT_IMAGE_NAME + i + FILE_TYPE));
                }
                INITIAL_PICTURES[CELL_IMAGE] = ImageIO.read(new File(CELL_PATH));
                INITIAL_PICTURES[FLAG_IMAGE] = ImageIO.read(new File(FLAG_PATH));
                INITIAL_PICTURES[QUESTION_IMAGE] = ImageIO.read(new File(QUESTION_PATH));
                INITIAL_PICTURES[MINE_IMAGE] = ImageIO.read(new File(MINE_PATH));

                picturesInitialased = true;
            }
            catch (IOException exception)
            {
                System.out.println("Game files are damaged. Reinstalling game may fix this error");
                System.exit(0);
            }
        }

        setSizeOfImages(INITIAL_CELL_WIDTH, INITIAL_CELL_HEIGHT);

        this.fieldWidth = fieldModel.getXSize();
        this.fieldHeight = fieldModel.getYSize();
        this.fieldController = fieldController;
        this.fieldModel = fieldModel;

        addMouseListener(new GameMouseListener(this, fieldController, gameFrame));//, gameType));
    }

    public void paintComponent(Graphics graphics)
    {
        for (int cellX = 0; fieldWidth > cellX; cellX++)
        {
            for (int cellY = 0; fieldHeight > cellY; cellY++)
            {
                int cellWidth = getWidth() / fieldWidth;
                int cellHeight = getHeight() / fieldHeight;

                if ((currentCellHeight != cellHeight) || (currentCellWidth != cellWidth))
                {
                    setSizeOfImages(cellWidth, cellHeight);
                    currentCellHeight = cellHeight;
                    currentCellWidth = cellWidth;
                }

                ToolkitImage drawingImage = images[CELL_IMAGE];

                if (fieldModel.isOpened(cellX, cellY))
                {
                    int mineCount = fieldModel.countNeighbourMines(cellX, cellY);
                    drawingImage = images[mineCount];
                }
                else if (fieldController.isGameOver() && (Cell.MINE == fieldModel.getCellContent(cellX, cellY)))
                {
                    drawingImage = images[MINE_IMAGE];
                }
                else if (fieldModel.isChecked(cellX, cellY))
                {
                    drawingImage = images[FLAG_IMAGE];
                }
                else if (fieldModel.isQuestioned(cellX, cellY))
                {
                    drawingImage = images[QUESTION_IMAGE];
                }

                graphics.drawImage(drawingImage, cellX * cellWidth + BORDER_WIDTH,
                        cellY * cellHeight + BORDER_HEIGHT, this);
            }
        }

        if (null != mineCountLabel)
        {
            mineCountLabel.setText("" + fieldController.getUncheckedMineCount());
        }
    }

    public void setSizeOfImages(int newWidth, int newHeight)
    {
        assert (0 <= newHeight);
        assert (0 <= newWidth);

        for (int pictureNumber = 0; pictureNumber < PICTURE_COUNT; pictureNumber++)
        {
            images[pictureNumber] = (ToolkitImage) INITIAL_PICTURES[pictureNumber]
                    .getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_AREA_AVERAGING);
        }
    }

    public int getFieldHeight()
    {
        return fieldHeight;
    }

    public int getFieldWidth()
    {
        return fieldWidth;
    }

    public int getInitialWidth()
    {
        return (fieldWidth * INITIAL_CELL_WIDTH);
    }

    public int getInitialHeight()
    {
        return (fieldHeight * INITIAL_CELL_HEIGHT);
    }

    public void repaintIfGameEnded()
    {
        if(isGameEnded())
        {
            repaint();
        }
    }

    public boolean isGameEnded()
    {
        return fieldController.isGameEnded();
    }

    public void addMineCounter(JLabel mineCountLabel)
    {
        assert (null != mineCountLabel);
        this.mineCountLabel = mineCountLabel;
    }

    public static int getCellWidth()
    {
        return currentCellWidth;
    }

    public static int getCellHeight()
    {
        return currentCellHeight;
    }

    public void setTimeLabel(JLabel timeLabel)
    {
        fieldController.setTimeLabel(timeLabel);
    }

    public void reset()
    {
        fieldController.resetField();
        repaint();
    }
}