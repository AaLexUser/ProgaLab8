package com.lapin.common.commands.impl;

import com.lapin.common.controllers.*;
import com.lapin.common.exception.NullEnvException;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.di.annotation.Inject;
import com.lapin.network.AccessType;
import com.lapin.network.StatusCodes;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Команда сохраняет коллекцию в файл
 */

@ClassMeta(name = "save", description = "сохранить коллекцию в файл")
public class Save extends AbstractCommand {
    @Inject
    private FileManager fileManager;
    private CollectionManager collectionManager = Controllers.getCollectionManager();
    {
        super.accessType = AccessType.ADMIN;
        super.executingLocal = true;
    }

    @Override
    public void execute(RequestCommand rc) {
        try {
            String response = fileManager.saveCollection(collectionManager);
            OutResultStack.push(StatusCodes.OK,response);

        } catch (NullEnvException e) {
            OutResultStack.push(StatusCodes.ERROR, "Failed to find env. Try again.");
        } catch (FileNotFoundException e){
            OutResultStack.push(StatusCodes.ERROR, "Failed to find file");
        }
        catch (RuntimeException | IOException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }
}
