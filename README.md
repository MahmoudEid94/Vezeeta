# Vezeeta Website Automated Testing

This repository contains automated test cases designed to test the functionalities of [Vezeeta](https://www.vezeeta.com/) using Selenium WebDriver, TestNG, and Java. The project follows the Page Object Model (POM) design pattern and includes a test suite with basic reporting using XML execution files. 

## Overview

The primary goal of this project is to automate the testing of Vezeeta's website using TestNG and Selenium WebDriver. The test suite, written in Java, encompasses various scenarios such as user registration, login (positive and negative scenarios), doctor search, result sorting, and appointment booking.

### Test Scenarios Included:

- Registration with an existing user.
- Login scenarios (positive and negative).
- Searching for doctors with different criteria.
- Sorting the result page.
- Booking time slots.

### Test Logic Highlights:

#### Searching Functions:

- Generic search functionality allows testers to pass any specialty or city and select a random area if desired.
- Each attribute (specialty, city, area) has a dedicated method for selection.
- Allows searches by doctor name.

#### Sorting Function:

- Implemented a robust sorting verification method that traverses multiple pages to ensure correct sorting.
- Checks price sorting on each page and validates the correct order.
- Dynamically reads the total pages and adapts behavior accordingly.
- Fails the test if a mismatch in sorting sequence is found.

#### Booking Function:

- Dynamic method recognizes and handles different types of available time slots.
- Identifies three types of time slots: single time frame, two-time frames with breaks, and multiple time slots (varying durations).
- Selects the suitable time slot as provided.
- Handles multiple time slots by recognizing and selecting the first available time slot if the required one is booked.
- Supports specifying a required future day and calculates the difference to proceed to the required day.

## Technologies Used:

1. POM (Page Object Model)
2. Selenium WebDriver
3. TestNG annotations
4. Java
5. XML execution files for reporting
6. GitHub as Version Control System (VCS)

Feel free to explore the codebase to understand the test cases and their implementation details.
