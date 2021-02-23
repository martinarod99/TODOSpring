package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.User;

import java.util.List;

@Component
public interface GroupRepository extends CrudRepository<Group, Long> {
}
