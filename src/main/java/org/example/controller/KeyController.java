package org.example.controller;

import org.example.model.Presentation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class KeyController extends KeyAdapter
{
    private final Presentation presentation; // Commands are given to the presentation

    public KeyController(Presentation p)
    {
        this.presentation = p;
    }

    public void keyPressed(KeyEvent keyEvent)
    {
        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_PAGE_DOWN:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_ENTER:
            case '+':
                this.presentation.nextSlide();
                break;
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_UP:
            case '-':
                this.presentation.prevSlide();
                break;
            case 'q':
            case 'Q':
                this.presentation.exit(0);
                break;
            case 'f':
            case 'F':
                this.presentation.enterFullscreen();
                break;
            case 'e':
            case 'E':
                this.presentation.editSlide();
                break;
            default:
                break;
        }
    }
}
