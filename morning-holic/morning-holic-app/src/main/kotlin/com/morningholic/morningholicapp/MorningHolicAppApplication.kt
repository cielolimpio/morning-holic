package com.morningholic.morningholicapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MorningHolicAppApplication

fun main(args: Array<String>) {
	runApplication<MorningHolicAppApplication>(*args)
}
