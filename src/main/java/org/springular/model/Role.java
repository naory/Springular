package org.springular.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springular.ui.annotation.UiForm;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Naor Yuval
 * Date: 12/13/13
 * Role entity
 */
@Entity
@Table(name="ROLE")
@UiForm(filterApiUrl = "/api/roles/find", addApiUrl = "/api/roles/add", editApiUrl = "/api/roles/update")
public class Role {

    @Id
    @JsonSerialize(contentAs = Integer.class)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String name;


    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String description;


    public Role(){
        super();
    }


    public Role(Long id, String name, String description){
        super();
        this.id = id;
        this.name = name;
        this.description = description;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String r) {
        name = r;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}
