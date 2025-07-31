# Spendee - Personal Expense Management App

[![Android](https://img.shields.io/badge/Android-API%2019+-green.svg)](https://developer.android.com/about/versions/android-4.4)
[![Firebase](https://img.shields.io/badge/Firebase-Authentication%20%26%20Database-orange.svg)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Spendee is a comprehensive personal expense management Android application that helps users track their daily, weekly, and monthly expenses with detailed analytics and budget management features.

## 🚀 Features

### 📊 **Expense Tracking**
- **Daily Spending**: Track expenses for the current day
- **Weekly Spending**: Monitor expenses over a week period
- **Monthly Spending**: View monthly expense summaries
- **Category-based Tracking**: Organize expenses into 9 categories:
  - 🚗 Transport
  - 🍽️ Food
  - 🏠 House
  - 🎬 Entertainment
  - 📚 Education
  - 👕 Clothes
  - 🏥 Health
  - 👤 Personal
  - 📦 Other

### 💰 **Budget Management**
- Set monthly budget limits
- Track budget vs actual spending
- Visual budget progress indicators
- Budget alerts and notifications

### 📈 **Analytics & Reports**
- **Daily Analytics**: Detailed breakdown of daily spending by category
- **Weekly Analytics**: Weekly spending patterns and trends
- **Monthly Analytics**: Comprehensive monthly expense analysis
- **Visual Charts**: Interactive pie charts and graphs using MPAndroidChart and AnyChart
- **Spending Ratios**: Compare spending across different categories

### 🔐 **User Authentication**
- Firebase Authentication integration
- User registration and login
- Password reset functionality
- Secure user data storage

### 📱 **User Interface**
- Modern Material Design UI
- Navigation drawer for easy access
- Floating Action Button (FAB) for quick expense entry
- Responsive design for different screen sizes
- Dark/Light theme support

## 🛠️ Technical Stack

### **Frontend**
- **Language**: Java
- **Minimum SDK**: API 19 (Android 4.4)
- **Target SDK**: API 30 (Android 11)
- **UI Framework**: AndroidX with Material Design components

### **Backend & Database**
- **Firebase Authentication**: User management and security
- **Firebase Realtime Database**: Real-time data synchronization
- **Firebase Analytics**: Usage tracking and insights

### **Libraries & Dependencies**
- **Firebase UI**: `com.firebaseui:firebase-ui-database:6.2.1`
- **Charts**: 
  - `com.github.PhilJay:MPAndroidChart:v3.1.0`
  - `com.github.AnyChart:AnyChart-Android:1.1.2`
- **Image Loading**: `com.github.bumptech.glide:glide:4.11.0`
- **Date/Time**: `joda-time:joda-time:2.10.10`
- **UI Components**: Material Design components

## 📋 Prerequisites

Before running this application, make sure you have:

- **Android Studio** (latest version recommended)
- **Android SDK** (API 19 or higher)
- **Google Services** account for Firebase integration
- **Firebase Project** with Authentication and Realtime Database enabled

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/VedantDeoree/Spendee.git
cd Spendee
```

### 2. Firebase Setup
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Authentication (Email/Password)
3. Enable Realtime Database
4. Download `google-services.json` and place it in the `app/` directory
5. Update Firebase rules for database access

### 3. Build and Run
1. Open the project in Android Studio
2. Sync Gradle files
3. Connect an Android device or start an emulator
4. Click "Run" to build and install the app

## 📱 App Structure

### **Activities**
- `SplashScreenActivity`: App launch screen
- `StartActivity`: Initial welcome screen
- `LoginActivity`: User authentication
- `RegisterActivity`: New user registration
- `MainActivity`: Main dashboard with navigation
- `TodaySpendingActivity`: Daily expense tracking
- `WeekSpendingActivity`: Weekly expense management
- `BudgetActivity`: Budget setting and management
- `DailyAnalyticsActivity`: Daily spending analytics
- `WeeklyAnalyticsActivity`: Weekly analytics
- `MonthlyAnalyticsActivity`: Monthly spending analysis
- `HistoryActivity`: Expense history and records
- `OverviewActivity`: Financial overview
- `AccountActivity`: User account management
- `AboutUsActivity`: App information

### **Data Model**
- `Data.java`: Main data class for expense items
  - Item name, date, amount, notes
  - Category classification
  - Time-based grouping (day/week/month)

## 🎯 Key Features Explained

### **Expense Entry**
- Add expenses with category selection
- Include notes and descriptions
- Real-time data synchronization
- Offline capability with sync when online

### **Analytics Dashboard**
- Visual representation of spending patterns
- Category-wise expense breakdown
- Spending trends over time
- Budget vs actual spending comparison

### **Budget Management**
- Set monthly budget limits
- Track spending against budget
- Receive alerts when approaching limits
- Budget adjustment capabilities

## 🔒 Security Features

- **Firebase Authentication**: Secure user login/logout
- **Data Encryption**: Firebase handles data security
- **User Isolation**: Each user's data is isolated
- **Secure Database Rules**: Firebase security rules implementation

## 📊 Database Schema

### **Users Collection**
```
/users/{userId}/
  - email: string
  - displayName: string
  - createdAt: timestamp
```

### **Expenses Collection**
```
/Expenses/{userId}/{expenseId}/
  - item: string
  - amount: number
  - date: string
  - note: string
  - category: string
  - itemNday: string
  - itemNweek: string
  - itemNmonth: string
```

### **Budget Collection**
```
/Budget/{userId}/{budgetItemId}/
  - item: string
  - amount: number
  - date: string
  - note: string
```

## 🚀 Future Enhancements

- [ ] Export data to CSV/PDF
- [ ] Multiple currency support
- [ ] Receipt image upload
- [ ] Expense reminders
- [ ] Bill payment tracking
- [ ] Investment tracking
- [ ] Financial goals setting
- [ ] Advanced reporting features

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Vedant Deore**
- GitHub: [@VedantDeoree](https://github.com/VedantDeoree)

## 🙏 Acknowledgments

- Original project by [Nandini Deore](https://github.com/NandiniDeore05)
- Firebase for backend services
- MPAndroidChart and AnyChart for visualization
- Material Design for UI components

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/VedantDeoree/Spendee/issues) page
2. Create a new issue with detailed description
3. Include device information and error logs

---

**Spendee** - Take control of your finances, one expense at a time! 💰📱 