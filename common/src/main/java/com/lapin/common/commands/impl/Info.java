package com.lapin.common.commands.impl;


import com.lapin.common.controllers.CollectionManager;
import com.lapin.common.controllers.Controllers;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.common.utility.OutResultStack;
import com.lapin.di.annotation.ClassMeta;
import com.lapin.common.commands.AbstractCommand;
import com.lapin.network.AccessType;
import com.lapin.network.StatusCodes;

import java.io.Serializable;


/**
 * Команда выводит основную информацию о коллекции
 */
@ClassMeta(name = "info", description = "вывести в стандартный поток вывода информацию о коллекции тип, дата инициализации, количество элементов и т.д.")
public class Info extends AbstractCommand {
    private CollectionManager collectionManager = Controllers.getCollectionManager();
    {
        super.accessType = AccessType.ALL;
    }

    @Override
    public void execute(RequestCommand rc) {

        try {
            String response = "";
            response += "Сведения о коллекции:\n";
            response += "Тип: " + collectionManager.getRouteCollection().getClass().getName()+"\n";
            response += "Дата инициализации: " + collectionManager.getLastInitTime()+"\n";
            String saveTime = "";
            if (collectionManager.getSaveTimeCollection() == null) {
                saveTime = "Коллекция не сохранялась в этой сессии";
            } else saveTime = collectionManager.getSaveTimeCollection().toString();
            response += "Дата сохранения: " + saveTime+"\n";
            response += "Количество элементов: " + collectionManager.getRouteCollection().size();
            OutResultStack.push(StatusCodes.OK,response);

        } catch (RuntimeException e) {
            OutResultStack.push(StatusCodes.ERROR, "The command ended with an error. Try again.");
        }
    }
}
