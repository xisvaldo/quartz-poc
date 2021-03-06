package com.xisvaldo.quartz.poc.adapters.controller

import com.xisvaldo.quartz.poc.usecases.UpsertSchedulerInput

fun SchedulerRequestBody.toUseCase() =
    UpsertSchedulerInput(identity = identity, group = group, description = description, cron = cron)
