package com.xisvaldo.quartz.poc.adapters.controller

data class SchedulerRequestBody(val identity: String, val group: String, val description: String, val cron: String)
