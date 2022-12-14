package com.lapin.common.commands.impl;


import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.network.AccessType;
import com.lapin.network.StatusCodes;

import java.io.Serializable;

/**
 * Команда для выхода из консольного приложения
 */
@ClassMeta(
        name = "exit",
        description = "завершить клиентскую программу (без сохранения в файл)")
public class Exit extends AbstractCommand {
    {
        super.accessType = AccessType.USER;
        super.executingLocal = true;
    }


    @Override
    public void execute(RequestCommand rc) {
        try {
            OutResultStack.push(StatusCodes.EXIT_CLIENT, "Client exit...");
            //System.exit(0);

        } catch (RuntimeException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }

}
