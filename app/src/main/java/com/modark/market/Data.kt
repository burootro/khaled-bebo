package com.modark.market

/* 0 = تمام | 1 = قرب | 2 = متأخر */

data class Debtor(
    val name: String,
    val phone: String,
    val note: String,
    val amount: Double,
    val days: Int,
    val limit: Double
)

data class Transfer(
    val name: String,
    val phone: String,
    val amount: Double,
    val ago: String,
    val state: Int
)

data class Cheque(
    val payer: String,
    val bank: String,
    val no: String,
    val amount: Double,
    val dueIn: Int,
    val state: Int
)

val debtors = listOf(
    Debtor("عم رضا — عمارة ٧", "201012345671", "آخر سداد من ٢٤ يوم", 3400.0, 24, 3000.0),
    Debtor("كافيه الركن", "201012345672", "حساب شهري · يسدد ١ الشهر", 2240.0, 11, 5000.0),
    Debtor("أم محمد", "201012345673", "آخر سداد من ٩ أيام", 1850.0, 9, 2000.0),
    Debtor("سعيد السباك", "201012345674", "آخر سداد امبارح", 460.0, 1, 1500.0),
    Debtor("ورشة الحاج فتحي", "201012345675", "بياخد كل خميس", 1290.0, 6, 3000.0),
    Debtor("محل الكوافير", "201012345676", "متأخرة من ٤٠ يوم", 980.0, 40, 1000.0),
)

val transfers = listOf(
    Transfer("محمد الجزار", "201112345671", 2500.0, "من ٥ أيام", 2),
    Transfer("شركة المياه", "201112345672", 1800.0, "من يومين", 1),
    Transfer("أحمد الخضري", "201112345673", 950.0, "امبارح", 1),
    Transfer("عم سيد", "201112345674", 600.0, "النهارده", 0),
    Transfer("مكتب الشحن", "201112345675", 3200.0, "من ٩ أيام", 2),
)

val cheques = listOf(
    Cheque("كافيه الركن", "بنك مصر", "٤٤٢١٩٠", 8500.0, 3, 1),
    Cheque("سوبر ماركت النور", "الأهلي", "١٩٨٧٣٤", 12000.0, 12, 0),
    Cheque("ورشة الحاج فتحي", "CIB", "٧٧١٢٠٥", 4300.0, -2, 2),
    Cheque("محل الأدوات", "QNB", "٣٣٤٥٦٧", 6200.0, 21, 0),
)

val totalDebt = debtors.sumOf { it.amount }
val totalVodafone = transfers.filter { it.state != 0 }.sumOf { it.amount }
val totalCheques = cheques.filter { it.state != 0 }.sumOf { it.amount }
val grandTotal = totalDebt + totalVodafone + totalCheques
val collectedThisMonth = 18600.0
