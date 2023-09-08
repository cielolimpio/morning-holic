package com.morningholic.morningholicapp.controllers

import com.morningholic.morningholicapp.services.ScheduleService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController

@RestController
class SchedulerController(
    private val scheduleService: ScheduleService,
) {

    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    fun schedule() {
        scheduleService.processAllUserScores()
        scheduleService.createNewDiaries()
    }
}