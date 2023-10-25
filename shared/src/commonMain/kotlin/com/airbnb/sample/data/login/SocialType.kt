package com.airbnb.sample.data.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

sealed interface SocialType {
    @get:Composable
    val icon: Painter
    val name: String

    @get:Composable
    val tint: Color

    @OptIn(ExperimentalResourceApi::class)
    data object Facebook : SocialType {
        override val icon: Painter
            @Composable get() = painterResource("drawable/ic_facebook.xml")
        override val name: String
            get() = "Facebook"

        override val tint: Color
            @Composable get() = Color.Unspecified
    }

    @OptIn(ExperimentalResourceApi::class)
    data object Google : SocialType {
        override val icon: Painter
            @Composable get() = painterResource("drawable/ic_google.xml")
        override val name: String
            get() = "Google"

        override val tint: Color
            @Composable get() = Color.Unspecified
    }

    @OptIn(ExperimentalResourceApi::class)
    data object Apple : SocialType {
        override val icon: Painter
            @Composable get() = painterResource("drawable/ic_apple.xml")
        override val name: String
            get() = "Apple"

        override val tint: Color
            @Composable get() = MaterialTheme.colorScheme.onBackground
    }

    @OptIn(ExperimentalResourceApi::class)
    data class PhoneOrEmail(val isPhoneSelected: Boolean) : SocialType {
        override val icon: Painter
            @Composable get() = if (!isPhoneSelected) {
                painterResource("drawable/ic_device_phone.xml")
            } else {
                painterResource("drawable/ic_envelope_closed.xml")
            }
        override val name: String
            get() = if (!isPhoneSelected) "phone" else "email"

        override val tint: Color
            @Composable get() = MaterialTheme.colorScheme.onBackground
    }

    companion object {
        fun available(withPhone: Boolean) = listOf(PhoneOrEmail(withPhone), Facebook, Google, Apple)
    }
}