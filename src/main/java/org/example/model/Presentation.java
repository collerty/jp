package org.example.model;

import org.example.view.SlideThumbnailPanel;
import org.example.view.SlideViewerComponent;
import org.example.view.SlideViewerFrame;
import org.example.state.PresentationState;
import org.example.state.ViewingMode;

import java.util.ArrayList;

public class Presentation
{
    private static final String JABTITLE = "New jabbepoint presentation";
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
        this.clear();
    }

    public Presentation(SlideViewerComponent slideViewerComponent)
    {
        this.slideViewComponent = slideViewerComponent;
        this.currentState = new ViewingMode();
        this.clear();
    }

    public int getSize()
    {
        return this.showList.size();
    }

    public String getTitle()
    {
        return this.showTitle;
    }

    public void setTitle(String nt)
    {
        this.showTitle = nt;
    }

    public void setShowView(SlideViewerComponent slideViewerComponent)
    {
        this.slideViewComponent = slideViewerComponent;
    }

    public int getSlideNumber()
    {
        return this.currentSlideNumber;
    }

    public void setSlideNumber(int number)
    {
        this.currentSlideNumber = number;
        if (this.slideViewComponent != null)
        {
            this.slideViewComponent.update(this, this.getCurrentSlide());
        }
        if (this.slideViewerFrame != null && this.slideViewerFrame.getThumbnailPanel() != null)
        {
            this.slideViewerFrame.getThumbnailPanel().setSelectedIndex(number);
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
        if (number < 0 || number >= this.getSize())
        {
            return null;
        }
        return (Slide) this.showList.get(number);
    }

    public void nextSlide()
    {
        this.currentState.nextSlide(this);
    }

    public void prevSlide()
    {
        this.currentState.prevSlide(this);
    }

    public void clear()
    {
        // Clear data
        this.showList = new ArrayList<Slide>();
        this.currentSlideNumber = 0;
        this.showTitle = JABTITLE;  // Set default title instead of empty string

        // Reset state
        this.currentState = new ViewingMode();

        this.setShowList(new ArrayList<Slide>());
        this.setCurrentSlideNumber(0);
        this.setTitle("New jabbepoint presentation");
        if (this.getSlideViewComponent() != null)
        {
            this.getSlideViewComponent().update(this, null);
        }

        // Remove header panel if in view mode
        if (this.slideViewerFrame != null)
        {
            this.slideViewerFrame.exitEditMode();
        }

        this.updateUI();

        if (this.thumbnailPanel != null)
        {
            this.thumbnailPanel.updateThumbnails();
        }
    }

    private void updateUI()
    {
        if (this.thumbnailPanel != null)
        {
            this.thumbnailPanel.updateThumbnails();
        }

        if (this.slideViewComponent != null)
        {
            slideViewComponent.update(this, null);
        }

        if (this.slideViewerFrame != null)
        {
            this.slideViewerFrame.setTitle(JABTITLE);
            this.slideViewerFrame.revalidate();
            this.slideViewerFrame.repaint();
        }
    }


    public void append(Slide slide)
    {
        String currentTitle = this.showTitle;

        this.currentState.addSlide(this, slide);

        if (this.showTitle.isEmpty())
        {
            this.showTitle = currentTitle;
        }

        this.updateUI();
    }


    public Slide getCurrentSlide()
    {
        return this.getSlide(this.currentSlideNumber);
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
