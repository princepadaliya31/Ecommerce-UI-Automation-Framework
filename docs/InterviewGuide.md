# Senior QA Automation Interview Guide & Framework Architecture Q&A

This guide explains key architectural and technical design decisions made in the `Ecommerce-UI-Automation-Framework` for senior technical interview preparation.

---

### Q1: Why did you choose Page Object Model (POM) and ThreadLocal DriverFactory?
* **Page Object Model (POM):** Decouples element locators and page interaction methods from test logic. If an element locator changes on SauceDemo (e.g. `login-button`), we update it once in `LoginPage` without touching any test classes.
* **ThreadLocal DriverFactory:** Ensures each executing thread gets its own isolated instance of `WebDriver`. This eliminates race conditions during parallel test execution (`parallel="methods"` in `testng.xml`).

---

### Q2: How does browser binary management work without `WebDriverManager`?
* Selenium 4 introduced native **Selenium Manager**. It inspects the local machine environment (e.g., Google Chrome installation at `C:\Program Files\Google\Chrome\Application\chrome.exe`), downloads matching driver binaries automatically if needed, and manages them silently without external third-party dependencies.

---

### Q3: How do you handle configuration overrides for CI/CD?
* Through `ConfigReader.java`, we prioritize System properties (`System.getProperty("browser")`) over values in `config.properties`.
* This enables developers and CI pipelines to override execution parameters at runtime without modifying code:
  ```bash
  mvn test -Dbrowser=firefox -Dheadless=true -DexplicitWait=15
  ```

---

### Q4: How does failure handling and Allure screenshot attachment work?
* We implemented `TestListener` which implements `ITestListener`.
* On test failure (`onTestFailure`), the listener extracts the current thread's `WebDriver` instance from `DriverFactory.getDriver()`.
* It calls `ScreenshotUtils.saveScreenshotToAllure()` decorated with `@Attachment(value = "{0}", type = "image/png")` which automatically attaches the failure screenshot PNG directly into the generated Allure Report.
