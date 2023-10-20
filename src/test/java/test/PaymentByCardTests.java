package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataBaseHelper;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PaymentByCardTests {

    MainPage mainPage;
    PaymentPage paymentPage;

    @BeforeEach
    void shouldOpenWeb() {
        DataBaseHelper.cleanDataBase();
        mainPage = open("http://localhost:8080", MainPage.class);
        paymentPage = mainPage.buyWithoutCredit();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @DisplayName("Покупка тура с вводом валидных данных картой со статусом approved")
    @Test // №1
    void shouldApproveFirstCard() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.expectApprovalFromBank();
        var expected = DataHelper.getFirstCardExpectedStatus();
        var actual = DataBaseHelper.getStatusPaymentWithoutCredit();
        assertEquals(expected, actual);
    }

    @DisplayName("Отказ в покупке тура при вводе невалидных данных")
    @Test //№2
    void shouldRejectSecondCard() {
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.expectRejectionFromBank();
        var expected = DataHelper.getSecondCardExpectedStatus();
        var actual = DataBaseHelper.getStatusPaymentWithoutCredit();
        Assertions.assertEquals(expected,actual);
    }

    @DisplayName("Отправка пустой формы поля номер карты")
    @Test //#3
    void shouldRejectEmptyNumberCard() {
        var cardNumber = DataHelper.getEmptyValue();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты текста")
    @Test //№4
    void checkingCardWithText() {
        var cardNumber = DataHelper.getValueText();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты неполных данных")
    @Test //№5
    void checkingIncompleteData() { /*Покупка тура при вводе неполных данных*/
        var cardNumber = DataHelper.getCardNumberIncomplete();
        var month = DataHelper.getIncompleteMonth();
        var year = DataHelper.getIncompleteYear();
        var owner = DataHelper.getIncompleteOwner();
        var cvc = DataHelper.getIncompleteCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты нулей")
    @Test //№6
    void checkingCardNumberZero() {
        var cardNumber = DataHelper.getCardNumberZero();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты одинаковых цифр")
    @Test //№7
    void checkingCardNumberRequestedData() {
        var cardNumber = DataHelper.getCardNumberNotExisting();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.expectRejectionFromBank();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты только 4 цифр")
    @Test //№8
    void checkingCardNumberUnderLimit() {
        var cardNumber = DataHelper.getCardNumberUnderLimit();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты 17 цифр")
    @Test
    void checkingCardNumberOverLimit() {
        var cardNumber = DataHelper.getCardNumberOverLimit();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutFields(cardNumber, month, year, owner, cvc);
        paymentPage.expectApprovalFromBank();
    }
}
