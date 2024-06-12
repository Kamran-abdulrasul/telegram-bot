package com.example.telegram_bot.service;


import com.example.telegram_bot.GetPaymentDto;
import com.example.telegram_bot.SavePaymentDto;
import com.example.telegram_bot.config.TelegramBotConfig;
import com.example.telegram_bot.repository.PermissionRepository;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SendDocument;
import jakarta.ws.rs.NotFoundException;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinAllChatMessages;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
@Service
@Slf4j
@RequiredArgsConstructor
@Setter
@Data
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;

    private final PermissionRepository permissionRepository;

    private final PaymentService paymentService;


    @Override
    public String getBotUsername() {

        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText() && checkChatId(update.getMessage().getChatId())) {
            String messageText = update.getMessage().getText();
            String[] keyword = messageText.split(" ", 2);
            Long chat_id = update.getMessage().getChatId();
            Message message = update.getMessage();


            switch (keyword[0]) {

                case "/payment":

                    try {
                        messageText = keyword[1];
                        paymentService.savePayment(processPaymentMessage(messageText), update.getMessage().getFrom().getId(), Long.valueOf(update.getMessage().getMessageId()));
                        sendMessage(update.getMessage().getChatId(), "Odenis qeyde alindi");
                    } catch (Exception e) {
                        sendMessage(update.getMessage().getChatId(), "FORMAT IS NOT CORRECT");
                        sendMessage(update.getMessage().getChatId(), "FORMAT: $100, Transaction ID: 123456");
                    }
                    break;
                case "/received":
                    try {

                        paymentService.confirmPayment(Long.valueOf(message.getReplyToMessage().getMessageId()));
                        sendMessage(update.getMessage().getChatId(), "Tesdiq qeyde alindi");
                    } catch (RuntimeException e) {
                        sendMessage(update.getMessage().getChatId(), "Odenis tesdiq edilmedi");
                    }
                    break;
                case "/getpayments":
                    try {
                        sendExcelFile(update.getMessage().getChatId());
                    } catch (RuntimeException e) {
                        sendMessage(update.getMessage().getChatId(), "Balans yoxdu");
                    }
                    break;

                default:
                    break;
            }

        } else {
            sendMessage(update.getMessage().getChatId(), "SOMETHING IS WRONG");
        }
    }


    private void sendExcelFile(Long chatId) {
        try {
            URL url = new URL("http://localhost:9095/v1/payments/export/excel");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();

            SendDocument sendDocumentRequest = new SendDocument(chatId, String.valueOf(inputStream));
            sendDocumentRequest.caption("payments.xlsx");

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String textToSend) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);


        try {
            execute(message);
        } catch (TelegramApiException e) {


        }
    }

    private List<Integer> extractNumbers(String message) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+(,\\d{3})*(\\\\.\\\\d+)?(e-?\\\\d+)?"); // Regular expression to match numbers
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            numbers.add(number);
        }

        return numbers;

    }


//    @Override
//    public void onUpdateReceived(Update update) {
//
//        if (update.hasMessage() && update.getMessage().hasText() && checkChatId(update.getMessage().getChatId())) {
//            String messageText = update.getMessage().getText();
//            //   char[] arrayTest = Long.toString(update.getMessage().getChatId()).toCharArray();
//
//            List<Integer> numbers = extractNumbers(messageText);
//            HashMap<String, List> value = new HashMap<>();
//            value.put(messageText, numbers);
//            System.out.println(numbers);
//
//            // paymentService.savePayment(processPaymentMessage(messageText), update.getMessage().getFrom().getId());
//
//
//            // Save numbers to the database
//            //  saveNumbersToDatabase(numbers);
//
//
//        }
//    }

    private SavePaymentDto processPaymentMessage(String messageText) throws RuntimeException {
        //  $100, Transaction ID: 123456
        SavePaymentDto dto = null;
        String[] parts = messageText.split(",");
        if (parts.length == 2) {
            String currency = Arrays.toString(parts[0].split(" ", 1));
            String amountStr = parts[0].replace("$", "");
            String messageTxt = parts[1];
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountStr));
            dto = SavePaymentDto.builder()
                    .amount(amount)
                    .messageText(messageTxt)
                    .currency(currency)
                    .build();
        }
        return dto;

    }

    public boolean checkChatId(Long chatId) {
        var result = permissionRepository.findById(chatId).orElseThrow(() ->
                new NotFoundException("NOT FOUND"));
        if (result != null) {
            return true;
        } else {
            return false;
        }

    }


}

