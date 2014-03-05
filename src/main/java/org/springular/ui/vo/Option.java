package org.springular.ui.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Value object that represents an HTML option for multi/single select list
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Option<T, String>{

    T id;

    String name;

    public Option(T id, String name){
        this.name = name;
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}