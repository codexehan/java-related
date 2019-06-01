package codexe.han.cache.controller;

import codexe.han.cache.entity.Student;
import codexe.han.cache.service.Impl.StudentRedisServiceImpl;
import codexe.han.cache.service.StudentRedisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@AllArgsConstructor
public class TestController {
    private StudentRedisService studentRedisService;
    @PostConstruct
    public ResponseEntity getFromRedis(){
        Student std = this.studentRedisService.getStudent(1);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
