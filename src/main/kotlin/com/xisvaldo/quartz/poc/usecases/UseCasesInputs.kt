package com.xisvaldo.quartz.poc.usecases

data class UpsertSchedulerInput(val identity: String, val group: String, val description: String, val cron: String)
