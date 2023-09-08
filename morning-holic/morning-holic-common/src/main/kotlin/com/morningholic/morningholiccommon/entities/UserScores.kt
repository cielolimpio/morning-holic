package com.morningholic.morningholiccommon.entities

import org.jetbrains.exposed.dao.id.LongIdTable

object UserScores: LongIdTable("user_scores", "id") {
    val user = reference("user_id", Users)
    val year = integer("year")
    val month = integer("month")
    val score = integer("score").default(100)
}