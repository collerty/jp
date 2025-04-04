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
        // Clear data
        showList = new ArrayList<Slide>();
        currentSlideNumber = 0;
        showTitle = JABTITLE;  // Set default title instead of empty string

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
        if (slideViewerFrame != null)
        {
            slideViewerFrame.exitEditMode();
        }

//         Reset UI components
        updateUI();

//         Update thumbnail panel
        if (this.thumbnailPanel != null)
        {
            this.thumbnailPanel.updateThumbnails();
        }
    }

    // Helper method to update all UI components
    private void updateUI()
    {
        // Update thumbnail panel
        if (thumbnailPanel != null)
        {
            thumbnailPanel.updateThumbnails();
        }

        // Update slide viewer
        if (slideViewComponent != null)
        {
            slideViewComponent.update(this, null);
        }

        // Update frame
        if (slideViewerFrame != null)
        {
            slideViewerFrame.setTitle(JABTITLE);
            slideViewerFrame.revalidate();
            slideViewerFrame.repaint();
        }
    }


    // Add a slide to the presentation
    public void append(Slide slide)
    {
        // Store current title
        String currentTitle = this.showTitle;

        // Add slide using state pattern
        this.currentState.addSlide(this, slide);

        // Restore title if it was changed
        if (this.showTitle.isEmpty())
        {
            this.showTitle = currentTitle;
        }

        // Update UI after adding slide
        updateUI();
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
