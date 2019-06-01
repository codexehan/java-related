package codexe.han.cache.service;

import codexe.han.cache.entity.Student;

public interface StudentRedisService {
    void addStudent();

    Student getStudent(Integer id);
}
