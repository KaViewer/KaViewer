package com.koy.kaviewer.core;

import com.koy.kaviewer.core.annotation.GroupCommandRegistrar;
import org.jline.reader.LineReader;
import org.jline.reader.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Order(InteractiveShellApplicationRunner.PRECEDENCE - 1000)
@Component
public class KaViewerInteractiveShellApplicationRunner extends InteractiveShellApplicationRunner {

    private final LineReader lineReader;

    private final PromptProvider promptProvider;

    private final Parser parser;

    private final Shell shell;

    @Autowired
    private GroupCommandRegistrar groupCommandRegistrar;


    public KaViewerInteractiveShellApplicationRunner(LineReader lineReader, PromptProvider promptProvider, Parser parser, Shell shell, Environment environment) {
        super(lineReader, promptProvider, parser, shell, environment);
        this.lineReader = lineReader;
        this.promptProvider = promptProvider;
        this.parser = parser;
        this.shell = shell;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LineReaderWrapper lineReader = LineReaderWrapper.getInstance();
        lineReader.setDelegate(this.lineReader);
        lineReader.setGroupCommandRegistrar(this.groupCommandRegistrar);

        InputProvider inputProvider = new InteractiveShellApplicationRunner.JLineInputProvider(lineReader, promptProvider);
        shell.run(inputProvider);
    }


    // should let it be true to handle the error instead of shutting fu*k down at
    // org.springframework.shell.result.ThrowableResultHandler.doHandleResult
    @Override
    public boolean isEnabled() {
        return true;
    }


}
