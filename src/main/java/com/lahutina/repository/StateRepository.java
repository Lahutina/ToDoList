package com.lahutina.repository;

import com.lahutina.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findByName(String name);

    @Query(value = "select * from states order by id", nativeQuery = true)
    List<State> getAll();

}
