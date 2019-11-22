package com.hackday.utils

import java.text.DecimalFormat

fun insertComma(num: Int): String = DecimalFormat("#,###").format(num)