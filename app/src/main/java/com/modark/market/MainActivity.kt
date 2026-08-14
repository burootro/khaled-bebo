package com.modark.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = C.Ink, surface = C.Card)) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AppRoot()
                }
            }
        }
    }
}

@Composable
fun AppRoot() {
    var tab by remember { mutableIntStateOf(0) }
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            SideMenu { picked ->
                scope.launch { drawer.close() }
                if (picked in 0..3) tab = picked
            }
        }
    ) {
        Scaffold(
            containerColor = C.Ink,
            bottomBar = { BottomBar(tab) { tab = it } }
        ) { pad ->
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF11242A), C.Ink),
                            endY = 700f
                        )
                    )
                    .padding(pad)
            ) {
                TopBar { scope.launch { drawer.open() } }
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 26.dp)
                ) {
                    key(tab) {
                        when (tab) {
                            0 -> HomeScreen { tab = it }
                            1 -> DebtScreen()
                            2 -> VodafoneScreen()
                            3 -> ChequeScreen()
                        }
                    }
                }
            }
        }
    }
}

/* ══════════ الهيدر ══════════ */
@Composable
fun TopBar(onMenu: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .border(1.dp, C.Line, RoundedCornerShape(11.dp))
                .clickable { onMenu() },
            contentAlignment = Alignment.Center
        ) { Text("☰", color = C.BrassSoft, fontSize = 16.sp) }

        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("خالد بيبو", color = C.Paper, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text("دفتر الحسابات · ١٤ أغسطس", color = C.Muted, fontSize = 10.5.sp)
        }
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .border(1.dp, C.Line, RoundedCornerShape(11.dp)),
            contentAlignment = Alignment.Center
        ) { Text("🔔", fontSize = 14.sp) }
    }
}

/* ══════════ القائمة الجانبية ══════════ */
@Composable
fun SideMenu(onPick: (Int) -> Unit) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF0C181C),
        drawerContentColor = C.Paper,
        modifier = Modifier.width(285.dp)
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            Spacer(Modifier.height(28.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(C.BrassSoft, Color(0xFF8A6416)))),
                    contentAlignment = Alignment.Center
                ) { Text("خ", color = C.Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("خالد بيبو", color = C.Paper, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("صاحب المحل", color = C.Muted, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(20.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(C.Brass.copy(alpha = 0.10f))
                    .border(1.dp, C.Brass.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Text("إجمالي فلوسك برة", color = C.Muted, fontSize = 10.5.sp)
                CountUp(grandTotal, 22, C.BrassSoft, FontWeight.Bold)
            }

            Spacer(Modifier.height(18.dp))
            MenuItem("📊", "الرئيسية") { onPick(0) }
            MenuItem("📕", "الديون") { onPick(1) }
            MenuItem("📱", "فودافون كاش") { onPick(2) }
            MenuItem("🧾", "الشيكات") { onPick(3) }

            HorizontalDivider(Modifier.padding(vertical = 14.dp), color = C.Line)

            MenuItem("💰", "الخزنة") { onPick(9) }
            MenuItem("📈", "تقارير الشهر") { onPick(9) }
            MenuItem("💾", "نسخة احتياطية") { onPick(9) }
            MenuItem("🔒", "قفل بالبصمة") { onPick(9) }
            MenuItem("⚙️", "الإعدادات") { onPick(9) }

            Spacer(Modifier.weight(1f))
            Text("الإصدار 0.2.0", color = C.Muted, fontSize = 10.sp)
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
fun MenuItem(icon: String, label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 15.sp)
        Spacer(Modifier.width(13.dp))
        Text(label, color = C.Paper, fontSize = 14.sp)
    }
}

/* ══════════ الرئيسية ══════════ */
@Composable
fun HomeScreen(goTo: (Int) -> Unit) {
    Reveal(0) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, C.Brass.copy(alpha = 0.28f), RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(listOf(Color(0x18C9922A), Color(0x04FFFFFF)))
                )
                .padding(vertical = 14.dp)
        ) {
            GlowAmount(grandTotal, "إجمالي فلوسك برة")

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressRing(
                    percent = (collectedThisMonth / (collectedThisMonth + grandTotal)).toFloat(),
                    label = "${((collectedThisMonth / (collectedThisMonth + grandTotal)) * 100).toInt()}%",
                    sub = "تحصيل الشهر"
                )
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("حصّلت الشهر ده", color = C.Muted, fontSize = 11.sp)
                    CountUp(collectedThisMonth, 20, C.Green)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${debtors.size + transfers.size + cheques.size} حساب مفتوح",
                        color = C.Muted,
                        fontSize = 11.sp
                    )
                    Text(
                        "${debtors.count { it.days > 30 }} متأخر عن ٣٠ يوم",
                        color = C.Red,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))
    Reveal(1) {
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            MiniStat("📕", "ديون العملاء", totalDebt, C.Red, Modifier.weight(1f)) { goTo(1) }
            MiniStat("📱", "فودافون كاش", totalVodafone, C.Vodafone, Modifier.weight(1f)) { goTo(2) }
            MiniStat("🧾", "شيكات", totalCheques, C.Cheque, Modifier.weight(1f)) { goTo(3) }
        }
    }

    SectionTitle("محتاج تحرك النهارده")

    debtors.filter { it.days > 20 }.forEachIndexed { i, d ->
        Reveal(i + 2) {
            AlertCard(
                "${d.name} متأخر ${d.days} يوم",
                "عليه ${money(d.amount)} جنيه",
                C.Red,
                d.phone,
                "السلام عليكم، فاكر إن باقي ليك عندنا ${money(d.amount)} جنيه. تحب نتحاسب امتى؟"
            )
        }
    }
    transfers.filter { it.state == 2 }.forEachIndexed { i, t ->
        Reveal(i + 4) {
            AlertCard(
                "تحويل ${t.name} لسه معلّق",
                "${money(t.amount)} جنيه · ${t.ago}",
                C.Vodafone,
                t.phone,
                "لو سمحت، التحويل بتاع ${money(t.amount)} جنيه لسه موصلش. ممكن تبعتلي صورة الرسالة؟"
            )
        }
    }
    cheques.filter { it.state == 2 }.forEachIndexed { i, c ->
        Reveal(i + 6) {
            AlertCard(
                "شيك ${c.payer} ارتد",
                "${money(c.amount)} جنيه · ${c.bank}",
                C.Cheque,
                "",
                ""
            )
        }
    }
}

@Composable
fun AlertCard(title: String, note: String, color: Color, phone: String, msg: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(15.dp))
            .background(color.copy(alpha = 0.06f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = C.Paper, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(note, color = C.Muted, fontSize = 11.sp)
        }
        if (phone.isNotEmpty()) WhatsAppButton(phone, msg)
    }
}

/* ══════════ الديون ══════════ */
@Composable
fun DebtScreen() {
    SectionTitle("ديون العملاء", "${money(totalDebt)} ج")
    debtors.sortedByDescending { it.amount }.forEachIndexed { i, d ->
        Reveal(i) {
            Panel(accent = if (d.amount > d.limit) C.Red else C.Line) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(d.name.first().toString(), C.Red)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(d.name, color = C.Paper, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
                        Text(d.note, color = C.Muted, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            money(d.amount),
                            color = C.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text("جنيه", color = C.Muted, fontSize = 9.5.sp)
                    }
                }
                Spacer(Modifier.height(11.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (d.amount > d.limit) StatusChip("عدّى الحد", C.Red)
                    else StatusChip("داخل الحد", C.Green)
                    Spacer(Modifier.width(7.dp))
                    StatusChip("${d.days} يوم", if (d.days > 30) C.Red else C.Muted)
                    Spacer(Modifier.weight(1f))
                    WhatsAppButton(
                        d.phone,
                        "السلام عليكم ${d.name}، باقي ليك عندنا ${money(d.amount)} جنيه. تحب نتحاسب امتى؟"
                    )
                    Spacer(Modifier.width(7.dp))
                    ActionPill("سدد", C.Brass)
                }
            }
        }
        Spacer(Modifier.height(9.dp))
    }
}

/* ══════════ فودافون كاش ══════════ */
@Composable
fun VodafoneScreen() {
    SectionTitle("فودافون كاش — تحويلات لسه معلّقة", "${money(totalVodafone)} ج")

    Reveal(0) {
        Panel(accent = C.Vodafone) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("معلّق دلوقتي", color = C.Muted, fontSize = 11.sp)
                    CountUp(totalVodafone, 26, C.Vodafone, FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${transfers.count { it.state == 2 }} متأخر", color = C.Red, fontSize = 12.sp)
                    Text("${transfers.count { it.state == 1 }} في الانتظار", color = C.Muted, fontSize = 12.sp)
                    Text("${transfers.count { it.state == 0 }} وصل", color = C.Green, fontSize = 12.sp)
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))
    transfers.sortedByDescending { it.state }.forEachIndexed { i, t ->
        Reveal(i + 1) {
            Panel(accent = if (t.state == 2) C.Vodafone else C.Line) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar("📱", C.Vodafone)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(t.name, color = C.Paper, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${t.phone.drop(2)} · ${t.ago}",
                            color = C.Muted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        money(t.amount),
                        color = if (t.state == 0) C.Green else C.Vodafone,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(Modifier.height(11.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when (t.state) {
                        2 -> StatusChip("متأخر — كلّمه", C.Red)
                        1 -> StatusChip("في الانتظار", C.Brass)
                        else -> StatusChip("وصل ✓", C.Green)
                    }
                    Spacer(Modifier.weight(1f))
                    if (t.state != 0) {
                        WhatsAppButton(
                            t.phone,
                            "لو سمحت، تحويل الـ ${money(t.amount)} جنيه لسه موصلش. ممكن تبعتلي صورة الرسالة؟"
                        )
                        Spacer(Modifier.width(7.dp))
                        ActionPill("وصل", C.Green)
                    }
                }
            }
        }
        Spacer(Modifier.height(9.dp))
    }
}

/* ══════════ الشيكات ══════════ */
@Composable
fun ChequeScreen() {
    SectionTitle("الشيكات", "${money(totalCheques)} ج")
    cheques.sortedBy { it.dueIn }.forEachIndexed { i, c ->
        val color = when {
            c.state == 2 -> C.Red
            c.dueIn <= 7 -> C.Brass
            else -> C.Cheque
        }
        Reveal(i) {
            Panel(accent = color) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(c.payer, color = C.Paper, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
                        Text("${c.bank} · شيك رقم ${c.no}", color = C.Muted, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            money(c.amount),
                            color = color,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text("جنيه", color = C.Muted, fontSize = 9.5.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))
                val progress = ((30 - c.dueIn).coerceIn(0, 30)) / 30f
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0x12FFFFFF))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(progress.coerceAtLeast(0.04f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(color)
                    )
                }

                Spacer(Modifier.height(11.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when {
                        c.state == 2 -> StatusChip("ارتد — راجع البنك", C.Red)
                        c.dueIn <= 0 -> StatusChip("استحق النهارده", C.Brass)
                        c.dueIn <= 7 -> StatusChip("باقي ${c.dueIn} أيام", C.Brass)
                        else -> StatusChip("باقي ${c.dueIn} يوم", C.Cheque)
                    }
                    Spacer(Modifier.weight(1f))
                    ActionPill("اتصرف", C.Green)
                    Spacer(Modifier.width(7.dp))
                    ActionPill("تفاصيل", C.Muted)
                }
            }
        }
        Spacer(Modifier.height(9.dp))
    }
}

/* ══════════ عناصر مشتركة ══════════ */
@Composable
fun Avatar(text: String, color: Color) {
    Box(
        Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(13.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActionPill(label: String, color: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.13f))
            .border(1.dp, color.copy(alpha = 0.32f), RoundedCornerShape(10.dp))
            .clickable { }
            .padding(horizontal = 11.dp, vertical = 5.dp)
    ) {
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

/* ══════════ الشريط السفلي ══════════ */
@Composable
fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    val items = listOf(
        "📊" to "الرئيسية",
        "📕" to "الديون",
        "📱" to "فودافون",
        "🧾" to "الشيكات"
    )
    NavigationBar(containerColor = Color(0xF2080F12), tonalElevation = 0.dp) {
        items.forEachIndexed { i, (icon, label) ->
            NavigationBarItem(
                selected = selected == i,
                onClick = { onSelect(i) },
                icon = { Text(icon, fontSize = 17.sp) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = C.BrassSoft,
                    unselectedTextColor = C.Muted,
                    indicatorColor = C.Brass.copy(alpha = 0.16f)
                )
            )
        }
    }
}
