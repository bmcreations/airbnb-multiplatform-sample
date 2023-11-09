package com.airbnb.sample.utils.uikit

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.copy
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIEdgeInsetsInsetRect
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UILabel
import platform.darwin.NSInteger

@ExperimentalForeignApi
class UIPaddingLabel(rect: CValue<CGRect>) : UILabel(rect) {

    constructor(): this(CGRectZero.readValue())

    private var textEdgeInsets = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
        set(value) {
            field = value
            invalidateIntrinsicContentSize()
        }

    override fun textRectForBounds(
        bounds: CValue<CGRect>,
        limitedToNumberOfLines: NSInteger
    ): CValue<CGRect> {
        val insetRect = UIEdgeInsetsInsetRect(
            bounds,
            textEdgeInsets
        )
        val textRect = super.textRectForBounds(insetRect, limitedToNumberOfLines)
        val invertedInsets = textEdgeInsets.useContents {
            UIEdgeInsetsMake(
                top = -top,
                left = -left,
                right = -right,
                bottom = -bottom
            )
        }


        return UIEdgeInsetsInsetRect(textRect, invertedInsets)
    }

    override fun drawTextInRect(rect: CValue<CGRect>) {
        super.drawTextInRect(
            UIEdgeInsetsInsetRect(
                rect,
                textEdgeInsets
            )
        )
    }


    var paddingLeft: CGFloat
        get() = textEdgeInsets.useContents { return left }
        set(value) {
            textEdgeInsets = textEdgeInsets.copy { left = value }
        }

    var paddingRight: CGFloat
        get() = textEdgeInsets.useContents { return right }
        set(value) {
            textEdgeInsets = textEdgeInsets.copy { right = value }
        }

    var paddingTop: CGFloat
        get() = textEdgeInsets.useContents { return top }
        set(value) {
            textEdgeInsets = textEdgeInsets.copy { top = value }
        }

    var paddingBottom: CGFloat
        get() = textEdgeInsets.useContents { return bottom }
        set(value) {
            textEdgeInsets = textEdgeInsets.copy { bottom = value }
        }
}