package org.mandl.services;

import jakarta.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mandl.LoggingHandler;
import org.mandl.repositories.UserContext;

public abstract class BaseDomainService {

    private static final Log log = LogFactory.getLog(BaseDomainService.class);
    @Inject
    protected UserContext userContext;

    protected LoggingHandler logger;

    protected BaseDomainService() {
        logger = LoggingHandler.getLogger(this.getClass());
    }
}
