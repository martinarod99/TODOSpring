package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() { return groupRepository; }

    @Transactional
    public IdObject addGroup(String name, Long ownerId, String description){
        try {
            User owner = userService.getUser(ownerId);
            Group group = new Group(name,  description);
            group.setOwner(owner);
            owner.addGroup(group);
            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    public Collection<Group> getGroups(Long id) {
        Optional<User> u = userService.crud().findById(id);
        if (!u.isPresent()) throw new ServiceException("User owner does not exist");
        return u.get().getGroups();
    }

    public Group getGroup(Long ownerId, Long id) {
        Optional<Group> g = groupRepository.findById(id);
        if (!g.isPresent()) throw new ServiceException("Group does not exist");
        if (g.get().getOwner().getId() != ownerId)
            throw new ServiceException("User does not own this group");
        return g.get();
    }

    public Collection<User> getMembers(Long ownerId, Long groupId) {
        Optional<User> o = userService.crud().findById(ownerId);
        if (!o.isPresent()) throw new ServiceException("User owner does not exist");
        Optional<Group> g = groupRepository.findById(groupId);
        if (!g.isPresent()) throw new ServiceException("Group does not exist");
        return g.get().getMembers();
    }

    @Transactional
    public void addMembersToGroup(Long ownerId, Long groupId, Collection<Long> members) {
        Group g = this.getGroup(ownerId, groupId);
        if (g.getOwner().getId() != ownerId)
            throw new ServiceException("This user is not the owner of this group");

        try {
            for (Long memberId : members) {
                Optional<User> omem = userService.crud().findById(memberId);
                if (omem.isPresent()) {
                    User member = omem.get();
                    g.addMember(member);
                    member.addMemberGroups(g);
                }
                else
                    throw new ServiceException("User does not exist");
            }
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
