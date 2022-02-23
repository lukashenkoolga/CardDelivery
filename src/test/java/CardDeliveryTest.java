import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    @BeforeEach
    public void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1600x900";
        open("http://localhost:9999");
    }


    @Test
    public void shouldSentForm () {
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id = phone] input").setValue("+79887776655");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + date),
                        Duration.ofSeconds(15));

    }

    @Test
    public void shouldSentInvalidCity () {
        $("[data-test-id=city] input").setValue("Тайшет");
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id = phone] input").setValue("+79887776655");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));

    }

    @Test
    public void shouldSentInvalidData () {
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id = date] input").doubleClick().sendKeys("12.01.2021");
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id = phone] input").setValue("+79887776655");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));

    }

    @Test
    public void shouldSentInvalidName () {
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Ivanov Ivan");
        $("[data-test-id = phone] input").setValue("+79887776655");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }

    @Test
    public void shouldSentInvalidPhone () {
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id= name] input").setValue("Иванов Иван");
        $("[data-test-id = phone] input").setValue("89887776655");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));

    }

    @Test
    public void shouldSentInactiveCheckbox () {
        $("[data-test-id=city] input").setValue("Иркутск");
        $("[data-test-id = date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(date);
        $("[data-test-id= name] input").setValue("Иванов Иван");
        $("[data-test-id = phone] input").setValue("+79887776655");
        $(".button").click();
        $("[data-test-id=agreement]")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));

    }
}