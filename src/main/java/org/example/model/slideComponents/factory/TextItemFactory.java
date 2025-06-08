package org.example.model.slideComponents.factory;

import org.example.model.slideComponents.SlideItem;
import org.example.model.slideComponents.TextItem;
import org.example.model.slideComponents.util.SlideItemDecoratorUtil;
import org.example.model.slideComponents.util.TextDecoratorType;
import org.example.model.slideComponents.factory.AbstractSlideItemFactory;

import java.util.ArrayList;
import java.util.List;

public class TextItemFactory implements AbstractSlideItemFactory
{

    @Override
    public SlideItem createItem(int level, String content) {
        return new TextItem(level, content);
    }

    public SlideItem createFormattedItem(int level, String content, boolean bold, boolean italic) {
        List<TextDecoratorType> decorators = new ArrayList<>();
        if (bold) decorators.add(TextDecoratorType.BOLD);
        if (italic) decorators.add(TextDecoratorType.ITALIC);
        return SlideItemDecoratorUtil.applyDecorators(new TextItem(level, content), decorators);
    }
}
