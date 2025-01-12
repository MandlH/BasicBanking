package org.mandl.services;

import jakarta.inject.Inject;
import org.mandl.LoggingHandler;
import org.mandl.repositories.UserContext;

public abstract class BaseDomainService {

    @Inject
    protected UserContext userContext;

    protected LoggingHandler logger;

    protected BaseDomainService() {
        logger = LoggingHandler.getLogger(this.getClass());
    }
}
