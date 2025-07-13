package com.example.spendwise.data.roomDb.model

enum class ExpenseCategoryEnum(val categoryName: String) {
    STAFF(categoryName = "Staff"),
    TRAVEL(categoryName = "Travel"),
    FOOD(categoryName = "Food"),
    UTILITY(categoryName = "Utility")
}