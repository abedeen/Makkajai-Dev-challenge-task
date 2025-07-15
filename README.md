# Makkajai-Dev-challenge-task

# Sales Tax Problem 1

## Problem Statement

This application solves the "Sales Tax" problem as described below:

- **Basic sales tax** is applicable at a rate of **10%** on all goods, **except** books, food, and medical products (which are exempt).
- **Import duty** is an additional sales tax applicable on **all imported goods** at a rate of **5%**, with no exemptions.
- When items are purchased, a receipt is generated listing all items and their final prices (including tax), as well as the total cost and the total sales taxes paid.
- **Rounding Rule:** For any tax calculation, the tax is rounded **up to the nearest 0.05**.

## Example Inputs & Outputs

### Input 1
```
1 book at 12.49
1 music CD at 14.99
1 chocolate bar at 0.85
```

#### Output 1
```
1 book: 12.49
1 music CD: 16.49
1 chocolate bar: 0.85
Sales Taxes: 1.50
Total: 29.83
```

---

### Input 2
```
1 imported box of chocolates at 10.00
1 imported bottle of perfume at 47.50
```

#### Output 2
```
1 imported box of chocolates: 10.50
1 imported bottle of perfume: 54.65
Sales Taxes: 7.65
Total: 65.15
```

---

### Input 3
```
1 imported bottle of perfume at 27.99
1 bottle of perfume at 18.99
1 packet of headache pills at 9.75
1 box of imported chocolates at 11.25
```

#### Output 3
```
1 imported bottle of perfume: 32.19
1 bottle of perfume: 20.89
1 packet of headache pills: 9.75
1 imported box of chocolates: 11.85
Sales Taxes: 6.70
Total: 74.68
```

## Requirements

- Parse a list of shopping basket items from input.
- Calculate sales taxes and import duty according to the rules above.
- Apply rounding to the nearest 0.05 for each tax calculation.
- Output the receipt with itemized prices (including taxes), total sales taxes, and total cost.

## How to Run

1. Clone this repository.
2. Implement your solution in your preferred programming language.
3. Use the provided input examples to test your application.
4. The output should match the examples above.

## Notes

- Exempt categories: books, food, medical products.
- "Imported" items are identified by the word "imported" in the item description.

---

**Happy coding!**