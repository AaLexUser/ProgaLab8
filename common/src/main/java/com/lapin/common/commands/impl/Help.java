package com.lapin.common.commands.impl;


import com.lapin.common.commands.CheckAccess;
import com.lapin.common.controllers.CommandManager;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.common.commands.Command;
import com.lapin.di.context.AbstractBean;
import com.lapin.network.AccessType;
import com.lapin.di.context.ApplicationContext;
import com.lapin.network.StatusCodes;
import org.reflections.Reflections;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Команда, выводящая справку по всем доступным командам
 */
@ClassMeta(
        name = "help",
        description = "вывести справку по доступным командам")
public class Help extends AbstractCommand {
    private CommandManager commandManager = ApplicationContext.getInstance().getBean(CommandManager.class);
    {
        super.accessType = AccessType.ALL;
        super.executingLocal =true;
    }
    @Override
    public void execute(RequestCommand rc) {
        try {
            Reflections scanner = new Reflections("");
            Set<Class<? extends AbstractCommand>> implementationClasses = scanner.getSubTypesOf(AbstractCommand.class);
            String response = implementationClasses
                    .stream()
                    .filter(clazz -> {
                        Object obj = null;
                        obj = ApplicationContext.getInstance().getBean(new AbstractBean<>(clazz),true,false);
                        Command command = (Command) (obj instanceof Command ? obj : null);
                        try {
                            Field accessTypeField = clazz.getSuperclass().getDeclaredField("accessType");
                            accessTypeField.setAccessible(true);
                            Field hideField = clazz.getSuperclass().getDeclaredField("hide");
                            hideField.setAccessible(true);
                            boolean flag = CheckAccess
                                    .check(
                                            commandManager.getClient().getClientType(),
                                            (AccessType) accessTypeField.get(command)
                                    ) && !((Boolean) hideField.get(command));
                            return flag;
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            return false;
                        }
                    })
                    .map(clazz -> clazz.getAnnotation(ClassMeta.class))
                    .filter(Objects::nonNull)
                    .map(an -> an.name() + " – " + an.description())
                    .sorted()
                    .collect(Collectors.joining("\n"));
            OutResultStack.push(StatusCodes.OK, response);

        } catch (RuntimeException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }
}
