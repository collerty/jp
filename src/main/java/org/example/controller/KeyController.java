package org.example.controller;

import org.example.model.Presentation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class KeyController extends KeyAdapter
{
    private Presentation presentation; // Commands are given to the presentation

    public KeyController(Presentation p)
    {
        presentation = p;
    }

    public void keyPressed(KeyEvent keyEvent)
    {
        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_PAGE_DOWN:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_ENTER:
            case '+':
                presentation.nextSlide();
                break;
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_UP:
            case '-':
                presentation.prevSlide();
                break;
            case 'q':
            case 'Q':
                presentation.exit(0);
                break;
            case 'f':
            case 'F':
                presentation.enterFullscreen();
                break;
            case 'e':
            case 'E':
                presentation.editSlide();
                break;
            default:
                break;
        }
    }
}
