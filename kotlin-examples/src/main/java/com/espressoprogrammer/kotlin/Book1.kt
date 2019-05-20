package com.espressoprogrammer.kotlin

data class Book1(val title: String,
                 val authors: List<String>) {

}

fun createBook1(title: String, author: String): Book1 {
    return Book1(title, listOf(author));
}