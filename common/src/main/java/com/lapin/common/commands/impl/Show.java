package com.lapin.common.commands.impl;


import com.lapin.common.controllers.Controllers;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.network.AccessType;
import com.lapin.common.controllers.CollectionManager;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.data.Route;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.network.StatusCodes;


import java.util.Set;


/**
 * Команда выводит в консоль все элементы коллекции
 */
@ClassMeta(
        name = "show",
        description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении")
public class Show extends AbstractCommand {
    private CollectionManager collectionManager = Controllers.getCollectionManager();
    {
        super.accessType = AccessType.ALL;
    }


    @Override
    public void execute(RequestCommand rc) {
        try {
            Set<Route> response;
            if (collectionManager.getRouteCollection().size() == 0) {
                response = null;
            }
            else response = collectionManager
                    .getRouteCollection();
            OutResultStack.push(StatusCodes.OK,response);

        } catch (RuntimeException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }
}
