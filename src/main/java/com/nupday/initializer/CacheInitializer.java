package com.nupday.initializer;

import com.nupday.cache.OwnerCache;
import com.nupday.cache.VisitorCache;
import com.nupday.dao.repository.OwnerRepository;
import com.nupday.dao.repository.VisitorRepository;
import com.nupday.service.OwnerService;
import com.nupday.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class CacheInitializer implements CommandLineRunner {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Override
    public void run(String... args) throws Exception {
        initUser();
    }

    private void initUser() {
        OwnerCache.setOwnerCache(ownerRepository.findAll());
        VisitorCache.setVisitorCache(visitorRepository.findAll());
    }
}
