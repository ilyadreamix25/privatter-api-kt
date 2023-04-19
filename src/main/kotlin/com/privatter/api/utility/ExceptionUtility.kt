package com.privatter.api.utility

fun Exception.beautify(doFilter: Boolean = false) = this.stackTrace.toList()
    .map { element ->
        element.toString().replace(", ", "\n")
    }
    .filter { element ->
        doFilter && "com.privatter" in element
    }
