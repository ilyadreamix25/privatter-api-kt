package com.privatter.api.validation

import org.intellij.lang.annotations.RegExp

annotation class ValidationAnnotation

@ValidationAnnotation
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsNotBlank

@ValidationAnnotation
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class IsNotEmpty

@ValidationAnnotation
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class LengthBetween(val min: Int, val max: Int)

@ValidationAnnotation
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Matches(@RegExp val regularExpression: String)
