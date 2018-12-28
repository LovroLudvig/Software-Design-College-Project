package hr.fer.handMadeShopBackend;

import hr.fer.handMadeShopBackend.domain.User;
import hr.fer.handMadeShopBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Example component used to insert some test students at application startup.
 */
@Component
public class DataInitializer {
//    @Autowired
//    private StudentService studentService;
//
//    @Value("${opp.test.student.names}")
//    private String testNames;
//
//    @Value("${opp.test.student.leads}")
//    private int testLeadsCount;
//
//    @EventListener
//    public void appReady(ApplicationReadyEvent event) {
//        String[] names = testNames.split(",");
//        Assert.isTrue(names.length < 10, "Can insert max 9 users");
//        for (int i = 0; i < names.length; i++) {
//            studentService.createStudent(makeStudent(names[i], i));
//        }
//    }
//
//    private User makeStudent(String prefix, int i) {
//        User student = new User();
//        student.setName(prefix + "ica");
//        student.setLastName(prefix + "ić");
//        return student;
//    }
}

