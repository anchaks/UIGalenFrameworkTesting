# Introduction to Galen Framework

---

## 1. What is Galen Framework?

Galen Framework is an open-source tool designed for automating the testing of web application layouts. It enables quality assurance teams and developers to verify that their applications’ UI layouts are visually correct across different browsers and devices. Galen uses a simple, human-readable syntax for writing layout specifications and integrates easily with Selenium and CI/CD pipelines.

---

## 2. How the `.gspec` File Works

A `.gspec` file is a Galen Specification file where you declare your page objects and define layout expectations using a domain-specific language. It specifies where elements should be, their sizes, distances between elements, and even if they should be visible or not.

**Example: Basic .gspec File**

```text
@objects
    header       id   header
    logo         css  .logo
    navMenu      id   main-nav
    mainButton   id   submit-btn

= Header section =
    header inside screen 0px top
    logo inside header 10px left
    navMenu right of logo 20px

= Main button check =
    mainButton below header 30px
    mainButton width ~ 120px
    mainButton height 40px
    mainButton centered horizontally inside screen
```

**Explanation:**
- Objects are mapped using identifiers (id, css, xpath selectors, etc.).
- Layout rules use readable phrases: e.g. `right of`, `below`, `inside`.
- `mainButton width ~ 120px` means width is approximately 120px (with a small tolerance).
- Sections can be named (`= Main button check =`) for organizational clarity.

---

## 3. How We Connect the `.gspec` File to Our Test Classes

You reference `.gspec` files in your test automation code—commonly in Java using the Galen Java API, but Galen also supports its own test language and JavaScript.

**Example: Using Galen in a Java Test**

_Ensure you have Galen dependencies and WebDriver set up._

```java
import com.galenframework.api.Galen;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Arrays;

public class LayoutTest {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("http://example.com");

            // The third argument is a list of tags that allow you to specify conditions (e.g., desktop, mobile)
            Galen.checkLayout(driver, "specs/homepage.gspec", Arrays.asList("desktop"));
        } finally {
            driver.quit();
        }
    }
}
```

**Using Galen’s own test syntax (`.test` file):**

```text
@@ check layout of homepage

load "http://example.com" size 1280x800
check layout "specs/homepage.gspec"  tags "desktop"
```

**Running from the CLI:**

```bash
galen check specs/homepage.gspec --url "http://example.com" --size "1280x800" --tags "desktop"
```

---

## 4. How Is Galen Useful?

- **Automated Visual Testing:** Galen checks visual correctness in ways that functional tests can’t (e.g., pixel-perfect alignment, responsive breakpoints).
- **Specification as Code:** Layout requirements are version-controlled, reviewed, and documented right in your repo.
- **Catches Layout Bugs Early:** Galen finds regression and cross-browser UI bugs during CI runs, before code reaches production.
- **Works with Responsive Designs:** Easily target multiple device sizes or custom breakpoints with tags.

---

## 5. What Can Be Tested With Galen?

- **Alignment and Placement:** Exact position of elements relative to one another or to containers (e.g., “button 20px below header”).
- **Size Constraints:** Ensures elements are specific or relative sizes regardless of screen.
- **Visibility:** Tests if an element is visible/hidden when it’s supposed to be.
- **Responsive Layout:** Same checks can be run against various viewports, using tags for “desktop”, “tablet”, “mobile”, etc.
- **Distances and Margins:** E.g., checks that spacing between cards in a grid is consistent and accurate.
- **Complex Component Structure:** Verifies groups of elements—like ensuring a modal dialog’s subcomponents are correctly arranged.

**Example: Responsive Block in .gspec**
```text
@objects
    menuButton id menu-btn
    menuPanel css .menu-panel

= Mobile menu =
    @on mobile
        menuButton visible
        menuPanel inside screen 0px left right

    @on desktop
        menuButton absent
        menuPanel width ~ 300px
```

This .gspec excerpt applies different rules based on given tags (in code/CLI, you pass either “mobile” or “desktop”).

---

## Summary

- Galen makes it practical to encode complex layout/layout-driven requirements into automated tests.
- It’s best suited for teams wanting early, automated UI validation as part of their build and deploy pipeline.
- Spec files are easy to write, re-use, and review—making visual quality a regular, enforceable checklist item in your development process.

**For further reading and more advanced usage, visit:**  
[Galen Framework Official Documentation](http://galenframework.com/docs/)