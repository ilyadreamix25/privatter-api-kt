package com.privatter.api.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(private val mailSender: JavaMailSender) {
    fun sendMail(to: String, subject: String, body: String) {
        val message = SimpleMailMessage().apply {
            setTo(to)
            setSubject(subject)
            text = body
        }

        mailSender.send(message)
    }
}
