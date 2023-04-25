package com.privatter.api.v1.utility

fun Exception.beautifyStackTrace(doFilter: Boolean = false) = this.stackTrace.toList()
    .map { element ->
        element.toString().replace(", ", "\n")
    }
    .filter { element ->
        doFilter && "com.privatter" in element
    }
