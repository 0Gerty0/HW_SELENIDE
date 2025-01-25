import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static java.time.Duration.ofSeconds;

public class CardDeliveryTest {

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.browserSize = "1920x1080";
    }

    @Test
    void shouldSubmitRequestSuccessfully() {
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        open("/");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).shouldBe(visible, ofSeconds(15)).click();

        $("[data-test-id=notification]")
                .shouldBe(visible, ofSeconds(15))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + planningDate));
    }
}
