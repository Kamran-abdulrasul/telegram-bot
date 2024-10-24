package com.example.telegram_bot.controller;

import com.example.telegram_bot.GetPaymentDto;
import com.example.telegram_bot.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.xlsx";
        response.setHeader(headerKey, headerValue);

        List<GetPaymentDto> listPayments = paymentService.getPayments();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("ID");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Amount");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Message Text");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Date");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Status");

        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("From Chat Id");

        int rowCount = 1;

        for (GetPaymentDto payments : listPayments) {
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(payments.getId());

            cell = row.createCell(1);
            cell.setCellValue(String.valueOf(payments.getAmount()));

            cell = row.createCell(2);
            cell.setCellValue(payments.getMessageText());

            cell = row.createCell(3);
            cell.setCellValue(payments.getDateTime());

            cell = row.createCell(4);
            cell.setCellValue(payments.getStatus());

            cell = row.createCell(5);
            cell.setCellValue(payments.getId());
       }

//            try (OutputStream outputStream = response.getOutputStream()) {
//                workbook.write(outputStream);
//
//                workbook.close();
//            }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();


        }
    }