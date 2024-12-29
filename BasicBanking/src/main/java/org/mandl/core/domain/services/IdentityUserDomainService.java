package org.mandl.core.domain.services;

import org.mandl.core.domain.identity.IdentityUser;
import org.mandl.core.domain.repositories.UserRepository;

public class IdentityUserDomainService {
    private final UserRepository userRepository;

    public IdentityUserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(IdentityUser user) {
        userRepository.save(user);
    }

    public void deleteUser(IdentityUser user) {
        userRepository.delete(user);
    }

    public IdentityUser findById(IdentityUser user) {
        return userRepository.findById(user.getId());
    }

    public IdentityUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
