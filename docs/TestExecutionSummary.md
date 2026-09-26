# Test Execution & Cross-Browser Summary Report

* **Framework:** Ecommerce-UI-Automation-Framework
* **Application Under Test:** https://www.saucedemo.com/
* **Test Engine:** TestNG 7.11 with Selenium 4.29 (Selenium Manager)
* **Report Engine:** Allure TestNG 2.29 + SLF4J / Logback Logging

---

## Executive Test Run Metrics

| Module | Executed | Passed | Failed | Skipped | Pass Rate |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Authentication / Login (`LoginTest`)** | 14 | 14 | 0 | 0 | 100% |
| **Inventory & Catalog (`InventoryTest`)** | 9 | 9 | 0 | 0 | 100% |
| **Product Details (`ProductDetailsTest`)** | 2 | 2 | 0 | 0 | 100% |
| **Shopping Cart (`CartTest`)** | 11 | 11 | 0 | 0 | 100% |
| **Checkout Flow (`CheckoutTest`)** | 14 | 14 | 0 | 0 | 100% |
| **End-to-End Journey (`EndToEndTest`)** | 1 | 1 | 0 | 0 | 100% |
| **Total Framework Executions** | **51** | **51** | **0** | **0** | **100%** |

---

## Cross-Browser & Execution Grid Matrix

| Browser | Execution Mode | Environment Availability | Status | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Google Chrome** | Local / Headless | Installed & Verified | **PASSED (100%)** | Native Selenium Manager driver resolution |
| **Mozilla Firefox** | Local / Headless | Not Installed | **Not executed — browser unavailable** | Configured in `DriverFactory` via `FirefoxOptions` |
| **Microsoft Edge** | Local / Headless | Driver Download Error | **Not executed — browser unavailable** | Remote driver download endpoint v154 unavailable on host |
| **Selenium Grid** | RemoteWebDriver | Optional Fallback | **Configured** | Activated via `-Dgrid.enabled=true -Dgrid.url=...` |

---

## Command Reference

### 1. Local Browser Execution
```powershell
# Default Chrome execution (Headless)
mvn clean test -Dbrowser=chrome -Dheadless=true

# Chrome Headed Mode
mvn test -Dbrowser=chrome -Dheadless=false

# Specific Test Groups
mvn test -Dgroups=smoke -Dheadless=true
mvn test -Dgroups=regression -Dheadless=true
mvn test -Dgroups=e2e -Dheadless=true
```

### 2. Optional Selenium Grid Execution
```powershell
# Connect to Remote Selenium Grid Hub (e.g. Docker container or standalone hub)
mvn test -Dgrid.enabled=true -Dgrid.url=http://localhost:4444/ -Dbrowser=chrome -Dheadless=true
```

### 3. Allure Report Generation
```powershell
# Serve interactive report in browser
allure serve target/allure-results

# Generate static HTML report
mvn allure:report
```
