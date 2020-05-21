package ru.nsu.fit.nyago.sapper.model.field;

import ru.nsu.fit.nyago.sapper.model.cell.Cell;

import java.awt.*;
import java.util.HashSet;

final class NeighbourSet
{
    private static final int NEIGHBOUR_RADIUS = 1;
    private HashSet <Dimension> neighbourSet;

    public NeighbourSet(int xCell, int yCell, int xSize, int ySize)
    {
        assert (Cell.isInRange(xCell, yCell, xSize, ySize));

        neighbourSet = new HashSet <Dimension> ();

        for (int x = xCell - NEIGHBOUR_RADIUS; x <= xCell + NEIGHBOUR_RADIUS; x++)
        {
            if ((0 > x) || (x >= xSize))
            {
                continue;
            }

            for (int y = yCell - NEIGHBOUR_RADIUS; y <= yCell + NEIGHBOUR_RADIUS; y++)
            {
                if ((0 > y) || (y >= ySize) || ((xCell == x) && (yCell == y)))
                {
                    continue;
                }

                neighbourSet.add(new Dimension(x, y));
            }
        }
    }

    public Dimension[] toArray()
    {
        Dimension [] neighbourArray = new Dimension [neighbourSet.size()];
        neighbourSet.toArray(neighbourArray);
        return neighbourArray;
    }
}
