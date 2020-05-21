package ru.nsu.fit.nyago.sapper.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public final class GameTimer
{
    private static final int MAX_SECOND = 60;
    private static final int MAX_MINUTE = 100;
    private static final int MAX_MILLISECOND = 1000;

    private static Timer timer;
    private static JLabel timeLabel;
    private static int currentSecond = 0;
    private static int currentMinute = 0;

    public void setTimeLabel(JLabel gameTimeLabel)
    {
        assert(null != gameTimeLabel);

        timer = new Timer(MAX_MILLISECOND, new ChangeTimeAction(gameTimeLabel));
        timeLabel = gameTimeLabel;
    }

    public void start()
    {
        reset();
        timer.start();
    }

    public boolean isRunning()
    {
        return timer.isRunning();
    }

    public void stop()
    {
        timer.stop();
    }

    public void reset()
    {
        currentMinute = 0;
        currentSecond = 0;
        timeLabel.setText(getStringTime());
    }

    public String getStringTime()
    {
        return currentMinute + ":" + currentSecond;
    }

    public int getTime()
    {
        return currentMinute * MAX_SECOND + currentSecond;
    }

    public static int getCurrentMinute()
    {
        return currentMinute;
    }

    public static int getCurrentSecond()
    {
        return currentSecond;
    }

    private class ChangeTimeAction implements ActionListener
    {
        JLabel timeLabel;

        private ChangeTimeAction(JLabel timeLabel)
        {
            this.timeLabel = timeLabel;
        }

        public void actionPerformed(ActionEvent event)
        {
            currentSecond++;
            if (MAX_SECOND == currentSecond)
            {
                currentMinute++;
                currentSecond = 0;

                if (MAX_MINUTE == currentMinute)
                {
                    stop();
                }
            }

            timeLabel.setText(getStringTime());
        }
    }
}
