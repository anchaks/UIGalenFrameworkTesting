# Selenium Automation Framework

A comprehensive Selenium with Java automation framework using PageFactory, TestNG, Galen Framework, ExtentReports with base64 screenshots, and Log4j2 logging.

## 🚀 Features

- **Page Object Model**: Clean and maintainable page objects using PageFactory pattern
- **TestNG Integration**: Powerful testing framework with parallel execution support
- **Galen Framework**: Responsive layout testing for cross-browser UI validation
- **ExtentReports**: Beautiful HTML reports with base64 encoded screenshots
- **Log4j2 Logging**: Comprehensive logging with file rotation and multiple levels
- **Multi-browser Support**: Chrome, Firefox, and Edge browsers
- **WebDriver Management**: Automatic driver setup using WebDriverManager
- **Configuration Management**: Externalized configuration via properties files

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox/Edge browser installed

## 🛠️ Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/anchaks/UIGalenFrameworkTesting.git
   cd UIGalenFramework
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

## ⚙️ Configuration

### Browser Configuration
Edit `src/test/resources/config.properties`:

```properties
# Browser Configuration
browser=chrome
headless=false

# Application URLs
base.url=https://practicetestautomation.com/practice-test-login/

# Test Credentials
valid.username=student
valid.password=Password123
```

### Supported Browsers
- `chrome` - Google Chrome
- `firefox` - Mozilla Firefox
- `edge` - Microsoft Edge

## 🏃‍♂️ Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```

### Run with Different Browser
```bash
mvn test -Dbrowser=firefox
```

### Run in Headless Mode
```bash
mvn test -Dheadless=true
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run with Parallel Execution
```bash
mvn test -DthreadCount=3
```

## 📊 Reports

### ExtentReports
- **Location**: `test-output/extent-reports/`
- **Format**: HTML with embedded base64 screenshots
- **Features**: 
  - Test execution summary
  - Step-by-step execution details
  - Screenshots on failures
  - System information
  - Test categorization

### Galen Layout Reports
- **Location**: `target/galen-reports/`
- **Format**: HTML with layout validation results
- **Features**:
  - Visual layout validation
  - Cross-device responsive testing
  - Element positioning verification

### Logs
- **Location**: `logs/automation.log`
- **Format**: Structured logging with timestamps
- **Features**:
  - Multiple log levels (INFO, DEBUG, ERROR, WARN)
  - Log file rotation
  - Console and file output

## 📁 Project Structure

```
selenium-automation-framework/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/automation/
│   │           ├── pages/
│   │           │   ├── BasePage.java
│   │           │   ├── LoginPage.java
│   │           │   └── SuccessPage.java
│   │           └── utils/
│   │               ├── ConfigReader.java
│   │               ├── DriverManager.java
│   │               ├── ExtentReportManager.java
│   │               └── ScreenshotUtils.java
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── base/
│       │       │   └── BaseTest.java
│       │       └── tests/
│       │           ├── LoginTest.java
│       │           └── GalenLayoutTest.java
│       └── resources/
│           ├── config.properties
│           ├── log4j2.xml
│           ├── testng.xml
│           └── galen-specs/
│               └── login-page.gspec
├── test-output/
│   ├── extent-reports/
│   └── screenshots/
├── logs/
├── target/
├── pom.xml
├── .gitignore
└── README.md
```

## 🧪 Test Cases

### Login Tests (`LoginTest.java`)
- ✅ Valid login with correct credentials
- ✅ Invalid login with wrong credentials
- ✅ Login page elements verification
- ✅ Error message validation

### Layout Tests (`GalenLayoutTest.java`)
- ✅ Desktop layout validation (1024x768)
- ✅ Tablet layout validation (768x1024)
- ✅ Mobile layout validation (375x667)
- ✅ Responsive element positioning

## 🔧 Key Components

### BasePage
- Common web element interactions
- WebDriver wait implementations
- Logging integration
- PageFactory initialization

### BaseTest
- WebDriver setup/teardown
- ExtentReports integration
- Screenshot capture on failures
- Test lifecycle management

### DriverManager
- Multi-browser WebDriver factory
- ThreadLocal WebDriver instances
- WebDriverManager integration
- Browser options configuration

### ExtentReportManager
- Centralized report management
- Base64 screenshot integration
- System information collection
- HTML report generation

### ScreenshotUtils
- Base64 screenshot encoding
- File-based screenshot capture
- Automatic screenshot attachment
- Error handling

## 📈 Best Practices Implemented

1. **Page Object Model**: Clean separation of page logic and test logic
2. **ThreadSafe WebDriver**: ThreadLocal pattern for parallel execution
3. **Configuration Management**: Externalized test configuration
4. **Comprehensive Logging**: Structured logging throughout the framework
5. **Screenshot Integration**: Automatic capture and embedding in reports
6. **Error Handling**: Proper exception handling and recovery
7. **Responsive Testing**: Layout validation across different screen sizes
8. **Parallel Execution**: TestNG parallel execution support

## 🐛 Troubleshooting

### Common Issues

1. **Browser Driver Issues**
   ```bash
   # Update WebDriverManager cache
   mvn clean install -DskipTests
   ```

2. **Report Generation Issues**
   ```bash
   # Check test-output directory permissions
   chmod 755 test-output/
   ```

3. **Galen Spec Issues**
   ```bash
   # Validate Galen spec syntax
   # Check element locators in .gspec files
   ```

### Log Analysis
Check `logs/automation.log` for detailed execution logs:
```bash
tail -f logs/automation.log
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the logs in `logs/automation.log`
- Review the ExtentReports for test execution details

## 🚀 Future Enhancements

- [ ] API testing integration
- [ ] Database validation support
- [ ] Docker containerization
- [ ] CI/CD pipeline integration
- [ ] Performance testing capabilities
- [ ] Mobile app testing support