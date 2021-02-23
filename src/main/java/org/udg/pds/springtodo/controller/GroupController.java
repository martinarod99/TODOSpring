package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import org.hibernate.loader.plan.build.internal.returns.CollectionFetchableElementCompositeGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController{

    @Autowired
    GroupService groupService;

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody R_Group group){
        Long ownerId = getLoggedUser(session);
        return groupService.addGroup(group.name, ownerId, group.description);
    }

    @GetMapping(path="/me")
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session) {
        Long ownerId = getLoggedUser(session);
        return groupService.getGroups(ownerId);
    }

    @PostMapping(path="/{id}/members")
    public String addMembers(@RequestBody Collection<Long> members, HttpSession session,
                             @PathVariable("id") Long groupId) {
        Long ownerId = getLoggedUser(session);
        groupService.addMembersToGroup(ownerId, groupId, members);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="{id}/members")
    public Collection<User> listAllMembers(HttpSession session,
                                           @PathVariable("id") Long groupId) {
        Long ownerId = getLoggedUser(session);
        return groupService.getMembers(ownerId, groupId);
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;
    }

}
