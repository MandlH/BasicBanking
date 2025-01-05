package org.mandl;

import java.util.List;
import java.util.UUID;

public interface IdentityUserService {
    void resetPassword(String password);
    void deactivateUser();
}
