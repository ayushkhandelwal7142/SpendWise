# 💸 SpendWise - Smart Daily Expense Tracker

## 📷 Screenshots

<img src = "https://github.com/user-attachments/assets/b635a78b-43ed-4cca-9acd-2112938b7bd8" width = "250" height = "500">
<img src = "https://github.com/user-attachments/assets/df098205-311f-4524-ac72-8188754e6005" width = "250" height = "500">
<img src = "https://github.com/user-attachments/assets/f252849c-9d0c-4d85-aaf9-fbcc2da8d79d" width = "250" height = "500">
<img src = "https://github.com/user-attachments/assets/95d8d060-271b-4f3d-b2c1-e4991c63e067" width = "250" height = "500">
<img src = "https://github.com/user-attachments/assets/40e425c3-e8f9-48bd-9957-93b7ae65ab6c" width = "250" height = "500">

------

A full-featured Jetpack Compose-based Expense Tracking module built for small business owners to easily record, analyze, and export daily expenses.

---

## 📱 Module Overview

This project helps small business owners digitize their daily expenses — which are often unrecorded or spread across WhatsApp messages or paper — into a centralized, user-friendly interface.

Built using Jetpack Compose, MVVM architecture, and optional Room DB, it leverages AI tools to speed up UI design, architecture planning, and implementation.

---

## ✅ Features

### 1. Expense Entry Screen
- Input fields:
    - Title (required)
    - Amount in ₹ (required, must be > 0)
    - Category: Staff, Travel, Food, Utility (mocked dropdown)
    - Optional Notes (max 100 characters)
    - Optional Receipt Image (via file picker)
- Animated submit button
- Real-time “Total Spent Today” display

### 2. Expense List Screen
- View expenses:
    - For Today (default)
    - For previous dates via calendar selector
- Toggle grouping:
    - By category or time
- Show:
    - Total expense count
    - Total amount
    - Empty state UI

### 3. Expense Report Screen
- View mock report of the last 7 days:
    - Daily totals
    - Category-wise totals
    - Bar chart (mocked using Compose)
- Export:
    - Simulated PDF export
    - Share via intent

---

## 🎁 Bonus Features
- Theme toggle (Light / Dark)
- Persisted data with Room DB
- Entry animation on successful add (Lottie animation)
- Input validation (e.g. non-empty title, amount > 0)
- Duplicate detection (basic matching logic)
- Offline-first mock syncing
- Reusable UI components

---

## 🧠 AI Usage Summary

This project was built using an AI-first workflow. Here’s how AI tools were used:

- ChatGPT:
    - Generated and iteratively refined Jetpack Compose UI code.
    - Suggested best practices for MVVM + StateFlow setup.
    - Helped write DAO interfaces, ViewModels, and Repository classes.
    - Provided UX enhancement ideas and animation logic.
    - Assisted with prompt debugging and Lottie integration.

- Copilot (GitHub):
    - Suggested boilerplate Kotlin code and function scaffolding.

- Prompt Examples Used:
    - “How to share a screenshot or export a view to PDF in Compose?”
    - “Jetpack Compose Lottie success animation with fade-in”
    - “Room DAO insertAll query for a list of expenses”

---

## 🏗️ Tech Stack

| Layer            | Library/Framework         |
|------------------|---------------------------|
| UI               | Jetpack Compose           |
| State Mgmt       | ViewModel + StateFlow     |
| Navigation       | Jetpack Compose Navigation|
| Local Storage    | Room DB                   |
| Animations       | Lottie Compose            |
| Charting         | Custom Compose Bar Chart  |
| Share/Export     | FileProvider + PDF APIs   |

---

## 🔧 Setup Instructions

1. Clone the repo:
   ```bash 
   git clone https://github.com/ayushkhandelwal7142/SpendWise.git
