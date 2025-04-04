package org.example.model;

import org.example.view.SlideThumbnailPanel;
import org.example.view.SlideViewerComponent;
import org.example.view.SlideViewerFrame;
import org.example.state.PresentationState;
import org.example.state.ViewingMode;

import java.util.ArrayList;

public class Presentation
{
    private String showTitle; // title of the presentation
    private ArrayList<Slide> showList = null; // an ArrayList with Slides
    private int currentSlideNumber = 0; // the slidenummer of the current java.com.Slide
    private SlideViewerComponent slideViewComponent = null; // the viewcomponent of the Slides
    private PresentationState currentState;
    private SlideViewerFrame slideViewerFrame = null;
    private SlideThumbnailPanel thumbnailPanel = null;


    public Presentation()
    {
        this.slideViewComponent = null;
        this.currentState = new ViewingMode();
        clear();
    }

    public Presentation(SlideViewerComponent slideViewerComponent)
    {
        this.slideViewComponent = slideViewerComponent;
        this.currentState = new ViewingMode();
        clear();
    }

    public int getSize()
    {
        return showList.size();
    }

    public String getTitle()
    {
        return showTitle;
    }

    public void setTitle(String nt)
    {
        showTitle = nt;
    }

    public void setShowView(SlideViewerComponent slideViewerComponent)
    {
        this.slideViewComponent = slideViewerComponent;
    }

    public int getSlideNumber()
    {
        return currentSlideNumber;
    }

    public void setSlideNumber(int number)
    {
        currentSlideNumber = number;
        if (slideViewComponent != null)
        {
            slideViewComponent.update(this, getCurrentSlide());
        }
        if (slideViewerFrame != null && slideViewerFrame.getThumbnailPanel() != null)
        {
            slideViewerFrame.getThumbnailPanel().setSelectedIndex(number);
        }
    }

    public ArrayList<Slide> getShowList()
    {
        return this.showList;
    }

    public String getShowTitle()
    {
        return this.showTitle;
    }

    public void setShowTitle(String showTitle)
    {
        this.showTitle = showTitle;
    }

    public void setShowList(ArrayList<Slide> showList)
    {
        this.showList = showList;
    }

    public int getCurrentSlideNumber()
    {
        return this.currentSlideNumber;
    }

    public void setCurrentSlideNumber(int currentSlideNumber)
    {
        this.currentSlideNumber = currentSlideNumber;
    }

    public SlideViewerComponent getSlideViewComponent()
    {
        return this.slideViewComponent;
    }

    public void setSlideViewComponent(SlideViewerComponent slideViewComponent)
    {
        this.slideViewComponent = slideViewComponent;
    }

    public SlideThumbnailPanel getThumbnailPanel()
    {
        return this.thumbnailPanel;
    }

    public void setThumbnailPanel(SlideThumbnailPanel panel)
    {
        this.thumbnailPanel = panel;
    }

    public PresentationState getCurrentState()
    {
        return this.currentState;
    }

    public void setCurrentState(PresentationState currentState)
    {
        this.currentState = currentState;
    }

    public SlideViewerFrame getSlideViewerFrame()
    {
        return this.slideViewerFrame;
    }

    public void setSlideViewerFrame(SlideViewerFrame slideViewerFrame)
    {
        this.slideViewerFrame = slideViewerFrame;
    }

    // Get a slide with a certain slidenumber
    public Slide getSlide(int number)
    {
        if (number < 0 || number >= getSize())
        {
            return null;
        }
        return (Slide) showList.get(number);
    }

    // go to the next slide unless your at the end of the presentation.
    public void nextSlide()
    {
        this.currentState.nextSlide(this);
    }

    // go to the previous slide unless your at the beginning of the presentation
    public void prevSlide()
    {
        this.currentState.prevSlide(this);
    }

    // Delete the presentation to be ready for the next one.
    public void clear()
    {
        showList = new ArrayList<Slide>();
        currentSlideNumber = 0;
        showTitle = "";
        if (thumbnailPanel != null) {
            thumbnailPanel.removeAll();
            thumbnailPanel.revalidate();
            thumbnailPanel.repaint();
        }
        if (slideViewComponent != null) {
            slideViewComponent.update(this, null);
        }
        if (slideViewerFrame != null) {
            slideViewerFrame.repaint();
        }
    }


    // Add a slide to the presentation
    public void append(Slide slide)
    {
        this.currentState.addSlide(this, slide);
    }


    // Give the current slide
    public Slide getCurrentSlide()
    {
        return getSlide(currentSlideNumber);
    }

    public void exit(int statusCode)
    {
        this.currentState.exit(this, statusCode);
    }

    public void enterFullscreen()
    {
        this.currentState.enterFullscreen(this);
    }

    public void editSlide()
    {
        this.currentState.editSlide(this);
    }
}
