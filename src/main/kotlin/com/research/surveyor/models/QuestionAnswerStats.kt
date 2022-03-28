package com.research.surveyor.models

data class QuestionAnswerStats(
    val questionId: Long,
    val answerOptionPercentiles: List<AnswerOptionPercentile>,
    val totalVotes: Long
)

data class AnswerOptionPercentile(
    val answerOptionId: Long,
    val votes: Long
)
