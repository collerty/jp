package org.example.filehandler;

import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.BitmapItem;
import org.example.model.Presentation;
import org.example.model.Slide;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.factory.AbstractSlideItemFactory;
import org.example.model.slideComponents.factory.BitmapItemFactory;
import org.example.model.slideComponents.factory.TextItemFactory;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XMLAccessor extends Accessor
{
    protected static final String SHOW_TITLE = "showtitle";
    protected static final String SLIDE_TITLE = "title";
    protected static final String SLIDE = "slide";
    protected static final String ITEM = "item";
    protected static final String LEVEL = "level";
    protected static final String KIND = "kind";
    protected static final String TEXT = "text";
    protected static final String IMAGE = "image";
    private final AbstractSlideItemFactory bitmapItemFactory = new BitmapItemFactory();
    private final AbstractSlideItemFactory textItemFactory = new TextItemFactory();


    /**
     * tekst van messages
     */
    protected static final String PCE = "Parser Configuration Exception";
    protected static final String UNKNOWN_TYPE = "Unknown Element type";
    protected static final String NFE = "Number Format Exception";


    private String getTitle(Element element, String tagName)
    {
        NodeList titles = element.getElementsByTagName(tagName);
        return titles.item(0).getTextContent();

    }

    public void loadFile(Presentation presentation, String filename) throws IOException
    {
        int slideNumber, itemNumber, max = 0, maxItems = 0;
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(filename)); // Create a JDOM document
            Element doc = document.getDocumentElement();
            presentation.setTitle(this.getTitle(doc, SHOW_TITLE));

            NodeList slides = doc.getElementsByTagName(SLIDE);
            max = slides.getLength();
            for (slideNumber = 0; slideNumber < max; slideNumber++)
            {
                Element xmlSlide = (Element) slides.item(slideNumber);
                Slide slide = new Slide();
                slide.setTitle(this.getTitle(xmlSlide, SLIDE_TITLE));
                presentation.append(slide);

                NodeList slideItems = xmlSlide.getElementsByTagName(ITEM);
                maxItems = slideItems.getLength();
                for (itemNumber = 0; itemNumber < maxItems; itemNumber++)
                {
                    Element item = (Element) slideItems.item(itemNumber);
                    this.loadSlideItem(slide, item);
                }
            }
        } catch (IOException iox)
        {
            System.err.println(iox.toString());
        } catch (SAXException sax)
        {
            System.err.println(sax.getMessage());
        } catch (ParserConfigurationException pcx)
        {
            System.err.println(PCE);
        }
    }

    protected void loadSlideItem(Slide slide, Element item)
    {
        int level = 1; // default
        NamedNodeMap attributes = item.getAttributes();
        String levelText = attributes.getNamedItem(LEVEL).getTextContent();
        if (levelText != null)
        {
            try
            {
                level = Integer.parseInt(levelText);
            } catch (NumberFormatException x)
            {
                System.err.println(NFE);
            }
        }
        String type = attributes.getNamedItem(KIND).getTextContent();
        if (TEXT.equals(type))
        {

            slide.append(this.textItemFactory.createItem(level, item.getTextContent()));
        }
        else
        {
            if (IMAGE.equals(type))
            {
                slide.append(this.bitmapItemFactory.createItem(level, item.getTextContent()));
            }
            else
            {
                System.err.println(UNKNOWN_TYPE);
            }
        }
    }

    public void saveFile(Presentation presentation, String filename) throws IOException
    {
        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.println("<?xml version=\"1.0\"?>");
        out.println("<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">");
        out.println("<presentation>");
        out.print("<showtitle>");
        out.print(presentation.getTitle());
        out.println("</showtitle>");
        for (int slideNumber = 0; slideNumber < presentation.getSize(); slideNumber++)
        {
            Slide slide = presentation.getSlide(slideNumber);
            out.println("<slide>");
            out.println("<title>" + slide.getTitle() + "</title>");
            Vector<SlideItem> slideItems = slide.getSlideItems();
            for (int itemNumber = 0; itemNumber < slideItems.size(); itemNumber++)
            {
                SlideItem slideItem = (SlideItem) slideItems.elementAt(itemNumber);
                out.print("<item kind=");
                if (slideItem instanceof TextItem)
                {
                    out.print("\"text\" level=\"" + slideItem.getLevel() + "\">");
                    out.print(((TextItem) slideItem).getText());
                }
                else
                {
                    if (slideItem instanceof BitmapItem)
                    {
                        out.print("\"image\" level=\"" + slideItem.getLevel() + "\">");
                        out.print(((BitmapItem) slideItem).getName());
                    }
                    else
                    {
                        System.out.println("Ignoring " + slideItem);
                    }
                }
                out.println("</item>");
            }
            out.println("</slide>");
        }
        out.println("</presentation>");
        out.close();
    }
}
