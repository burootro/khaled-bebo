package com.modark.market

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.roundToLong

object C {
    val Ink = Color(0xFF080F12)
    val Card = Color(0xFF0F1D21)
    val Line = Color(0xFF1F3A38)
    val Brass = Color(0xFFC9922A)
    val BrassSoft = Color(0xFFE8C173)
    val Red = Color(0xFFE0685F)
    val Green = Color(0xFF49C69A)
    val Vodafone = Color(0xFFE60000)
    val Cheque = Color(0xFF6FA8DC)
    val Paper = Color(0xFFEDE8DC)
    val Muted = Color(0xFF7D9498)
}

fun money(v: Double): String {
    val n = v.roundToLong()
    val s = abs(n).toString().reversed().chunked(3).joinToString(",").reversed()
    return if (n < 0) "−$s" else s
}
