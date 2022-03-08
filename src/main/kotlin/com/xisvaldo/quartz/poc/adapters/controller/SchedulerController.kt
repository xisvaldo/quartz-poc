package com.xisvaldo.quartz.poc.adapters.controller

import com.xisvaldo.quartz.poc.usecases.CreateScheduler
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scheduler")
class SchedulerController(
    @Qualifier("QuartzScheduler") private val scheduler: Scheduler,
    private val createScheduler: CreateScheduler
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createScheduler(@RequestBody schedulerRequestBody: SchedulerRequestBody) {
        createScheduler(schedulerInput = schedulerRequestBody.toUseCase())
    }
}
