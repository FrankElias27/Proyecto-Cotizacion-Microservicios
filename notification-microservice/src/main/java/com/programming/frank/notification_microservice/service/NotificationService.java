package com.programming.frank.notification_microservice.service;

import com.programming.frank.notification_microservice.dto.Report;
import com.programming.frank.quotation_microservice.event.QuotationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    private final ReportService reportService;

    @KafkaListener(topics = "quotation")
    public void listen(QuotationEvent quotationEvent){
        log.info("Got Message from quotation topic {}", quotationEvent);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("cotizacion@webpageica.com");
            messageHelper.setTo(quotationEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Your Quotaion is placed successfully", quotationEvent.getSubject()));
            messageHelper.setText(String.format("""
                            Estimado cliente,
                                           
                            Nos complace informarle que la cotización titulada **"%s"** ha sido enviada con éxito y se encuentra adjunta a este correo.
                                           
                            Si tiene alguna consulta adicional o desea realizar modificaciones, no dude en contactarnos.
                                           
                            Saludos cordiales, \s
                            **Sistema de Cotización**
                            """,
                    quotationEvent.getSubject()));


            Map<String, Object> params = new HashMap<>();
            params.put("cotizacion", quotationEvent.getQuotationId().toString());
            params.put("tipo", "PDF");

            Report report = reportService.getReportQuotation(params);
            if (report == null || report.getStream() == null) {
                log.error("Report or report stream is null. No attachment will be added.");
            } else {
                log.info("Report generated: {}", report.getFileName());
            }



            InputStream is = report.getStream();
            byte[] buffer = is.readAllBytes();
            log.info("Report stream actual size in bytes: {}", buffer.length);


            InputStreamSource attachment = () -> new ByteArrayInputStream(buffer);


            messageHelper.addAttachment(report.getFileName(), attachment);

        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Quotation Notification email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }
    }
}

