# Bug Reports & Defect Analysis Log

## 1. Execution Failure Classification & Analysis

All test execution failures encountered during automation framework development were analyzed and classified according to standard QA engineering categories:

| Execution Event / Failure | Classification Category | Root Cause & Resolution |
| :--- | :--- | :--- |
| Cart badge count assertion failure on 0 items | **TEST EXPECTATION ERROR** | Cart badge element is dynamically unmounted from DOM by React when count = 0. Resolved with predicate `waitForCartBadgeCount`. |
| OpenCSV parameter count mismatch in login CSV | **FRAMEWORK ERROR** | Comma inside error message string parsed as extra column. Resolved by enclosing strings in double quotes in `login-data.csv`. |
| Checkout navigation timeout to step-two | **SYNCHRONIZATION ERROR** | Selenium `sendKeys` left Control modifier key depressed on React inputs. Resolved using `Keys.chord(Keys.CONTROL, "a")` and explicit URL transition waits. |
| Edge browser driver initialization failure | **TEST ENVIRONMENT ERROR** | Remote Selenium Manager driver endpoint for Edge v154 (`msedgedriver.azureedge.net`) unavailable on host network. |

---

## 2. Confirmed Application Defects Statement

**No confirmed application defects were identified during the documented execution.**

All 52 automated production tests (`LoginTest`, `InventoryTest`, `ProductDetailsTest`, `CartTest`, `CheckoutTest`, `EndToEndTest`) passed with a **100% success rate** on standard application workflows on SauceDemo.

---

## 3. SAMPLE BUG REPORT — INTERVIEW PRACTICE ONLY

> [!IMPORTANT]
> **DISCLAIMER:** The bug report below is a sample demonstration created strictly for interview practice, defect management demonstration, and portfolio assessment purposes. It does NOT represent an actual application defect on standard_user production execution.

### Bug ID: BUG-SAMPLE-001
* **Bug Title:** `problem_user` Account Renders Duplicate Placeholder Images Across Product Catalog
* **Module:** Inventory / Product Catalog
* **Environment:** Windows 11 / Chrome 154.0 (Headless & Headed) / SauceDemo v4.0
* **Preconditions:** Application base URL https://www.saucedemo.com/ is accessible.
* **Steps to Reproduce:**
  1. Open browser and navigate to `https://www.saucedemo.com/`
  2. Enter Username: `problem_user`
  3. Enter Password: `secret_sauce`
  4. Click the `Login` button.
  5. Inspect the `src` attribute of all 6 product image elements on the `/inventory.html` catalog page.
* **Test Data:** Username: `problem_user`, Password: `secret_sauce`
* **Expected Result:** Every product item displays its respective unique product image (e.g., `sauce-backpack-1200x1500.jpg`, `bike-light-1200x1500.jpg`).
* **Actual Result:** All 6 product cards render the identical fallback dog placeholder image URL (`/static/media/sl-404.168b1cce.jpg`).
* **Severity:** Medium (Visual / UI functionality degradation)
* **Priority:** P2 (High Priority for UX consistency)
* **Status:** Open (Sample)
* **Screenshot / Evidence:** Captured image source URL mismatch: `Expected: /static/media/sauce-backpack-1200x1500.jpg | Actual: /static/media/sl-404.168b1cce.jpg`
