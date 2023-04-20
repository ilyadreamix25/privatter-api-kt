package com.privatter.api.validation

import org.intellij.lang.annotations.Language

annotation class ValidationAnnotation

@ValidationAnnotation
@Target(AnnotationTarget.PROPERTY)
annotation class IsNotBlank

@ValidationAnnotation
@Target(AnnotationTarget.PROPERTY)
annotation class IsNotEmpty

@ValidationAnnotation
@Target(AnnotationTarget.PROPERTY)
annotation class LengthBetween(val min: Int, val max: Int)

@ValidationAnnotation
@Target(AnnotationTarget.PROPERTY)
annotation class Matches(@Language("RegExp") val regularExpression: String)
