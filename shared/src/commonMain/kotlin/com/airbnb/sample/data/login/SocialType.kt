package com.airbnb.sample.data.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.airbnb.sample.ui.resources.Drawables

sealed interface SocialType {
    @get:Composable
    val icon: Painter
    val name: String

    @get:Composable
    val tint: Color

    data object Facebook : SocialType {
        override val icon: Painter
            @Composable get() = Drawables.FacebookLogo
        override val name: String
            get() = "Facebook"

        override val tint: Color
            @Composable get() = Color.Unspecified
    }

    data object Google : SocialType {
        override val icon: Painter
            @Composable get() = Drawables.GoogleLogo
        override val name: String
            get() = "Google"

        override val tint: Color
            @Composable get() = Color.Unspecified
    }

    data object Apple : SocialType {
        override val icon: Painter
            @Composable get() = Drawables.AppleLogo
        override val name: String
            get() = "Apple"

        override val tint: Color
            @Composable get() = MaterialTheme.colorScheme.onBackground
    }

    data class PhoneOrEmail(val isPhoneSelected: Boolean) : SocialType {
        override val icon: Painter
            @Composable get() = if (!isPhoneSelected) {
                Drawables.DevicePhone
            } else {
                Drawables.EnvelopeClosed
            }
        override val name: String
            get() = if (!isPhoneSelected) "phone" else "email"

        override val tint: Color
            @Composable get() = MaterialTheme.colorScheme.onBackground
    }

    companion object {
        fun available(withPhone: Boolean) = listOf(PhoneOrEmail(withPhone), Apple, Google, Facebook)
    }
}