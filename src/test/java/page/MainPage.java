package page;


import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class MainPage {
    private SelenideElement buttonBuy = $(byText("Купить"));
    private SelenideElement buttonBuyByCredit = $(byText("Купить в кредит"));
    private SelenideElement byCard = $(byText("Оплата по карте"));
    private SelenideElement byCredit = $(byText("Кредит по данным карты"));

    public PaymentPage buyWithoutCredit() {
        buttonBuy.click();
        byCard.shouldBe(visible);
        return new PaymentPage();
    }

    public CreditPage buyWithCredit() {
        buttonBuyByCredit.click();
        byCredit.shouldBe(visible);
        return new CreditPage();
    }
}