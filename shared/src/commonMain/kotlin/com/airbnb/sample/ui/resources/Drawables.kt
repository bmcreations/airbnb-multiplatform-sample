package com.airbnb.sample.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun resourceForName(name: String) = painterResource("drawable/$name.xml")
object Drawables {

    val AirbnbLogo: Painter
        @Composable get() = resourceForName("ic_airbnb")
    val FacebookLogo: Painter
        @Composable get() = resourceForName("ic_facebook")
    val GoogleLogo: Painter
        @Composable get() = resourceForName("ic_google")
    val AppleLogo: Painter
        @Composable get() = resourceForName("ic_apple")

    val DevicePhone: Painter
        @Composable get() = resourceForName("ic_device_phone")
    val EnvelopeClosed: Painter
        @Composable get() = resourceForName("ic_envelope_closed")


    val ChatCentered: Painter
        @Composable get() = resourceForName("ic_chat_centered")

    val Settings: Painter
        @Composable get() = resourceForName("ic_settings")

    val HelpCircle: Painter
        @Composable get() = resourceForName("ic_help_circle")
}