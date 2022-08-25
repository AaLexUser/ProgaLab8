package com.lapin.server.utility;


import com.lapin.common.controllers.*;
import com.lapin.common.data.Route;
import com.lapin.common.data.User;
import com.lapin.common.exception.IdOverflowException;
import com.lapin.di.annotation.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс управляет коллекцией
 */
public class JavaCollectionManager implements CollectionManager {
    /**
     * Дата последней инициализации коллекции.
     */
    private static LocalDate lastInitTime;
    /**
     * Дата последнего сохранения коллекции.
     */
    private static LocalDate saveTime;
    /**
     * Коллекция в которой хранятся {@link Route}
     */
    private static LinkedHashSet<Route> routeCollection = new LinkedHashSet<>();
    private static final ArrayList<String[]> stringRouteCollection = new ArrayList<>();
    private final DBHandler dbHandler = Controllers.getDbHandler();

    public ArrayList<String[]> getStringRouteCollection() {
        ArrayList<String[]> strRouteCol = new ArrayList<>();
        for(Route route : routeCollection){
            List<String> routeList = new ArrayList<>();
            routeList.add(((Integer) route.getId()).toString());
            routeList.add(route.getName());
            routeList.add(route.getCoordinates().getX().toString());
            routeList.add(((Double) route.getCoordinates().getY()).toString());
            routeList.add(route.getCreationDate().toString());
            routeList.add(route.getFrom().getX().toString());
            routeList.add(((Float) route.getFrom().getY()).toString());
            routeList.add(((Double) route.getFrom().getZ()).toString());
            routeList.add(route.getTo().getX().toString());
            routeList.add(route.getTo().getY().toString());
            routeList.add(route.getTo().getName());
            routeList.add(route.getDistance().toString());
            String[] str = {routeList.get(0), routeList.get(1), routeList.get(2), routeList.get(3), routeList.get(4),
                    routeList.get(5), routeList.get(6), routeList.get(7), routeList.get(8), routeList.get(9), routeList.get(10),
                    routeList.get(11)};
            strRouteCol.add(str);
        }
        return strRouteCol;
    }



    /**
     * Возвращает коллекцию из <b>Route</b>.
     *
     * @return коллекция из Route.
     */
    public Set<Route> getRouteCollection() {
        return routeCollection;
    }

    /**
     * Записывает последнее время сохранения коллекции.
     */
    public void saveTimeCollection() {
        saveTime = LocalDate.now();
    }

    @Override
    public void add(Route route, User user) throws RuntimeException{

        if (dbHandler.addRoute(route, user) == -1){
            throw new RuntimeException();
        }
        loadCollection();
    }

    @Override
    public boolean update(Integer id, Route newRoute, User user) throws RuntimeException {
        for (Route route : routeCollection) {
            if (route.getId() == id) {
                if(route.getAuthor().getId() == user.getId()){
                    if(dbHandler.updateRoute(id,newRoute,user) == -1){
                        throw new RuntimeException("The command ended with an error. Try again.");
                    }
                    loadCollection();
                    return true;
                }
                else throw new RuntimeException("No permission for update");
            }
        }
        return false;
    }
    @Override
    public long deleteRouteByID(int routeID, long authorID) throws RuntimeException{
        long res = dbHandler.deleteRouteByID(routeID, authorID);
        if (res == -1) {
            throw new RuntimeException("The command ended with an error. Try again.");
        }
        loadCollection();
        return res;
    }
    /**
     * Возвращает последнюю дату сохранения коллекции.
     *
     * @return Последнюю дату сохранения.
     */
    public LocalDate getSaveTimeCollection() {
        return saveTime;
    }

    /**
     * Возвращает последнюю дату инициализации коллекции.
     *
     * @return последнюю дату инициализации.
     */
    public LocalDate getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Загружает коллекцию из файла
     */
    public void loadCollection() {
        lastInitTime = LocalDate.now();
        routeCollection.clear();
        routeCollection.addAll(dbHandler.loadRoutes());
    }

    /**
     * Очищает коллекцию
     */
    public long clear(long authorId) throws RuntimeException{
        long res = dbHandler.deleteRoutesByAuthor(authorId);
        if (res == -1){
            throw new RuntimeException("The command ended with an error. Try again.");
        }
        loadCollection();
        return res;
    }
}
