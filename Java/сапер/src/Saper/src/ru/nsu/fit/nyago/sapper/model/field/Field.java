package ru.nsu.fit.nyago.sapper.model.field;

import ru.nsu.fit.nyago.sapper.model.cell.Cell;
import ru.nsu.fit.nyago.sapper.GameException.GameException;

import java.awt.*;
import java.util.Random;
import java.util.Stack;

public final class Field
{
    private int xSize;
    private int ySize;

    private Cell[][] cellArray;

    private int flagCount;
    private int openedCount;
    private final int mineCount;

    public int getYSize()
    {
        return ySize;
    }

    public int getXSize()
    {
        return xSize;
    }

    public int getMineCount()
    {
        return mineCount;
    }

    public int getOpenedCount()
    {
        return openedCount;
    }

    public boolean getCellContent(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));
        return cellArray[x][y].getContent();
    }

    public Field(int fieldXSize, int fieldYSize, int mineCount)
    {
        xSize = fieldXSize;
        ySize = fieldYSize;

        flagCount = 0;
        openedCount = 0;
        this.mineCount = mineCount;

        cellArray = new Cell[xSize][];

        for (int x = 0; x < xSize; x++)
        {
            cellArray[x] = new Cell[ySize];
        }
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                cellArray[x][y] = new Cell();
                cellArray[x][y].setContent(Cell.FREE);
            }
        }

        generate(mineCount);
    }

    public void generate(int mineCount)
    {
        assert ((xSize * ySize > mineCount) && (0 < mineCount));

        Random numberGenerator = new Random();

        for (int setMineCount = 0; setMineCount < mineCount; setMineCount++)
        {
            int y = numberGenerator.nextInt(ySize);
            int x = numberGenerator.nextInt(xSize);

            locateMine(x, y);
        }
    }

    private void locateMine(int startX, int startY)
    {
        assert (Cell.isInRange(startX, startY, xSize, ySize));

        int x = startX;
        int y = startY;

        while(Cell.MINE == cellArray[x][y].getContent())
        {
            x++;

            if (xSize == x)
            {
                x = 0;
                y++;

                if (ySize == y)
                {
                    y = 0;
                }
            }
        }

        cellArray[x][y].setContent(Cell.MINE);
    }

    private void relocateIfMine(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));

        if (Cell.FREE == cellArray[x][y].getContent())
        {
            return;
        }

        locateMine(x, y);
        cellArray[x][y].setContent(Cell.FREE);
    }

    public boolean isOpened(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));
        return cellArray[x][y].isOpened();
    }

    public boolean isChecked(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));
        return cellArray[x][y].isChecked();
    }

    public boolean isQuestioned(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));
        return cellArray[x][y].isQuestioned();
    }

    public int countNeighbourMines(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));

        int neighbourMineCount = 0;

        NeighbourSet neighbourSet = new NeighbourSet(x, y, xSize, ySize);
        for (Dimension cell : neighbourSet.toArray())
        {
            if (Cell.MINE == cellArray[cell.width][cell.height].getContent())
            {
                neighbourMineCount++;
            }
        }
        
        return neighbourMineCount;
    }

    public int countNeighbourFlags(int x, int y)
    {
        assert (Cell.isInRange(x, y, xSize, ySize));

        int neighbourFlagCount = 0;

        NeighbourSet neighbourSet = new NeighbourSet(x, y, xSize, ySize);
        for (Dimension cell : neighbourSet.toArray())
        {
            if (cellArray[cell.width][cell.height].isChecked())
            {
                neighbourFlagCount++;
            }
        }

        return neighbourFlagCount;
    }
    
    public void changeCheckStatus(int x, int y)
    {
        if (isOpened(x,y) || (xSize <= x) || (ySize <= y) || (0 > x) || (0 > y))
        {
            return;
        }

        if (isQuestioned(x,y))
        {
            cellArray[x][y].changeCheckStatus();
        }
        else if (isChecked(x,y))
        {
            cellArray[x][y].changeCheckStatus();
            flagCount--;
        }
        else if ((mineCount > flagCount) && (false == isChecked(x,y)))
        {
            cellArray[x][y].changeCheckStatus();
            flagCount++;
        }
    }

    public void changeOpenStatus(int xOpen, int yOpen) throws GameException
    {
        if ((false == Cell.isInRange(xOpen, yOpen, xSize, ySize)) || isChecked(xOpen, yOpen))
        {
            return;
        }
        else
        {
            Stack<Dimension> cellStack = new Stack<Dimension>();

            if (0 == openedCount)
            {
                relocateIfMine(xOpen, yOpen);
            }

            cellStack.push(new Dimension(xOpen, yOpen));

            while (false == cellStack.empty())
            {
                Dimension openingCell = cellStack.pop();
                int x = openingCell.width;
                int y = openingCell.height;

                if (isOpened(x, y))
                {
                    if ((0 == countNeighbourMines(x, y)) || (countNeighbourMines(x, y) == countNeighbourFlags(x, y)))
                    {
                        NeighbourSet neighbourSet = new NeighbourSet(x, y, xSize, ySize);
                        for (Dimension cell : neighbourSet.toArray())
                        {
                            if ((false == isChecked(cell.width, cell.height)) && (false == isOpened(cell.width, cell.height)))
                            {
                                cellArray[cell.width][cell.height].open();
                                openedCount++;

                                if (0 == countNeighbourMines(cell.width, cell.height))
                                {
                                    cellStack.push(new Dimension(cell.width, cell.height));
                                }
                            }
                        }
                    }
                }
                else
                {
                    cellArray[x][y].open();
                    openedCount++;

                    if (0 == countNeighbourMines(x, y))
                    {
                        cellStack.push(new Dimension(x, y));
                    }
                }
            }
        }

        if (mineCount + openedCount == xSize * ySize)
        {
            throw new GameException(GameException.GAME_WON);
        }
    }

    public int countUncheckedMines()
    {
        return (mineCount - flagCount);
    }

    public void reset()
    {
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                cellArray[x][y].setInitialState();
            }
        }

        generate(mineCount);
        openedCount = 0;
        flagCount = 0;
    }
}
