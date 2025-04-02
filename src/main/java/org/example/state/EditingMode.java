package org.example.state;

import org.example.Presentation;
import org.example.Slide;

import javax.swing.*;

public class EditingMode implements PresentationState
{
    @Override
    public void nextSlide(Presentation presentation)
    {

    }

    @Override
    public void prevSlide(Presentation presentation)
    {

    }

    @Override
    public void editSlide(Presentation presentation)
    {

    }

    @Override
    public void enterFullscreen(Presentation presentation)
    {

    }

    @Override
    public void exit(Presentation presentation, int statusCode)
    {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION)
        {
            System.exit(statusCode);
        }

    }


    @Override
    public void save(Presentation presentation)
    {

    }


    @Override
    public void clear(Presentation presentation)
    {

    }

    @Override
    public void addSlide(Presentation presentation, Slide slide)
    {

    }

}
