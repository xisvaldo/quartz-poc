package com.xisvaldo.quartz.poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = ["com.xisvaldo.quartz.poc"])
@ConfigurationPropertiesScan(basePackages = ["com.xisvaldo.quartz.poc"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
