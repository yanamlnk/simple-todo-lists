package com.simpletodo.repositories;

import com.simpletodo.entities.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    List<ToDoList> findAll();
    Optional<ToDoList> findById(Long id);
    ToDoList save(ToDoList toDoList);
    void delete(ToDoList entity);
}
