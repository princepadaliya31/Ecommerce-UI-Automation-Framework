# Master Test Plan — SauceDemo UI Automation Framework

## 1. Objective
The objective of this Master Test Plan is to define the QA strategy, testing scope, test design methodologies, environment requirements, and automation approach for the [SauceDemo E-Commerce Application](https://www.saucedemo.com/). This framework ensures high application quality, functional correctness, and regression stability across supported web browsers.

---

## 2. Application Overview
SauceDemo is a front-end e-commerce web application hosted by Sauce Labs to demonstrate UI automation testing capabilities. It provides a complete e-commerce user journey including user authentication, product catalog viewing, item sorting, product detail inspection, shopping cart management, customer checkout information submission, order summary calculation, and order completion confirmation.

---

## 3. Scope
This test plan covers manual and automated UI testing of the client-facing SauceDemo web application across authentication, catalog navigation, shopping cart, and multi-step checkout workflows.

---

## 4. In-Scope Functionality
* **Authentication Module:**
  * Login with valid user credentials (`standard_user`, `problem_user`, `performance_glitch_user`, `error_user`, `visual_user`).
  * Negative login handling for locked-out user (`locked_out_user`), invalid credentials, and empty fields.
  * Sidebar navigation and logout functionality.
* **Product Catalog & Inventory Module:**
  * Catalog rendering verifying all 6 default products.
  * Product sorting by Name (A to Z, Z to A) and Price (low to high, high to low).
  * Dynamic cart badge incrementing and decrementing upon adding/removing items.
* **Product Details Module:**
  * Product detail page navigation via item title click.
  * Product name, description, and price accuracy.
  * Direct add-to-cart and remove functionality from product detail view.
* **Cart Management Module:**
  * Cart item inventory validation.
  * Item removal from cart.
  * Navigation between cart, catalog ("Continue Shopping"), and checkout ("Checkout").
* **Checkout Module:**
  * Customer information form validation (First Name, Last Name, Zip/Postal Code mandatory checks).
  * Order summary total verification (Item Subtotal + Tax = Total).
  * Order finish workflow and confirmation screen rendering.
* **End-to-End (E2E) Flow:**
  * Complete purchase lifecycle from login through product selection, cart review, checkout form completion, and order confirmation.

---

## 5. Out-of-Scope Functionality
* User registration and account creation (SauceDemo uses fixed mock user accounts).
* Payment gateway processing or real credit card authorization.
* Email notifications, order confirmation emails, or invoice PDF downloads.
* Backend API validation, database SQL queries, or server log auditing.
* Performance, load, stress, or penetration security testing.

---

## 6. Test Strategy
Testing will follow a structured risk-based QA methodology combining manual test scenario documentation with automated UI test execution. Automated tests are implemented using Java 21, Selenium WebDriver 4, TestNG, and Page Object Model (POM) architecture.

---

## 7. Functional Testing
Functional tests ensure each UI element and feature adheres to functional expectations:
* Form input field validation and error banner rendering.
* Add/Remove button state transitions.
* Navigation links and back buttons.
* Dynamic cart badge count updates.

---

## 8. Smoke Testing
Smoke tests verify critical path sanity prior to running full test suites:
* Login with `standard_user`.
* Load inventory catalog.
* Add a single item to cart.
* Verify cart page navigation.

---

## 9. Regression Testing
Regression test suites execute full functional coverage to prevent defects when code or environment changes occur:
* All negative login edge cases.
* All 4 product sorting variations.
* Product detail page verifications across multiple products.
* Form validation checks for missing checkout fields.

---

## 10. End-to-End Testing
End-to-End (E2E) tests validate the entire user transaction journey:
* User logs in -> selects multiple products -> views cart -> enters shipping details -> verifies order totals -> submits order -> confirms "Thank you for your order!" screen.

---

## 11. Cross-Browser Testing
Automated tests are configured to execute across multiple modern desktop browsers:
* **Google Chrome** (Headless and Headed modes via `--headless=new`).
* **Mozilla Firefox** (Headless and Headed modes via `-headless`).
* **Microsoft Edge** (Headless and Headed modes).

---

## 12. Test Environment
* **Target Application URL:** `https://www.saucedemo.com/`
* **Operating System:** Windows / Linux (GitHub Actions runner `ubuntu-latest`).
* **Java Version:** JDK 21 (Temurin / Oracle JDK).
* **Build Tool:** Apache Maven 3.9+.
* **Browser Driver Engine:** Selenium Manager in Selenium 4.29+.

---

## 13. Test Data
Pre-configured SauceDemo user accounts and static item data:
* **Valid User:** `standard_user` / `secret_sauce`
* **Locked Out User:** `locked_out_user` / `secret_sauce`
* **Problem User:** `problem_user` / `secret_sauce`
* **Performance Glitch User:** `performance_glitch_user` / `secret_sauce`
* **Test Data CSV:** Located in [`src/test/resources/testdata/login-data.csv`](file:///c:/Users/princ/OneDrive/Desktop/QA_UI/src/test/resources/testdata/login-data.csv)

---

## 14. Entry Criteria
* Target application `https://www.saucedemo.com/` is operational and accessible.
* Automation repository compiles cleanly (`mvn clean test-compile`).
* Test data CSV files and configuration properties are in place.

---

## 15. Exit Criteria
* 100% of planned smoke and critical regression test scenarios executed.
* Zero unresolved Blocker or Critical defects.
* Allure execution report generated and archived.

---

## 16. Deliverables
* Master Test Plan (`docs/TestPlan.md`)
* Comprehensive Test Scenarios (`docs/TestScenarios.md`)
* Executable Test Cases CSV (`docs/TestCases.csv`)
* Defect & Bug Log (`docs/BugReports.md`)
* Requirements Traceability Matrix (`docs/RequirementsTraceabilityMatrix.csv`)
* Test Execution Summary (`docs/TestExecutionSummary.md`)
* Senior QA Interview Guide (`docs/InterviewGuide.md`)

---

## 17. Risks
| Risk Description | Impact | Mitigation Strategy |
| :--- | :--- | :--- |
| External SauceDemo site downtime | High | Use retry logic and verify connectivity in `@BeforeMethod`. |
| Network latency causing explicit wait timeouts | Medium | Implement configurable explicit waits via `ConfigReader`. |
| Browser driver compatibility issues | Low | Leverage native Selenium Manager for automatic driver management. |

---

## 18. Assumptions
* SauceDemo application UI locators remain stable.
* Mock user credentials (`standard_user`, `locked_out_user`, `secret_sauce`) remain unchanged.
* Internet connectivity is available during test execution.

---

## 19. Defect Management
* Defects discovered during manual or automated testing are logged with reproduction steps, severity, expected vs. actual outcomes, and screenshots.
* Test failures automatically capture and attach screenshots into Allure reports via `TestListener`.

---

## 20. Automation Strategy
* **Design Pattern:** Page Object Model (POM) separating page element locators from test logic.
* **Thread Safety:** `ThreadLocal<WebDriver>` in `DriverFactory` for thread-safe parallel test execution.
* **Data-Driven Approach:** TestNG `@DataProvider` consuming OpenCSV test datasets.
* **CI Integration:** GitHub Actions workflow executing headless Maven test suites on push/pull request.
