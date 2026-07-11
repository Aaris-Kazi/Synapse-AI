package com.izak.synapse_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.izak.synapse_backend.entities.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);


    @Query("SELECT u FROM Users u WHERE u.username = :username AND u.password = :password")
    Optional<Users> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    boolean existsByUsername(@Param("username") String username);

    @Query("insert into Users (firstName, lastName, username, email, password) values (:firstName, :lastName, :username, :email, :password)")
    void saveUser(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("username") String username, @Param("email") String email, @Param("password") String password);


    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);
}
