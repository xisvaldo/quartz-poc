package com.xisvaldo.quartz.poc.usecases

data class UpsertSchedulerInput(val identity: String, val description: String, val intervalInSeconds: Int)
