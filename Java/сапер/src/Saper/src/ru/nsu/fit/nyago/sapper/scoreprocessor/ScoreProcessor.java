package ru.nsu.fit.nyago.sapper.scoreprocessor;

import java.io.*;
import java.util.*;

public final class ScoreProcessor
{
    private static final String [] SCORE_FILE = {   "resource\\data\\noviceScores",
                                                    "resource\\data\\mediumScores",
                                                    "resource\\data\\expertScores"};
    private static final int SCORE_COUNT = 10;
    public static final int SECOND_COUNT = 60;

    public static void addScore(int minutes, int seconds, String userName, int gameType)
    {
        int time = minutes * SECOND_COUNT + seconds;
        insertScore(time, userName, gameType);
    }

    public static boolean isScore(int time, int gameType)
    {
        final int LAST_SCORE_NUMBER = SCORE_COUNT - 1;

        List <Score> scoreList = getScoreArray(gameType);
        return (scoreList.size() < SCORE_COUNT) || (time < scoreList.get(LAST_SCORE_NUMBER).getTime());
    }

    public static void insertScore(int time, String userName, int gameType)
    {
        List <Score> scoreList = getScoreArray(gameType);

        scoreList.add(new Score(userName, time));
        Collections.sort(scoreList);
        save (scoreList, gameType);
    }

    public static List <Score> getScoreArray(int gameType)
    {
        List <Score> scoreList = new ArrayList<Score>();
        Reader reader = null;

        try
        {
            reader = new InputStreamReader(new FileInputStream(SCORE_FILE[gameType]));

            for (int scoreNumber = 0; reader.ready() && (scoreNumber < SCORE_COUNT); scoreNumber++)
            {
                StringBuilder name = new StringBuilder();
                StringBuilder time = new StringBuilder();

                for(;reader.ready();)
                {
                    char readingChar = (char)reader.read();

                    if (' ' == readingChar)
                    {
                        break;
                    }

                    name.append(readingChar);
                }

                for(;reader.ready();)
                {
                    char readingChar = (char)reader.read();

                    if (false == Character.isDigit(readingChar))
                    {
                        break;
                    }

                    time.append(readingChar);
                }

                if (false == ((name.toString().equals("")) || (time.toString().equals(""))))
                {
                    Score newScore = new Score(name.toString(), Integer.parseInt(time.toString()));
                    scoreList.add(newScore);
                }
            }
        }
        catch (IOException catchValue)
        {
            System.err.println("Error while reading file: " + catchValue.getLocalizedMessage());
        }
        finally
        {
            if (null != reader)
            {
                try
                {
                    reader.close();
                }
                catch (IOException catchValue)
                {
                    catchValue.printStackTrace(System.err);
                }
            }
        }

        return scoreList;
    }

    public static String getTextScores (int gameType)
    {
        String textScore = "<html>";
         List <Score> scoreList = getScoreArray(gameType);
        for (Score adding : scoreList)
        {
            textScore = textScore + adding.toPrintableString() + "<p>";
        }
        for (int i = scoreList.size(); i < SCORE_COUNT; i++)
        {
            textScore = textScore + "no name 0:00<p>";
        }

        textScore += "</html>";
        return textScore;
    }

    public static void save(List <Score> scoreList, int gameType)
    {
        PrintWriter printWriter = null;

        try
        {
            printWriter = new PrintWriter(SCORE_FILE[gameType]);

            for (int index = 0; (index < SCORE_COUNT) && (index < scoreList.size()); index++)
            {
                printWriter.println(scoreList.get(index).toString());
            }
        }
        catch (FileNotFoundException catchValue)
        {
            System.err.println("Error while writing file: " + catchValue.getLocalizedMessage());
        }
        finally
        {
            if (null != printWriter)
            {
                printWriter.close();
            }
        }
    }
}
