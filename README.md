# 🍽️ Bite Rush

A modern, full-featured food ordering app built with **Jetpack Compose** and **Firebase**.

---

## 📸 App Preview

<!-- Add screenshots here -->
![Login Screen]<img width="406" alt="Screenshot 2025-07-03 at 1 05 51 AM" src="https://github.com/user-attachments/assets/af40eefb-f033-4211-a22f-145414994382" />

![Menu Screen]<img width="406" alt="Screenshot 2025-07-03 at 1 06 35 AM" src="https://github.com/user-attachments/assets/45c9e90f-f0be-4de1-a4ec-265478f0a06e" />

![Cart Screen]<img width="406" alt="Screenshot 2025-07-03 at 1 06 57 AM" src="https://github.com/user-attachments/assets/390f011e-1f06-4073-8adb-ab1c89e5911e" />

<img width="406" alt="Screenshot 2025-07-03 at 1 07 14 AM" src="https://github.com/user-attachments/assets/e3053dbc-73bd-4676-a455-0712eb89e38a" />
<img width="406" alt="Screenshot 2025-07-03 at 1 07 24 AM" src="https://github.com/user-attachments/assets/fd267de2-025e-462a-9fec-77fa936c4d75" />

---

## ✨ Features

### 🔐 User Authentication
- Login screen with **email and password validation**
- “Remember Me” functionality
- **Password visibility toggle**
- **Forgot password** option for account recovery

---

### 🎨 Dynamic Theming
- Support for **light and dark mode**
- Dynamic color theming for **Android 12+**

---

### 🧭 Navigation
- Smooth navigation between **Login**, **Menu**, and **Order** screens
- **Back stack management** for proper navigation flow
- Logout functionality redirects to the Login screen

---

### 🍕 Menu Management
- Display of **categorized menu items** (e.g., Pizza, Burger, Pasta)
- **Search bar** to filter menu items by name
- Category filters for quick navigation
- Tags for **popular**, **vegetarian**, and **spicy** items

---

### 🛒 Cart Management
- Add, remove, and update item quantities
- Real-time **cart summary** with item count and total price
- **Clear cart** button to reset selections

---

### 📦 Order Placement
- Delivery address input with proper **validation**
- Option to add **special instructions** for orders
- Support for **multiple payment methods**:
  - Credit Card
  - Debit Card
  - Cash on Delivery
  - Digital Wallet
- Order summary including:
  - Item total
  - Tax
  - Delivery fee
  - Discount
- Estimated **delivery time calculation**

---

### 🎁 Promotions and Discounts
- **Free delivery** for orders above a certain threshold
- **Auto-applied discounts** for large orders

---

### 🧑‍🎨 UI/UX Enhancements
- Beautiful gradient backgrounds and **Material 3 design**
- Smooth **animations** for cart updates
- Floating Action Button (FAB) for quick order placement

---

### 🔥 Firebase Integration
- Configured via `google-services.json`
- Backend services powered by **Firebase Authentication** and **Realtime Database**

---

### 🛠️ Extensibility
- Modular **navigation structure** for easy addition of new screens
  - Profile
  - Order History
- Helper functions for **navigation** and **state management**

---

## 📂 Project Structure

```bash
├── MainActivity.kt
├── navigation/
├── screens/
│   ├── login/
│   ├── menu/
│   ├── cart/
│   ├── order/
├── viewmodels/
├── models/
├── repository/
├── utils/
