package ru.nsu.fit.nyago.sapper.scoreprocessor;

public final class Score implements Comparable <Score>
{
    private String name;
    private int time;

    public Score (String initialName, int initialCount)
    {
        name = initialName;
        time = initialCount;
    }

    public int compareTo(Score comparing)
    {
        return (time - comparing.time);
    }

    public String toString()
    {
        return (name + " " + time);
    }

    public String toPrintableString()
    {
        return (name + " " + time / ScoreProcessor.SECOND_COUNT + ":" + time % ScoreProcessor.SECOND_COUNT + "\n");
    }

    public int getTime()
    {
        return time;
    }
}
