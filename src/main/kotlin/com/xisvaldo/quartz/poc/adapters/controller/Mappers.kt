package com.xisvaldo.quartz.poc.adapters.controller

import com.xisvaldo.quartz.poc.usecases.SchedulerInput

fun SchedulerRequestBody.toUseCase() =
    SchedulerInput(identity = identity, description = description, intervalInSeconds = intervalInSeconds)
