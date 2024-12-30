package org.mandl;

public class NavigationController extends BaseController {

    private NavigationController(UserDto user, ServiceManager serviceManager) {
        super(user, serviceManager);
    }

    public static Controller create(UserDto user, ServiceManager serviceManager) {
        return new NavigationController(user, serviceManager);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected void displayActions() {

    }
}
