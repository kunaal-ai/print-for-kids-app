package com.example.kidsapp.utils

import kotlin.random.Random

data class MathProblem(
    val n1: Int,
    val n2: Int,
    val operator: String
) {
    override fun toString(): String {
        return "$n1 $operator $n2 ="
    }
}

object MathGenerator {
    fun generateProblems(grade: String, operation: String, count: Int = 30): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        val range = getRangeForGrade(grade, operation)

        repeat(count) {
            problems.add(generateSingleProblem(range, operation))
        }
        return problems
    }

    private fun getRangeForGrade(grade: String, operation: String): IntRange {
        if (operation == "multiply" || operation == "divide") {
            return when (grade) {
                "pre" -> 0..2   // Very basic
                "k" -> 0..5     // Beginner
                "1" -> 1..5     // Simple tables
                "2" -> 1..10    // Full tables
                else -> 1..10
            }
        }
        // Add/Sub
        return when (grade) {
            "pre" -> 0..5
            "k" -> 0..10
            "1" -> 0..20
            "2" -> 0..100
            else -> 0..20
        }
    }

    private fun generateSingleProblem(range: IntRange, operation: String): MathProblem {
        var n1 = 0
        var n2 = 0
        val opSymbol = when (operation) {
            "add" -> "+"
            "subtract" -> "-"
            "multiply" -> "×"
            "divide" -> "÷"
            else -> "+"
        }

        when (operation) {
            "add" -> {
                n1 = range.random()
                n2 = range.random()
            }
            "subtract" -> {
                n1 = range.random()
                n2 = range.random()
                // Ensure non-negative result
                if (n2 > n1) {
                    val temp = n1
                    n1 = n2
                    n2 = temp
                }
            }
            "multiply" -> {
                n1 = range.random()
                n2 = range.random()
            }
            "divide" -> {
                // Ensure clean division: n1 = result * n2
                val result = range.random()
                n2 = range.random().coerceAtLeast(1) // Avoid div by zero
                n1 = result * n2
            }
        }
        return MathProblem(n1, n2, opSymbol)
    }
}
