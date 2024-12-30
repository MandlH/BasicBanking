package org.mandl;

import java.util.List;

public abstract class BaseController {
    private final UserDto user;
    private ServiceManager serviceManager;
    LoggingHandler logger = LoggingHandler.getLogger(BaseController.class);

    protected BaseController(
            UserDto user,
            ServiceManager serviceManager) {
        this.user = user;
    }

    private boolean isAuthenticated() {
        return user != null;
    }

    private boolean isAuthorized(List<RoleDto> roles, List<ClaimDto> claims) {
        return serviceManager.getIdentityUserService().isAuthorized(user.getId(), roles, claims);
    }

    private boolean isAuthenticatedAndAuthorized(List<RoleDto> roles, List<ClaimDto> claims) {
        return isAuthenticated() && isAuthorized(roles, claims);
    }

    public final void start(List<RoleDto> roles, List<ClaimDto> claims) {
        if (this instanceof AuthenticationController){
            execute();
            return;
        }

        if (!isAuthenticatedAndAuthorized(roles, claims)){
            logger.warn(this.user.getUsername() + " attempted to enter " + this.getClass().getSimpleName());
            AuthenticationController authenticationController = new AuthenticationController(null, serviceManager);
            authenticationController.start(roles, claims);
            return;
        }

        execute();
    }

    protected abstract void execute();
}
