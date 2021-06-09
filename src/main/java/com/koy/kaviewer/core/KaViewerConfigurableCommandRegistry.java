package com.koy.kaviewer.core;

import com.koy.kaviewer.core.annotation.KaViewerShellGroupCommand;
import org.springframework.shell.ConfigurableCommandRegistry;
import org.springframework.shell.MethodTarget;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class KaViewerConfigurableCommandRegistry extends ConfigurableCommandRegistry {

    private final Map<String, MethodTarget> commands = new HashMap<>();

    @Override
    public Map<String, MethodTarget> listCommands() {
        return new TreeMap<>(commands);
    }

    @Override
    public void register(String name, MethodTarget target) {
        KaViewerShellGroupCommand ann = target.getBean().getClass().getAnnotation(KaViewerShellGroupCommand.class);
        String newNameWithGroup = Objects.isNull(ann) ? name : ann.value() + "#" + name;
        MethodTarget previous = commands.get(newNameWithGroup);
        if (previous != null) {
            throw new IllegalArgumentException(
                    String.format("Illegal registration for command '%s': Attempt to register in same group '%s' both '%s' and '%s'", newNameWithGroup, name, target, previous));
        }
        commands.put(newNameWithGroup, target);
    }
}
