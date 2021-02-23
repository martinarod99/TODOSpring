package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name="usergroup")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    public Group(){
    }

    public Group(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_owner")
    private User owner;

    @Column(name="fk_owner", insertable = false, updatable = false)
    private Long ownerId;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "member_groups")
    private Collection<User> members = new ArrayList<>();

    @JsonView(Views.Private.class)
    public Long getId() { return id;}

    public void setOwner(User owner) { this.owner = owner; }

    @JsonIgnore
    public User getOwner() { return owner; }

    @JsonView(Views.Private.class)
    public String getName() { return name; }

    @JsonView(Views.Private.class)
    public String getDescription() { return description; }

    public void addMember(User member) { members.add(member); }

    @JsonView(Views.Complete.class)
    public Collection<User> getMembers() {
        members.size();
        return members;
    }
}

