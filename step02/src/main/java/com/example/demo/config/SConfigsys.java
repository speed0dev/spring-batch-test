package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SConfigsys {



    @PostConstruct
    public void postInit(){
        log.debug(" @@@@@ [SConfigsys] postInit() 001 --------------" );
        log.debug(" @@@@@ [SConfigsys] postInit() 002 --------------" );
        log.debug(" @@@@@ [SConfigsys] postInit() 003 --------------" );


    }
}
