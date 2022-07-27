package com.lapin.common.commands.impl;


import com.lapin.common.controllers.Controllers;
import com.lapin.common.data.Route;
import com.lapin.common.controllers.CollectionManager;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.network.AccessType;
import com.lapin.network.StatusCodes;



import java.io.Serializable;

/**
 * Команда выводит минимальный по distance элемент Route
 */
@ClassMeta(name = "min_by_distance", description = "вывести любой объект из коллекции, значение поля distance которого является минимальным")
public class MinByDistance extends AbstractCommand {
    private CollectionManager collectionManager = Controllers.getCollectionManager();;
    {
        super.accessType = AccessType.ALL;
    }


    @Override
    public void execute(RequestCommand rc) {
        try {
            String response = "";
            if (collectionManager.getRouteCollection().size() == 0) {
                response +="Коллекция пуста.";
            }
            else response = collectionManager
                    .getRouteCollection()
                    .stream()
                    .sorted(new Route.ComparatorByDistance())
                    .toList()
                    .get(0)
                    .toString();
            OutResultStack.push(StatusCodes.OK,response);

        } catch (RuntimeException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }
}
