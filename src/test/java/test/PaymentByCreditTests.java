package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataBaseHelper;
import data.DataHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPage;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


public class PaymentByCreditTests {

    private  WebDriver driver;

    MainPage mainPage;
    CreditPage creditPage;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--no-sandbox");
        // options.addArguments("--headless");
        // driver = new ChromeDriver(options);
        WebDriverManager.chromedriver().setup();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void shouldCleanDataBaseAndOpenWeb() {
        // driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        DataBaseHelper.cleanDataBase();
        mainPage = open("http://localhost:8080", MainPage.class);
        creditPage = mainPage.buyWithCredit();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @DisplayName("Покупка тура с вводом валидных данных картой со статусом approved")
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

    @DisplayName("Отказ в покупке тура при вводе невалидных данных")
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

    // Проверка поля "Номер"

    @DisplayName("Отправка пустой формы поля номер карты")
    @Test//#3
    void shouldRejectEmptyNumberCard() {
        var cardNumber = DataHelper.getEmptyValue();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты текста")
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

    @DisplayName("Покупка тура при вводе в поле номер карты неполных данных")
    @Test//№5
    void checkingIncompleteData() { /*Покупка тура при вводе неполных данных*/
        var cardNumber = DataHelper.getCardNumberIncomplete();
        var month = DataHelper.getIncompleteMonth();
        var year = DataHelper.getIncompleteYear();
        var owner = DataHelper.getIncompleteOwner();
        var cvc = DataHelper.getIncompleteCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitNecessaryFillOutField();
    }

    @DisplayName("Покупка тура при вводе в поле номер карты нулей")
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

    @DisplayName("Покупка тура при вводе в поле номер карты одинаковых цифр")
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

    @DisplayName("Покупка тура при вводе в поле номер карты только 4 цифр")
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

    @DisplayName("Покупка тура при вводе в поле номер карты 17 цифр")
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

    // Проверка поля "Дата"

    @DisplayName("Отправка пустой формы поля месяц")
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

    @DisplayName("Отправка пустой формы поля год")
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

    @DisplayName("Ввод невалидного значения в поле месяц")
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

    @DisplayName("Покупка тура при вводе в поле месяц нулей")
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

    @DisplayName("Покупка тура при вводе в поле месяц букв")
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

    @DisplayName("Покупка тура при вводе в поле месяц более 2х цифр")
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

    @DisplayName("Ввод невалидного значения в поле год")
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

    @DisplayName("Покупка тура при вводе в поле год нулей")
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

    @DisplayName("Покупка тура при вводе в поле год букв")
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

    @DisplayName("Покупка тура при вводе в поле год более 2х цифр")
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

    // Проверка поля "Имя"

    @DisplayName("Ввод невалидного значения в поле имя")
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

    @DisplayName("Отправка пустой формы поля имя")
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

    @DisplayName("Покупка тура при вводе в поле имя цифр")
    @Test //№22
    void checkingOwnerWithNumbers() { /*Покупка тура при вводе цифр в поле владелец*/
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerNumbers();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    @DisplayName("Покупка тура при вводе в поле имя букв в нижнем регистре")
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

    @DisplayName("Покупка тура при вводе в поле имя кириллицы")
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

    @DisplayName("Покупка тура при вводе в поле имени и фамилии владельца без пробелов")
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

    @DisplayName("Покупка тура при вводе в поле имя одной буквы")
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

    @DisplayName("Покупка тура при вводе в поле имя имени из 20 букв")
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

    @DisplayName("Покупка тура при вводе в поле имя только фамилии")
    @Test //№28
    void checkingOwnerOnlySurname() { /*Покупка тура при вводе только фамилии в поле владельца*/
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerOnlySurname();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutFields(cardNumber, month, year, owner, cvc);
        creditPage.waitInvalidFormat();
    }

    // Проверка поля "CVC"

    @DisplayName("Ввод невалидного значения в поле CVC")
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

    @DisplayName("Отправка пустой формы поля CVC")
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

    @DisplayName("Покупка тура при вводе в поле CVC нулей")
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

    @DisplayName("Покупка тура при вводе в поле CVC текста")
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

    @DisplayName("Покупка тура при вводе в поле CVC более 3х цифр")
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