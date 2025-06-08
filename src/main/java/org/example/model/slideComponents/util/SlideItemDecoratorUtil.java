package org.example.model.slideComponents.util;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.decorator.BoldDecorator;
import org.example.model.slideComponents.decorator.ItalicDecorator;
import java.util.List;

public class SlideItemDecoratorUtil {

    public static SlideItem applyDecorators(SlideItem item, List<TextDecoratorType> decorators) {
        for (TextDecoratorType decorator : decorators) {
            switch (decorator) {
                case BOLD -> item = new BoldDecorator(item);
                case ITALIC -> item = new ItalicDecorator(item);
            }
        }
        return item;
    }
}
