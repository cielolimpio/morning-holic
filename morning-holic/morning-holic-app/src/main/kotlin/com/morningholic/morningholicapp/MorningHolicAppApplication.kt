package com.morningholic.morningholicapp

import com.morningholic.morningholiccommon.entities.DiaryImages
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct

@EnableScheduling
@SpringBootApplication
class MorningHolicAppApplication {
	@PostConstruct
	fun started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	}
}

fun main(args: Array<String>) {
    runApplication<MorningHolicAppApplication>(*args)
}
