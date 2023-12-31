package insertsdemo.services;


import insertsdemo.controller.StudentDatabaseJPAController;
import insertsdemo.dao.entity.Student;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ETAJob {

    private final StudentDatabaseJPAController studentDatabaseJPAController;

    @PostConstruct
    void insertRecords() {
        long startTime = System.currentTimeMillis();
        System.out.println("startTime " + startTime);
        for (int i = 0; i < 1000; i++) {
            Student student = new Student();
            student.setFirstName("Alice");
            student.setLastName("John");
            student.setStudentsClass(String.valueOf(i % 10));
            student.setAddress("California");
            if (i % 100 == 0) {
                System.out.println("Total Time consumed By Spring JPA module in inserting " + i + " records with auto increment @GeneratedValue and sql IDENTITY(1,1) is " + (System.currentTimeMillis() - startTime));
            }
            studentDatabaseJPAController.addNewStudent(student);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("endTime " + endTime);

        System.out.println("Total Time consumed By Spring JPA module in inserting 1000 records with auto increment @GeneratedValue and sql IDENTITY(1,1) is " + (endTime - startTime));
    }

    @PostConstruct
    void batchInsertRecords() {
        long startTime = System.currentTimeMillis();
        System.out.println(startTime);

        for (int i = 1; i <= 10; i++) {
            List<Student> studentList = new ArrayList<>();
            for (int j = 1; j <= 100; j++) {
                Student student = new Student();
                student.setFirstName("Alice");
                student.setLastName("John");
                student.setStudentsClass("6");
                student.setAddress("California");
                studentList.add(student);
            }
            studentDatabaseJPAController.addNewStudents(studentList);
            System.out.println("Total Time consumed By Spring JPA module in inserting " + (i * 100) + " records with auto increment @GeneratedValue and Batch of 100 is " + (System.currentTimeMillis() - startTime));
        }

        System.out.println(System.currentTimeMillis() - startTime);
    }
}
