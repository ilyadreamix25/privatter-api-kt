package com.privatter.api.validation

import com.privatter.api.utility.validate as validateAsException
import com.privatter.api.validation.exception.ValidationException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

typealias KProperty<T> = KProperty1<out T, *>

fun <T : Any> KClass<T>.findValidationProperties(): List<Pair<KProperty<T>, List<Annotation>>> {
    val properties = mutableListOf<Pair<KProperty<T>, List<Annotation>>>()

    this.declaredMemberProperties.forEach continueForEach@{ property ->
        val annotations = property.findValidationAnnotations()
        if (annotations.isEmpty())
            return@continueForEach

        properties.add(property to annotations)
    }

    return properties
}

fun <T : Any> List<Pair<KProperty<T>, List<Annotation>>>.getValidationExceptions(context: T): List<ValidationException> {
    val exceptions = mutableListOf<ValidationException>()

    this.forEach {
        val (property, annotations) = it
        annotations.forEach { annotation ->
            val validationException = property.validate(context, annotation)
            if (validationException != null)
                exceptions.add(validationException)
        }
    }

    return exceptions
}

private fun <T : Any> KProperty<T>.findValidationAnnotations() = this.annotations.filter { annotation ->
    annotation.annotationClass.findAnnotation<ValidationAnnotation>() != null
}

private fun <T : Any> KProperty<T>.validate(context: T, annotation: Annotation): ValidationException? {
    val thisValue = this.call(context)
    if (thisValue !is String)
        throw ValidationException("Unsupported type of ${this.name} (${this::class.simpleName})")

    when (annotation) {
        is IsNotBlank -> return validateAsException(
            thisValue.isNotBlank(),
            "${this.name} must not be blank",
            false
        )

        is IsNotEmpty -> return validateAsException(
            thisValue.isNotEmpty(),
            "${this.name} must not be empty",
            false
        )

        is LengthBetween -> {
            val range = (annotation.min)..(annotation.max)
            return validateAsException(
                thisValue.length in range,
                "${this.name} length must be in $range range",
                false
            )
        }

        is Matches -> return validateAsException(
            thisValue.matches(annotation.regularExpression.toRegex()),
            "${this.name} must match ${annotation.regularExpression}",
            false
        )

        // Most likely it will never happen
        else -> throw ValidationException("Unsupported validation")
    }
}
