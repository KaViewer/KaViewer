package com.koy.kaviewer.ui.prompt;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class KaViewerPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("KaViewer:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
    }

}
