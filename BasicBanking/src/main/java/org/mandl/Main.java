package org.mandl;

import org.mandl.core.domain.identity.IdentityUser;
import org.mandl.core.persistence.repository.IdentityUserRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        IdentityUserRepository userRepository = new IdentityUserRepository();
        try {
            // Save a new user
            userRepository.save(IdentityUser.create("TestUser2", "TestPassword"));

            // Fetch all users
            List<IdentityUser> users = userRepository.findAll();
            if (!users.isEmpty()) {
                for (IdentityUser user : users) {
                    System.out.println(user.getUsername());
                }
            } else {
                System.out.println("No users found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userRepository.close();
        }
    }
}
