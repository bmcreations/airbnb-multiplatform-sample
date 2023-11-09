package com.airbnb.sample.utils.uikit

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.CoreGraphics.CGSizeZero
import platform.QuartzCore.kCALayerMaxXMaxYCorner
import platform.QuartzCore.kCALayerMaxXMinYCorner
import platform.QuartzCore.kCALayerMinXMaxYCorner
import platform.QuartzCore.kCALayerMinXMinYCorner
import platform.UIKit.UIBezierPath
import platform.UIKit.UIColor
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage
import platform.UIKit.UIRectCornerAllCorners
import platform.UIKit.UIView

var UIView.cornerRadius: CGFloat
    get() = layer.cornerRadius
    set(value) {
        layer.cornerRadius = value
    }

var UIView.borderWidth: CGFloat
    get() = layer.borderWidth
    set(value) {
        layer.borderWidth = value
    }

@OptIn(ExperimentalForeignApi::class)
var UIView.borderColor: UIColor?
    get() = layer.borderColor?.usePinned { this.borderColor }
    set(value) {
        if (value != null) {
            layer.borderColor = value.CGColor
        } else {
            layer.shadowColor = null
        }
    }

@OptIn(ExperimentalForeignApi::class)
var UIView.shadowPath: UIBezierPath?
    get() = layer.shadowPath?.usePinned { shadowPath }
    set(value) {
        if (value != null) {
            layer.shadowPath = value.CGPath
        } else {
            layer.shadowPath = null
        }
    }


var UIView.shadowRadius: CGFloat
    get() = layer.shadowRadius
    set(value) {
        layer.shadowRadius = value
    }

var UIView.shadowOpacity: Float
    get() = layer.shadowOpacity
    set(value) {
        layer.shadowOpacity = value
    }

@OptIn(ExperimentalForeignApi::class)
var UIView.shadowOffset: CValue<CGSize>
    get() = layer.shadowOffset
    set(value) {
        layer.shadowOffset = value
    }

@OptIn(ExperimentalForeignApi::class)
var UIView.shadowColor: UIColor?
    get() = layer.shadowColor?.usePinned { this.shadowColor }
    set(value) {
        if (value != null) {
            layer.shadowColor = value.CGColor
        } else {
            layer.shadowColor = null
        }
    }

var UIView.masksToBounds: Boolean
    get() = layer.masksToBounds
    set(value) {
        layer.masksToBounds = value
    }

@OptIn(ExperimentalForeignApi::class)
fun UIView.asImage(): UIImage {
    val format = UIGraphicsImageRendererFormat().apply {
        scale = 5.0
    }
    val renderer = UIGraphicsImageRenderer(bounds, format)
    return renderer.imageWithActions {
        it?.let {
            layer.renderInContext(it.CGContext)
        }
    }
}

inline fun uiView(
    cornerRadius: Double? = null,
    shadowRadius: Double? = null,
    shadowOffsetX: Double = .0,
    shadowOffsetY: Double = .0,
    shadowColorAlpha: Double = .15,
    shadowColor: UIColor = UIColor.blackColor,
    setup: UIView.() -> Unit = {}
) =
    UIView().apply {
        translatesAutoresizingMaskIntoConstraints = false
        backgroundColor = UIColor.whiteColor
        cornerRadius?.let { layer.cornerRadius = it }
        shadowRadius?.let {
            addDropShadow(
                it,
                shadowOffsetX,
                shadowOffsetY,
                shadowColor.colorWithAlphaComponent(shadowColorAlpha)
            )
        }
        setup()
    }


inline fun UIView.roundCorners(
    radius: Double,
    shouldRoundTop: Boolean = true,
    shouldRoundRight: Boolean = true,
    shouldRoundBottom: Boolean = true,
    shouldRoundLeft: Boolean = true,
    shouldRoundTopLeft: Boolean = shouldRoundTop && shouldRoundLeft,
    shouldRoundTopRight: Boolean = shouldRoundTop && shouldRoundRight,
    shouldRoundBottomLeft: Boolean = shouldRoundBottom && shouldRoundLeft,
    shouldRoundBottomRight: Boolean = shouldRoundBottom && shouldRoundRight
) {
    layer.cornerRadius = radius
    val shouldRoundAll =
        shouldRoundTopLeft && shouldRoundTopRight && shouldRoundBottomLeft && shouldRoundBottomRight
    if (!shouldRoundAll) {
        var maskedCorners = 0UL
        if (shouldRoundTopLeft) maskedCorners = maskedCorners or kCALayerMinXMinYCorner
        if (shouldRoundTopRight) maskedCorners = maskedCorners or kCALayerMaxXMinYCorner
        if (shouldRoundBottomLeft) maskedCorners = maskedCorners or kCALayerMinXMaxYCorner
        if (shouldRoundBottomRight) maskedCorners = maskedCorners or kCALayerMaxXMaxYCorner
        layer.maskedCorners = maskedCorners
        layer.masksToBounds = true
    }
}

@OptIn(ExperimentalForeignApi::class)
inline fun UIView.addDropShadow(
    radius: Double = 10.0,
    offsetX: Double = .0,
    offsetY: Double = .0,
    color: UIColor = UIColor.blackColor.colorWithAlphaComponent(.9),
) {
    addSubview(UIView().apply {
        backgroundColor = UIColor.blackColor
        masksToBounds = false
        clipsToBounds = false
        layer.shadowColor = color.CGColor
        layer.shadowOffset = CGSizeMake(offsetX, offsetY)
        layer.shadowRadius = radius
        layer.shadowOpacity = 1f
    })

    bringSubviewToFront(this)
}