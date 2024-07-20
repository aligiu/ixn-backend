package com.hng.ixn;

import com.hng.ixn.content.Content;
import com.hng.ixn.content.ContentRepository;
import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import com.hng.ixn.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ContentRepository contentRepository;

    @Value("${data.initialize:true}")  // Default to true if not specified
    private boolean initialize;

    @PostConstruct
    public void init() throws Exception {
        if (initialize) {
            initializeUsers();
            initializeContent();
        } else {
            System.out.println("Data initialization is skipped.");
        }
    }

    private void initializeUsers() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("a").isEmpty()) {
            User admin = User.builder()
                    .email("a")
                    .password(passwordEncoder.encode("a"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
        System.out.println("Users initialized");
    }

    private void initializeContent() {
        // Clear existing data
        contentRepository.deleteAll();

        // Insert new data
        contentRepository.saveAll(List.of(
                Content.builder()
                        .id(1)
                        .title("Ashford and St Peter's")
                        .description("A comprehensive overview of services and specialties offered.")
                        .content("<h1>Gibberish</h1><h3>Point 1</h3><p>Lorem ipsum dolor <u>sit amet, </u><strong><u>consectetur</u></strong><u> adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqu</u>a. </p><h3>Point 2</h3><p>Ut enim ad minim veniam, quis </p><p>nostrud exercitation ullamco</p><ul><li><p>laboris <s>nisi</s> ut aliquip ex </p></li><li><p>ea commodo consequat. </p></li><li><p>Duis aute irure dolor in reprehenderit in</p></li></ul><p>voluptate velit </p><ol><li><p>esse cillum dolore </p></li><li><p>eu fugiat nulla pariatur.</p></li></ol><h3>Point 3 </h3><ul data-type=\"taskList\"><li data-checked=\"false\" data-type=\"taskItem\"><label><input type=\"checkbox\"><span></span></label><div><p>Excepteur sint occaecat <strong>cupidatat</strong> non proident, sunt in culpa qui officia</p></div></li><li data-checked=\"true\" data-type=\"taskItem\"><label><input type=\"checkbox\" checked=\"checked\"><span></span></label><div><p>Deserunt mollit anim id est laborum</p></div></li></ul>")
                        .nextId(2)
                        .prevId(null)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .secret("<p>This secret is visible to users and admins, but not guests</p><p>Email: abc@email.com</p><p>Password: 123</p>")
                        .build(),
                Content.builder()
                        .id(2)
                        .title("East Surrey")
                        .description("Information on facilities and patient care at East Surrey.")
                        .content("<p>Some important content</p>")
                        .nextId(3)
                        .prevId(1)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .secret("<p>Here's another secret...</p>")
                        .build(),
                Content.builder()
                        .id(3)
                        .title("Frimley")
                        .description("Details about the Frimley Health NHS Foundation Trust.")
                        .content("")
                        .nextId(4)
                        .prevId(2)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(4)
                        .title("Royal Surrey")
                        .description("An in-depth look at Royal Surrey's medical services.")
                        .content("")
                        .nextId(5)
                        .prevId(3)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(5)
                        .title("Wexham")
                        .description("Insights into the Wexham Park Hospital operations.")
                        .content("")
                        .nextId(6)
                        .prevId(4)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(6)
                        .title("Academy")
                        .description("Educational programs and training at the Academy.")
                        .content("")
                        .nextId(7)
                        .prevId(5)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(7)
                        .title("ARCP")
                        .description("Annual Review of Competence Progression guidelines.")
                        .content("")
                        .nextId(8)
                        .prevId(6)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(8)
                        .title("Conferences")
                        .description("Upcoming conferences and events for medical professionals.")
                        .content("")
                        .nextId(9)
                        .prevId(7)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(9)
                        .title("Educational Resources")
                        .description("Learning materials and resources for medical education.")
                        .content("")
                        .nextId(10)
                        .prevId(8)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(10)
                        .title("Exams")
                        .description("Examination schedules and preparation tips.")
                        .content("")
                        .nextId(null)
                        .prevId(9)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build()
        ));
        System.out.println("Content data initialized");
    }
}
