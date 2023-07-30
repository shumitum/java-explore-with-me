package ru.practicum.mainsrv.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u " +
            "where ((:userIds) is NULL or u.id in :userIds)")
    List<User> getUsers(@Param("userIds") List<Long> userIds, PageRequest page);
}


