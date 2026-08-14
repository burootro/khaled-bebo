package com.modark.market

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/* ─────── ظهور متدرّج للكروت ─────── */
@Composable
fun Reveal(index: Int, content: @Composable () -> Unit) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 65L)
        show = true
    }
    val a by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(430, easing = FastOutSlowInEasing),
        label = "reveal"
    )
    Box(
        Modifier.graphicsLayer {
            alpha = a
            translationY = (1f - a) * 30f
        }
    ) { content() }
}

/* ─────── رقم بيعدّ من صفر ─────── */
@Composable
fun CountUp(
    target: Double,
    size: Int,
    color: Color,
    weight: FontWeight = FontWeight.SemiBold,
    modifier: Modifier = Modifier
) {
    var go by remember { mutableStateOf(false) }
    val v by animateFloatAsState(
        targetValue = if (go) target.toFloat() else 0f,
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "count"
    )
    LaunchedEffect(Unit) { go = true }
    Text(
        money(v.toDouble()),
        color = color,
        fontSize = size.sp,
        fontWeight = weight,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/* ─────── الرقم الكبير بهالة نابضة ─────── */
@Composable
fun GlowAmount(amount: Double, caption: String) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val glow by pulse.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.62f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "glow"
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(130.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(220.dp, 90.dp)
                .blur(46.dp)
                .background(C.Brass.copy(alpha = glow), CircleShape)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(caption, color = C.Muted, fontSize = 11.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(4.dp))
            CountUp(amount, 42, C.Paper, FontWeight.Bold)
            Text("جنيه", color = C.BrassSoft, fontSize = 12.sp)
        }
    }
}

/* ─────── حلقة نسبة التحصيل ─────── */
@Composable
fun ProgressRing(percent: Float, label: String, sub: String) {
    var go by remember { mutableStateOf(false) }
    val sweep by animateFloatAsState(
        targetValue = if (go) percent * 360f else 0f,
        animationSpec = tween(1600, easing = FastOutSlowInEasing),
        label = "ring"
    )
    LaunchedEffect(Unit) { go = true }

    Box(Modifier.size(104.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0x14FFFFFF),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 9f, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(C.Brass, C.BrassSoft, C.Brass)),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = 9f, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = C.Paper, fontSize = 19.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(sub, color = C.Muted, fontSize = 9.sp)
        }
    }
}

/* ─────── كارت ─────── */
@Composable
fun Panel(
    accent: Color = C.Line,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
            .background(C.Card)
            .padding(16.dp),
        content = content
    )
}

@Composable
fun SectionTitle(text: String, trailing: String = "") {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = C.Muted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(Modifier.weight(1f), color = C.Line)
        if (trailing.isNotEmpty()) {
            Spacer(Modifier.width(10.dp))
            Text(trailing, color = C.BrassSoft, fontSize = 11.sp)
        }
    }
}

/* ─────── شارة حالة ─────── */
@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.13f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50))
            .padding(horizontal = 9.dp, vertical = 3.dp)
    ) {
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

/* ─────── كارت إحصائية صغير ─────── */
@Composable
fun MiniStat(
    icon: String,
    label: String,
    value: Double,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(listOf(color.copy(alpha = 0.10f), Color(0x04FFFFFF)))
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Text(icon, fontSize = 17.sp)
        Spacer(Modifier.height(7.dp))
        Text(label, color = C.Muted, fontSize = 10.5.sp)
        CountUp(value, 17, color)
    }
}

/* ─────── زرار واتساب ─────── */
@Composable
fun WhatsAppButton(phone: String, message: String, small: Boolean = true) {
    val ctx = LocalContext.current
    Box(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(C.Green.copy(alpha = 0.14f))
            .border(1.dp, C.Green.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .clickable {
                val url = "https://wa.me/$phone?text=" + Uri.encode(message)
                runCatching {
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            .padding(
                horizontal = if (small) 10.dp else 16.dp,
                vertical = if (small) 5.dp else 10.dp
            )
    ) {
        Text(
            "واتساب",
            color = C.Green,
            fontSize = if (small) 11.sp else 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
