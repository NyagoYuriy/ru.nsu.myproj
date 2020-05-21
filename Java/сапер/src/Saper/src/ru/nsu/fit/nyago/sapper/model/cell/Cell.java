package ru.nsu.fit.nyago.sapper.model.cell;

import ru.nsu.fit.nyago.sapper.GameException.GameException;

public final class Cell
{
    public static final boolean MINE = true;
    public static final boolean FREE = false;

    private boolean content;
    private boolean opened;
    private boolean checked;
    private boolean questioned;

    public boolean getContent()
    {
        return content;
    }

    public void setContent(boolean newContent)
    {
        content = newContent;
    }

    public void setInitialState()
    {
        content = FREE;
        checked = false;
        opened = false;
        questioned = false;
    }

    public boolean isOpened()
    {
        return opened;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public boolean isQuestioned()
    {
        return questioned;
    }

    public Cell()
    {
        content = FREE;
    }

    public void open () throws GameException
    {
        if (MINE == content)
        {
            throw(new GameException(GameException.GAME_OVER));
        }
        else
        {
            changeOpenStatus();
        }
    }

    public void changeOpenStatus()
    {
        if (false == opened)
        {
            opened = true;
        }
    }

    public void changeCheckStatus()
    {
        assert (false == (checked && questioned));

        if (checked && (false == questioned))
        {
            checked = false;
            questioned = true;
        }
        else if (questioned && (false == checked))
        {
            questioned = false;
        }
        else
        {
            checked = true;
        }
    }

    public static boolean isInRange(int x, int y, int xSize, int ySize)
    {
        return((0 <= x) || (xSize < x) || (0 <= y) || (ySize < y));
    }
}