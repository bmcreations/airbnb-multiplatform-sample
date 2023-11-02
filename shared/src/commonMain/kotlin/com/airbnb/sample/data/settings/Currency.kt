package com.airbnb.sample.data.settings

import kotlinx.serialization.Serializable

@Suppress("EnumEntryName")
@Serializable
enum class Currency(
    val code: String,
    val symbol: String,
) {
    Australian_dollar("AUD", "$"),
    Brazilian_real("BRL", "R$"),
    Bulgarian_lev("BGN", "лв."),
    Canadian_dollar("CAD", "$"),
    Chilean_peso("CLP", "$"),
    Chinese_yuan("CNY", "¥"),
    Colombian_peso("COP", "$"),
    Costa_Rican_colon("CRC", "₡"),
    Croatian_kuna("HRK", "kn"),
    Czech_koruna("CZK", "Kč"),
    Danish_krone("DKK", "Kr."),
    Emirati_dirham("AED", "د.إ"),
    Euro("EUR", "Є"),
    Hong_Kong_dollar("HKD", "$"),
    Hungarian_fotrint("HUF", "Ft"),
    Indian_rupee("INR", "₹"),
    Indonesian_rupiah("IDR", "Rp"),
    Israeli_new_shekel("ILS", "₪"),
    Japanese_yen("JPY", "¥"),
    Malaysian_ringgit("MYR", "RM"),
    Mexican_peso("MXN", "$"),
    Moroccan_dirham("MAD", "MAD"),
    New_Taiwan_dollar("TWD", "$"),
    New_Zealand_dollar("NZD", "$"),
    Norwegian_krone("NOK", "kr"),
    Peruvian_sol("PEN", "S/"),
    Philippine_peso("PHP", "₱"),
    Polish_zloty("PLN", "zł"),
    Pound_sterling("GBP", "£"),
    Romanian_leu("RON", "lei"),
    Saudi_Arabian_riyal("SAR", "SR"),
    Singapore_dollar("SGD", "$"),
    South_African_rand("ZAR", "R"),
    South_Korean_won("KRW", "₩"),
    Swedish_krona("SEK", "kr"),
    Swiss_franc("CHF", "CHf"),
    Thai_baht("THB", "฿"),
    Turkish_lira("TRY", "₺"),
    United_States_dollar("USD", "$"),
    Uruguayan_peso("UYU", "\$U");

    fun displayName(): String = name.replace("_", " ")

    companion object {

        fun findWithSymbol(symbol: String): Currency? {
            return entries.firstOrNull { it.symbol == symbol }
        }

        fun findWithCode(code: String): Currency? {
            return entries.firstOrNull { it.code == code }
        }
    }
}

