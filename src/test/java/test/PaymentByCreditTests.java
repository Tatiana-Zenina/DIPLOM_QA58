package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataBaseHelper;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPage;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


public class PaymentByCreditTests {

    MainPage mainPage;
    CreditPage creditPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void shouldCleanDataBaseAndOpenWeb() {
        DataBaseHelper.cleanDataBase();
        mainPage = open("http://localhost:8080", MainPage.class);
        creditPage = mainPage.buyWithCredit();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @DisplayName("Покупка тура в кредит с вводом валидных данных (картой со статусом APPROVED)")
    @Test// №1
    void shouldApproveFirstCard() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.expectApprovalFromBank();
        var expected = DataHelper.getFirstCardExpectedStatus();
        var actual = DataBaseHelper.getStatusPaymentWithCredit();
        assertEquals(expected, actual);
    }

    @DisplayName("Отказ в покупке тура в кредит при вводе невалидных данных (карты со статусом DECLINED)")
    @Test//№2
    void shouldRejectSecondCard() {
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.expectRejectionFromBank();
        var expected = DataHelper.getSecondCardExpectedStatus();
        var actual = DataBaseHelper.getStatusPaymentWithCredit();
        Assertions.assertEquals(expected, actual);
    }

    // Проверка поля "НОМЕР КАРТЫ"

    @DisplayName("Отправка пустой формы поля НОМЕР КАРТЫ при покупке тура в кредит")
    @Test//№3
    void shouldRejectEmptyNumberCard() {
        var cardNumber = DataHelper.getEmptyValue();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ текста")
    @Test//№4
    void checkingCardWithText() {
        var cardNumber = DataHelper.getValueText();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ неполных данных")
    @Test//№5
    void checkingIncompleteData() {
        var cardNumber = DataHelper.getCardNumberIncomplete();
        var month = DataHelper.getIncompleteMonth();
        var year = DataHelper.getIncompleteYear();
        var owner = DataHelper.getIncompleteOwner();
        var cvc = DataHelper.getIncompleteCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ нулей")
    @Test//№6
    void checkingCardNumberZero() {
        var cardNumber = DataHelper.getCardNumberZero();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ одинаковых цифр")
    @Test//№7
    void checkingCardNumberRequestedData() {
        var cardNumber = DataHelper.getCardNumberNotExisting();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.expectRejectionFromBank();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ только 4 цифр")
    @Test//№8
    void checkingCardNumberUnderLimit() {
        var cardNumber = DataHelper.getCardNumberUnderLimit();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле НОМЕР КАРТЫ 17 цифр")
    @Test//№9
    void checkingCardNumberOverLimit() {
        var cardNumber = DataHelper.getCardNumberOverLimit();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.expectApprovalFromBank();
    }

    // Проверка полей "ДАТА"

    @DisplayName("Отправка пустой формы поля МЕСЯЦ при покупке тура в кредит")
    @Test//№10
    void shouldRejectEmptyMonth() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyValue();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Отправка пустой формы поля ГОД при покупке тура в кредит")
    @Test//№11
    void shouldRejectEmptyYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getEmptyValue();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Ввод невалидного значения в поле МЕСЯЦ при покупке тура в кредит")
    @Test//№12
    void shouldRejectInvalidMonth() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getInvalidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidDuration();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле МЕСЯЦ нулей")
    @Test//№13
    void shouldRejectZeroMonth() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getZeroValue();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле МЕСЯЦ букв")
    @Test//№14
    void checkingMonthWithText() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValueText();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле МЕСЯЦ более 2х цифр")
    @Test//№15
    void checkingMonthOverLimit() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getDurationOverLimit();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidDuration();
    }

    @DisplayName("Ввод невалидного значения в поле ГОД при покупке тура в кредит")
    @Test//№16
    void shouldRejectInvalidYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getInvalidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidYear();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ГОД нулей")
    @Test//№17
    void shouldRejectZeroYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getZeroValue();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ГОД букв")
    @Test//№18
    void checkingYearWithText() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValueText();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ГОД более 2х цифр")
    @Test //№19
    void checkingYearOverLimit() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getDurationOverLimit();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    // Проверка поля "ВЛАДЕЛЕЦ"

    @DisplayName("Ввод невалидного значения в поле ВЛАДЕЛЕЦ при покупке тура в кредит")
    @Test //№20
    void checkingOwnerInvalid() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getInvalidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Отправка пустой формы поля ВЛАДЕЛЕЦ при покупке тура в кредит")
    @Test //№21
    void shouldRejectEmptyOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getEmptyValue();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ цифр")
    @Test //№22
    void checkingOwnerWithNumbers() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerNumbers();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ букв в нижнем регистре")
    @Test //№23
    void checkingOwnerLowerCase() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerLowerCase();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ кириллицы")
    @Test //№24
    void checkingOwnerRus() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getInvalidOwnerRus();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ имени и фамилии без пробелов")
    @Test //№25
    void checkingOwnerWithoutSpaces() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getDataWithoutSpaces();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ одной буквы")
    @Test //№26
    void checkingOwnerUnderLimit() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getUnderLimitOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ имени из 20 букв")
    @Test //№27
    void checkingOwnerOverLimit() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOverLimitOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле ВЛАДЕЛЕЦ только фамилии")
    @Test //№28
    void checkingOwnerOnlySurname() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerOnlySurname();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    // Проверка поля "CVC/CVV"

    @DisplayName("Ввод невалидного значения в поле CVC/CVV при покупке тура в кредит")
    @Test //№29
    void shouldRejectInvalidCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getInvalidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Отправка пустой формы поля CVC/CVV при покупке тура в кредит")
    @Test //№30
    void shouldRejectEmptyCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getEmptyValue();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле CVC/CVV нулей")
    @Test //№31
    void shouldRejectZeroCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getZeroCvv();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле CVC/CVV текста")
    @Test //№32
    void checkingCVVWithText() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValueText();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура в кредит при вводе в поле CVC/CVV более 3х цифр")
    @Test //№33
    void checkingCVVOverLimit() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getCvcOverLimit();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

}