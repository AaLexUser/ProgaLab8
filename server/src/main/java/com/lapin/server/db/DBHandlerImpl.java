package com.lapin.server.db;

import com.lapin.common.data.*;
import com.lapin.common.controllers.DBHandler;
import com.lapin.common.encrypt.Encryptor;
import com.lapin.di.context.ApplicationContext;
import com.lapin.network.log.NetworkLogOutputConsole;
import com.lapin.network.log.NetworkLogger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DBHandlerImpl implements DBHandler {
    private final DBConnector dbConnector;
    private final Encryptor encryptor;
    private final NetworkLogger Logger;

    public DBHandlerImpl(DBConnector dbConnector, Encryptor encryptor){
        this.dbConnector = dbConnector;
        this.encryptor = encryptor;
        Logger = ApplicationContext.getInstance().getBean(NetworkLogger.class);
        Logger.setLogOutput(new NetworkLogOutputConsole());
    }

    public synchronized long addUser(User user) {
        if(checkUsername(user) == -1){
            return -1;
        }
        else if (!(checkUsername(user) == 0)){
            return 0;
        }
        try (Connection connection = dbConnector.connect();
             PreparedStatement addUserToTable = connection.prepareStatement(DBQuery.INSERT_USER.getQuery()))
        {
            addUserToTable.setString(1, user.getUsername());
            addUserToTable.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = addUserToTable.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("id");
            user.setId(userId);
            Logger.info("New user added to database with id - " + userId);
            return userId;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }

    public synchronized long checkUsername(User user){
        try (Connection connection = dbConnector.connect();
             PreparedStatement checkUsername = connection.prepareStatement(DBQuery.SELECT_USER_BY_USERNAME.getQuery())
        ) {
            checkUsername.setString(1, user.getUsername());
            ResultSet resultSet = checkUsername.executeQuery();
            if (resultSet.next()) {
                long userId = resultSet.getLong("id");
                Logger.info("User with id - " + userId + " found");
                return userId;
            }
            return 0;
        }
        catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }
    public synchronized int addRoute(Route route, User user){
        try (Connection connection = dbConnector.connect();
             PreparedStatement addRouteToTable = connection.prepareStatement(DBQuery.INSERT_ROUTE.getQuery())) {
            int paramCounter = 1;
            addRouteToTable.setDouble(1,route.getCoordinates().getX());
            addRouteToTable.setDouble(2,route.getCoordinates().getY());
            addRouteToTable.setInt(3,route.getFrom().getX());
            addRouteToTable.setDouble(4,route.getFrom().getY());
            addRouteToTable.setDouble(5,route.getFrom().getZ());
            addRouteToTable.setDouble(6, route.getTo().getX());
            addRouteToTable.setLong(7, route.getTo().getY());
            addRouteToTable.setString(8, route.getTo().getName());
            addRouteToTable.setString(9, route.getName());
            addRouteToTable.setDate(10, Date.valueOf(route.getCreationDate()));
            addRouteToTable.setLong(11, route.getDistance());
            addRouteToTable.setLong(12, user.getId());
            ResultSet resultSet = addRouteToTable.executeQuery();
            resultSet.next();
            int routeId = resultSet.getInt("id");
            Logger.info("New route added to database with id - " + routeId);
            return routeId;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }

    public synchronized long checkUser(User user) {
        try (Connection connection = dbConnector.connect();
             PreparedStatement checkUser = connection.prepareStatement(DBQuery.SELECT_USER_BY_USERNAME_AND_PASSWORD.getQuery());
        ) {
            checkUser.setString(1, user.getUsername());
            checkUser.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = checkUser.executeQuery();
            if (resultSet.next()) {
                long userId = resultSet.getLong("id");
                Logger.info("User with id - " + userId + " found");
                return userId;
            }
            return 0;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }
    @Override
    public synchronized Set<Route> loadRoutes(){
        Set<Route> routes = ConcurrentHashMap.newKeySet();
        try(
                Connection connection = dbConnector.connect();
                PreparedStatement selectAllRoutes = connection.prepareStatement(DBQuery.SELECT_ALL_ROUTES.getQuery());
                ){
            ResultSet resultSet = selectAllRoutes.executeQuery();
            while (resultSet.next()){
                Route route = new Route(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new Coordinates(
                                resultSet.getDouble("coord_x"),
                                resultSet.getDouble("coord_y")
                        ),
                        resultSet.getDate("creation_date").toLocalDate(),
                        new LocationFrom(
                                resultSet.getInt("loc_from_x"),
                                resultSet.getFloat("loc_from_y"),
                                resultSet.getDouble("loc_from_z")
                        ),
                        new LocationTo(
                                resultSet.getFloat("loc_to_x"),
                                resultSet.getLong("loc_to_y"),
                                resultSet.getString("loc_to_name")
                        ),
                        resultSet.getLong("distance")
                        );
                route.setAuthor(new User(resultSet.getLong("author_id"), resultSet.getString("username")));
                routes.add(route);
            }
            return routes;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
        }
        return routes;
    }
    public synchronized long updateRoute(Integer id,Route route, User user){
        try (Connection connection = dbConnector.connect();
             PreparedStatement updateRoute = connection.prepareStatement(DBQuery.UPDATE_ROUTE.getQuery())) {
            updateRoute.setDouble(1,route.getCoordinates().getX());
            updateRoute.setDouble(2,route.getCoordinates().getY());
            updateRoute.setInt(3,route.getFrom().getX());
            updateRoute.setDouble(4,route.getFrom().getY());
            updateRoute.setDouble(5,route.getFrom().getZ());
            updateRoute.setDouble(6, route.getTo().getX());
            updateRoute.setLong(7, route.getTo().getY());
            updateRoute.setString(8, route.getTo().getName());
            updateRoute.setString(9, route.getName());
            updateRoute.setLong(10, route.getDistance());
            updateRoute.setInt(11,id);
            updateRoute.setLong(12, user.getId());
            ResultSet resultSet = updateRoute.executeQuery();
            resultSet.next();
            int routeId = resultSet.getInt("id");
            Logger.info("Route updated to database with id - " + routeId);
            return routeId;
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }
    public synchronized long deleteRouteByID(int routeID, long authorID){
        try (Connection connection = dbConnector.connect();
             PreparedStatement deleteRouteByID = connection.prepareStatement(DBQuery.DELETE_ROUTE_BY_ID.getQuery())
        ){
            deleteRouteByID.setInt(1,routeID);
            deleteRouteByID.setLong(2,authorID);
            return deleteRouteByID.executeUpdate();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }
    public synchronized long deleteRoutesByAuthor(long authorID){
        try (Connection connection = dbConnector.connect();
        PreparedStatement deleteRouteByAuthor = connection.prepareStatement(DBQuery.DELETE_ROUTES_BY_AUTHOR.getQuery())
        ){
            deleteRouteByAuthor.setLong(1,authorID);
            return deleteRouteByAuthor.executeUpdate();
        }
        catch (SQLException e) {
            Logger.error(e.getMessage());
            return -1;
        }
    }
}
