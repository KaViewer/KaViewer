package com.koy.kaviewer.shell.command;

import com.koy.kaviewer.shell.common.DefaultValues;
import com.koy.kaviewer.shell.core.KaViewerCommandHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup("Built-In Commands")
public class KaViewerCommand {

    @Autowired
    private KaViewerCommandHistory kaViewerCommandHistory;

    @ShellMethod(value = "show history command", key = {"history", "his"})
    public void history(@ShellOption(defaultValue = DefaultValues.DEFAULT_NONE) String token) {
        kaViewerCommandHistory.showHis(token);
    }


}
