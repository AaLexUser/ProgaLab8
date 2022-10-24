package com.lapin.common.data;

import com.lapin.di.annotation.ClassMeta;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Элементы коллекцией, которыми управляет программа
 */
@Getter
@ClassMeta(name = "route")
public class Route implements Comparable<Route>, Serializable {
    @Getter
    @Setter
    private int id; //This field's value must be greater than 0, This field's value must be unique, This field's value must be generated automatically
    @Getter
    @NotNull
    private String name; //This field can't be null, The string can't be empty
    @Getter
    @NotNull
    private Coordinates coordinates; //This field can't be null
    @Getter
    @NotNull
    private LocalDate creationDate; //This field can't be null, This field's value must be generated automatically
    @Getter
    @NotNull
    private LocationFrom from; //This field can't be null
    @Getter
    @NotNull
    private LocationTo to; //This field can't be null
    @Getter
    @NotNull
    private Long distance; //This field can't be null, This field's value must be greater than 1
    @Getter
    @Setter
    private User author;
    /**
     * Все поля Route в виде String
     */
    private List<String> routeList = new ArrayList<>();

    /**
     * @param name         Route name
     * @param coordinates  Route coordinates
     * @param from         Route from
     * @param to           Route to
     * @param distance     Route distance
     */
    public Route(@NotNull String name, @NotNull Coordinates coordinates,
                 @NotNull LocationFrom from,@NotNull LocationTo to,@NotNull Long distance) {
        try {
            if (name.equals("")) throw new IllegalArgumentException();
            else this.name = name;
            this.coordinates = coordinates;
            creationDate = LocalDate.now();
            this.from = from;
            this.to = to;
            if (distance < 1) throw new IllegalArgumentException("distance incorrect");
            else this.distance = distance;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Route(int id, @NotNull String name,@NotNull Coordinates coordinates,@NotNull LocalDate creationDate,
                 @NotNull LocationFrom from,@NotNull LocationTo to,@NotNull Long distance) {
        try {
            this.id = id;
            if (name.equals("")) throw new IllegalArgumentException();
            else this.name = name;
            this.coordinates = coordinates;
            this.creationDate = creationDate;
            this.from = from;
            this.to = to;
            if (distance < 1) throw new IllegalArgumentException("distance incorrect");
            else this.distance = distance;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", " + coordinates.toString() +
                ", creationDate: " + creationDate + ", " + from.toString() + ", " + to.toString() +
                ", distance: " + distance + ", Author: " + author.getUsername();
    }

    public String toStringWithoutId() {
        return  "name: " + name + ", " + coordinates.toString() +
                ", creationDate: " + creationDate + ", " + from.toString() + ", " + to.toString() +
                ", distance: " + distance;
    }

    @Override
    public int compareTo(Route routeObj) {
        Long compareDist = distance;
        return compareDist.compareTo(routeObj.getDistance());
    }

    /**
     * Class for comparison of Route elements by distance
     */
    public static class ComparatorByDistance implements Comparator<Route> {
        @Override
        public int compare(Route o1, Route o2) {
            return o1.getDistance().compareTo(o2.getDistance());
        }
    }

    /**
     * Class for comparison of Route elements by creation date
     */
    public static class ComparatorByCreationDate implements Comparator<Route> {
        @Override
        public int compare(Route o1, Route o2) {
            return o1.getCreationDate().compareTo(o2.getCreationDate());
        }
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Long getDistance(){
        return distance;
    }
}
