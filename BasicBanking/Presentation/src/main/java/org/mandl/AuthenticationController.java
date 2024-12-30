package org.mandl;

public class AuthenticationController extends BaseController {

    protected AuthenticationController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    @Override
    protected void execute() {
        System.out.println("Executing UserController logic...");
    }
}
