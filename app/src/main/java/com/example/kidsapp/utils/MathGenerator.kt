package com.example.kidsapp.utils

import kotlin.random.Random

data class MathProblem(
    val n1: Int = 0,
    val n2: Int = 0,
    val operator: String = "",
    val displayValue: String? = null
) {
    override fun toString(): String {
        return displayValue ?: "$n1 $operator $n2 ="
    }
}

object MathGenerator {
    fun generateProblems(grade: String, operation: String, difficulty: String? = null, count: Int = 30): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        
        if (grade == "pre" && (operation == "numbers" || operation == "alphabets" || operation == "shapes" || operation == "colors")) {
            return generateIdentification(operation, difficulty ?: "1-10", count)
        }

        // If difficulty is specified, it overrides the grade-based range
        val range = if (difficulty != null) {
            getRangeForDifficulty(difficulty, operation)
        } else {
            getRangeForGrade(grade, operation)
        }

        repeat(count) {
            problems.add(generateSingleProblem(range, operation))
        }
        return problems
    }

    private fun generateIdentification(type: String, difficulty: String, count: Int): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        when (type) {
            "numbers" -> {
                val range = getRangeForDifficulty(difficulty, "numbers")
                val rangeList = range.toList().shuffled()
                
                // Add all numbers in range first to ensure coverage
                rangeList.forEach { n ->
                    if (problems.size < count) {
                        problems.add(MathProblem(displayValue = n.toString()))
                    }
                }
                
                // Fill the rest with random numbers from range
                while (problems.size < count) {
                    problems.add(MathProblem(displayValue = range.random().toString()))
                }
                
                // Shuffle final list to mix the guaranteed ones
                problems.shuffle()
            }
            "alphabets" -> {
                val chars = when (difficulty) {
                    "lowercase" -> ('a'..'z').toList()
                    "both" -> ('A'..'Z').toList() + ('a'..'z').toList()
                    else -> ('A'..'Z').toList() // Default uppercase
                }
                repeat(count) {
                    problems.add(MathProblem(displayValue = chars.random().toString()))
                }
            }
            "shapes" -> {
                val shapes = listOf(
                    "Circle ⭕", "Square ⬛", "Triangle 🔺", "Star ⭐", 
                    "Heart ❤️", "Diamond 💠", "Pentagon ⬠", "Hexagon ⬡"
                )
                repeat(count) {
                    problems.add(MathProblem(displayValue = shapes.random()))
                }
            }
            "colors" -> {
                val colorItems = listOf(
                    "Red Apple 🍎", "Blue Book 📘", "Green Leaf 🌿", "Yellow Sunflower 🌻",
                    "Orange Fruit 🍊", "Purple Grapes 🍇", "Pink Flower 🌸", "Brown Bear 🐻",
                    "Black Cat 🐈‍⬛", "White Cloud ☁️"
                )
                repeat(count) {
                    problems.add(MathProblem(displayValue = colorItems.random()))
                }
            }
        }
        return problems
    }

    private fun getRangeForDifficulty(difficulty: String, operation: String): IntRange {
        return when (difficulty) {
            "1-5" -> 1..5
            "5-10" -> 5..10
            "1-10" -> 1..10
            "1-20" -> 1..20
            "1-50" -> 1..50
            "1-100" -> 1..100
            "1-200" -> 1..200
            "1-500" -> 1..500
            else -> 1..10
        }
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
                // For difficulty, we might want the product to stay within range 
                // but usually ranges like 1-10 mean the numbers being multiplied.
                // However, user said "in specefic number", which might mean the result.
                // Let's assume the numbers being operated on should be in range for now, 
                // except for division where result * n2 = n1.
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
