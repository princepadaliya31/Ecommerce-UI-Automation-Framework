# Test Scenarios — SauceDemo QA Automation Framework

This document outlines all manual and automated test scenarios across the SauceDemo application modules.

---

## 1. Authentication & Login Module
* **TS_LOGIN_001:** Verify user can log in successfully with valid credentials (`standard_user` / `secret_sauce`).
* **TS_LOGIN_002:** Verify error message banner when attempting login with locked-out user credentials (`locked_out_user`).
* **TS_LOGIN_003:** Verify error message banner when attempting login with invalid username/password.
* **TS_LOGIN_004:** Verify error message banner when username field is left blank.
* **TS_LOGIN_005:** Verify error message banner when password field is left blank.

---

## 2. Product Catalog & Inventory Module
* **TS_INV_001:** Verify inventory catalog displays exactly 6 products upon successful login.
* **TS_INV_002:** Verify product item cards display valid title, description, price, and "Add to cart" button.
* **TS_INV_003:** Verify clicking "Add to cart" on an item changes button state to "Remove" and increments cart badge count.
* **TS_INV_004:** Verify clicking "Remove" on an inventory item decrements cart badge count and resets button state.

---

## 3. Product Details Module
* **TS_PD_001:** Verify clicking a product title in inventory navigates to individual Product Details page.
* **TS_PD_002:** Verify Product Details page displays matching product title, description, and price.
* **TS_PD_003:** Verify user can add product to cart directly from Product Details page.
* **TS_PD_004:** Verify clicking "Back to products" button returns user to main Inventory page.

---

## 4. Product Sorting Module
* **TS_SORT_001:** Verify sorting inventory products by Name (A to Z) orders items alphabetically.
* **TS_SORT_002:** Verify sorting inventory products by Name (Z to A) orders items in reverse alphabetical order.
* **TS_SORT_003:** Verify sorting inventory products by Price (low to high) orders items in ascending price value.
* **TS_SORT_004:** Verify sorting inventory products by Price (high to low) orders items in descending price value.

---

## 5. Shopping Cart Module
* **TS_CART_001:** Verify navigating to Shopping Cart page displays all selected items with correct quantities and prices.
* **TS_CART_002:** Verify removing an item from Shopping Cart page updates cart list and badge counter.
* **TS_CART_003:** Verify clicking "Continue Shopping" button navigates user back to Inventory catalog.
* **TS_CART_004:** Verify clicking "Checkout" button navigates user to Checkout Step One (Information) page.

---

## 6. Checkout Module
* **TS_CHK_001:** Verify error message banner when First Name is omitted during checkout information submission.
* **TS_CHK_002:** Verify error message banner when Last Name is omitted during checkout information submission.
* **TS_CHK_003:** Verify error message banner when Zip/Postal Code is omitted during checkout information submission.
* **TS_CHK_004:** Verify Checkout Step Two (Overview) page displays selected items, payment info ("SauceCard #31337"), and shipping info ("Free Pony Express Delivery!").
* **TS_CHK_005:** Verify price calculation accuracy on Overview page (Subtotal + Tax = Total).
* **TS_CHK_006:** Verify clicking "Cancel" on Checkout Information page returns user to Shopping Cart.
* **TS_CHK_007:** Verify clicking "Cancel" on Checkout Overview page returns user to Inventory catalog.

---

## 7. Logout & Session Management Module
* **TS_LOGOUT_001:** Verify clicking sidebar menu button opens sidebar with navigation links.
* **TS_LOGOUT_002:** Verify clicking "Logout" link terminates session and redirects user to Login page.

---

## 8. End-to-End (E2E) Purchasing Flow Module
* **TS_E2E_001:** Verify complete end-to-end purchase lifecycle from login, catalog selection, cart validation, customer info entry, price review, order placement, to final "Thank you for your order!" confirmation screen.
