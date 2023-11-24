package com.airbnb.sample.networking

object Unsplash {
    fun randomImageUrl(typeQuery: String, searchQuery: String? = null) = "https://source.unsplash.com/random/?$typeQuery".let {
        if (searchQuery != null) {
            it.plus("+$searchQuery")
        } else {
            it
        }
    }
}