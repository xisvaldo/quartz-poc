package com.xisvaldo.quartz.poc.adapters.controller

data class SchedulerRequestBody(val identity: String, val description: String, val intervalInSeconds: Int)
