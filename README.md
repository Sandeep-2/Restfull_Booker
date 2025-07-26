```markdown
# Restfull_Booker

📦 **Restfull_Booker** is a comprehensive API automation project built with Java, Rest Assured, and TestNG. It validates and reports RESTful operations on the [https://restful-booker.herokuapp.com](https://restful-booker.herokuapp.com) API, supporting end-to-end CRUD tests, parallel data collection, Excel export, and Allure reporting.

---

## 🚀 Features

- ✅ Health check for API availability
- ✅ Create, Read, Update, Delete (CRUD) booking endpoints
- ✅ Partial updates using PATCH
- ✅ Multi-threaded data collection from all bookings
- ✅ Excel report generation using Apache POI
- ✅ Allure reporting integration
- ✅ Parallel execution support (TestNG)
- ✅ Clean code structure using Page Object-like separation (clients/models)

---

## 🧱 Tech Stack

| Tool/Library        | Purpose                         |
|---------------------|----------------------------------|
| Java 17             | Language                        |
| Maven               | Build & Dependency Management   |
| Rest Assured        | API Testing                     |
| TestNG              | Test Framework                  |
| Allure              | Reporting                       |
| Lombok              | Model simplification            |
| Apache POI          | Excel Export                    |

---

## 🗂️ Project Structure

```

Restfull\_Booker/
│
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       └── java/
│           ├── base/             # Base test config
│           ├── clients/          # API client wrappers
│           ├── config/           # Token/auth helpers
│           ├── models/           # Booking models
│           ├── utils/            # Excel writer utility
│           ├── BookingDataCollector.java
│           └── E2E.java
│
├── testng.xml                  # TestNG suite config
├── pom.xml                     # Maven configuration
├── README.md                   # Project documentation
└── allure-results/             # Raw test output

````

---

## ✅ How to Run the Tests

### 1. Prerequisites

- Java 17+
- Maven 3.6+
- Allure CLI (for reporting)  
  Install via:
  ```bash
  brew install allure        # macOS
  scoop install allure       # Windows
````

---

### 2. Run Test Suite

Run from the root of the project:

```bash
mvn clean test
```

TestNG will run `testng.xml` which includes both:

* `E2E` (end-to-end booking lifecycle test)
* `BookingDataCollector` (Excel export of all bookings)

---

### 3. Generate Allure Report

After tests complete:

```bash
allure generate allure-results --clean -o allure-report
allure open allure-report
```

This opens an interactive dashboard in your default browser.

---

### 4. View Excel Report

Excel file will be created at:

```
src/test/resources/BookingData.xlsx
```

---

## 📊 Sample Report Screenshots

> **(You can upload screenshots of your Allure or Excel reports here once available)**

---

## 🤝 Contribution

Pull requests and feedback are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 🔒 License

This project is licensed under the [MIT License](LICENSE) - feel free to use and modify.

---

## 👤 Author

**Sandeep Surepalli**
[GitHub](https://github.com/Sandeep-2)

---

```

---

Would you like me to:
- Add badges (e.g. build passing, license)?
- Format this as a downloadable `README.md` file?
- Customize the author/link section with your GitHub username?

Let me know!
```
