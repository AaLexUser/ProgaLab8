package com.lapin.server.db;

public enum DBQuery {
    CREATE_ROUTE_TABLE("CREATE TABLE IF NOT EXISTS ROUTE(\n" +
            "\tID SERIAL PRIMARY KEY,\n" +
            "\tNAME VARCHAR(80) NOT NULL,\n" +
            "\tCOORD_ID BIGINT NOT NULL,\n" +
            "\tCREATION_DATE DATE NOT NULL,\n" +
            "\tLOC_FROM_ID BIGINT NOT NULL,\n" +
            "\tLOC_TO_ID BIGINT NOT NULL,\n" +
            "\tDISTANCE BIGINT NOT NULL CHECK(DISTANCE > 1),\n" +
            "\tAUTHOR_ID BIGINT NOT NULL,\n" +
            "\tFOREIGN KEY(COORD_ID) REFERENCES COORDINATES(ID) ON UPDATE CASCADE ON DELETE RESTRICT,\n" +
            "\tFOREIGN KEY(LOC_FROM_ID) REFERENCES LOCATION_FROM(ID) ON UPDATE CASCADE ON DELETE RESTRICT,\n" +
            "\tFOREIGN KEY(LOC_TO_ID) REFERENCES LOCATION_TO(ID) ON UPDATE CASCADE ON DELETE RESTRICT,\n" +
            "\tFOREIGN KEY(AUTHOR_ID) REFERENCES USERS(ID) ON UPDATE CASCADE ON DELETE CASCADE\n" +
            ")"),
    CREATE_COORDINATES_TABLE("CREATE TABLE IF NOT EXISTS COORDINATES(\n" +
            "\tID BIGSERIAL PRIMARY KEY,\n" +
            "\tCOORD_X DOUBLE PRECISION CHECK(COORD_X <= 669) NOT NULL,\n" +
            "\tCOORD_Y DOUBLE PRECISION\n" +
            ") "),
    CREATE_LOCATION_FROM_TABLE("CREATE TABLE IF NOT EXISTS LOCATION_FROM(\n" +
            "\tID BIGSERIAL PRIMARY KEY,\n" +
            "\tLOC_FROM_X INT NOT NULL,\n" +
            "\tLOC_FROM_Y DOUBLE PRECISION,\n" +
            "\tLOC_FROM_Z DOUBLE PRECISION\n" +
            ")"),
    CREATE_LOCATION_TO_TABLE("CREATE TABLE IF NOT EXISTS LOCATION_TO(\n" +
            "ID BIGSERIAL PRIMARY KEY,\n" +
            "\tLOC_TO_X DOUBLE PRECISION NOT NULL,\n" +
            "\tLOC_TO_Y BIGINT NOT NULL,\n" +
            "\tLOC_TO_NAME VARCHAR(80) NOT NULL)"),
    CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS USERS(\n" +
            "\tID BIGSERIAL PRIMARY KEY,\n" +
            "\tUSERNAME VARCHAR(80) UNIQUE NOT NULL CHECK(LENGTH(USERNAME) >= 1),\n" +
            "\tHASH VARCHAR(256) NOT NULL CHECK(LENGTH(HASH) >= 6)\n" +
            ")"),
    INSERT_USER("INSERT INTO users (username, hash) values(?, ?) RETURNING id"),
    SELECT_USER_BY_USERNAME_AND_PASSWORD("SELECT id from users where username = ? and hash = ?"),
    SELECT_USER_BY_USERNAME("SELECT id from users where username = ?"),
    SELECT_ALL_ROUTES("SELECT ROUTE.ID, ROUTE.NAME, COORD_X, COORD_Y, CREATION_DATE,\n" +
            "LOC_FROM_X, LOC_FROM_Y, LOC_FROM_Z,\n" +
            "LOC_TO_X, LOC_TO_Y, LOC_TO_NAME,\n" +
            "DISTANCE, AUTHOR_ID, USERNAME FROM ROUTE\n" +
            "JOIN COORDINATES ON ROUTE.COORD_ID = COORDINATES.ID \n" +
            "JOIN LOCATION_FROM ON ROUTE.LOC_FROM_ID = LOCATION_FROM.ID\n" +
            "JOIN LOCATION_TO ON ROUTE.LOC_TO_ID = LOCATION_TO.ID\n" +
            "JOIN USERS ON ROUTE.AUTHOR_ID = USERS.ID"),
    INSERT_ROUTE("with coords as \n" +
            "(INSERT INTO COORDINATES (COORD_X, COORD_Y) \n" +
            " VALUES (?, ?) ON CONFLICT DO NOTHING RETURNING ID),\n" +
            " loc_from as\n" +
            " (INSERT INTO LOCATION_FROM (LOC_FROM_X, LOC_FROM_Y, LOC_FROM_Z)\n" +
            " VALUES (?, ?, ?) ON CONFLICT DO NOTHING RETURNING ID),\n" +
            " loc_to as \n" +
            " (INSERT INTO LOCATION_TO (LOC_TO_X, LOC_TO_Y, LOC_TO_NAME)\n" +
            " VALUES (?, ?, ?) ON CONFLICT DO NOTHING RETURNING ID)\n" +
            " INSERT INTO ROUTE (NAME, COORD_ID, CREATION_DATE, LOC_FROM_ID, \n" +
            "\t\t\t\t\tLOC_TO_ID, DISTANCE, AUTHOR_ID)\n" +
            "\t\t\t\t\tVALUES (?,\n" +
            "\t\t\t\t\t\t\t(SELECT ID FROM coords),\n" +
            "\t\t\t\t\t\t\t?,\n" +
            "\t\t\t\t\t\t\t(SELECT ID FROM loc_from),\n" +
            "\t\t\t\t\t\t\t(SELECT ID FROM loc_to),\n" +
            "\t\t\t\t\t\t\t?, ?)\n" +
            "\t\t\t\t\tRETURNING ID"),
    UPDATE_ROUTE("with coords as \n" +
            "(INSERT INTO COORDINATES (COORD_X, COORD_Y) \n" +
            " VALUES (?, ?) ON CONFLICT DO NOTHING RETURNING ID),\n" +
            " loc_from as\n" +
            " (INSERT INTO LOCATION_FROM (LOC_FROM_X, LOC_FROM_Y, LOC_FROM_Z)\n" +
            " VALUES (?, ?, ?) ON CONFLICT DO NOTHING RETURNING ID),\n" +
            " loc_to as \n" +
            " (INSERT INTO LOCATION_TO (LOC_TO_X, LOC_TO_Y, LOC_TO_NAME)\n" +
            " VALUES (?, ?, ?) ON CONFLICT DO NOTHING RETURNING ID)\n" +
            " UPDATE ROUTE SET \n" +
            "\t\t\t\tNAME = ?,\n" +
            "\t\t\t\tCOORD_ID = (SELECT ID FROM coords),\n" +
            "\t\t\t\tLOC_FROM_ID = (SELECT ID FROM loc_from) ,\n" +
            "\t\t\t\tLOC_TO_ID = (SELECT ID FROM loc_to),\n" +
            "\t\t\t\tDISTANCE = ?\n" +
            "\t\t\t\t\tWHERE ID = ? AND AUTHOR_ID = ?\n" +
            "\t\t\t\t\tRETURNING ID"),
    DELETE_ROUTE_BY_ID("DELETE FROM ROUTE WHERE ID = ? AND AUTHOR_ID = ?"),
    DELETE_ROUTES_BY_AUTHOR("DELETE FROM ROUTE WHERE AUTHOR_ID = ?");
    private final String query;

    DBQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
