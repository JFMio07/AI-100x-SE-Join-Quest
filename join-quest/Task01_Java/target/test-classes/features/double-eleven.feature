@double_eleven
Feature: Double Eleven Promotion Pricing
  As a shopper during Double Eleven event
  I want the system to calculate my order total with Double Eleven bulk discount
  So that I can get 20% discount for every 10 pieces of the same product

  Background:
    Given the Double Eleven promotion is active

  Scenario: Purchase 12 pieces of same product - basic bulk discount
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 50        |
    Then the order summary should be:
      | totalAmount |
      | 500         |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: Purchase 27 pieces of same product - multiple bulk discounts
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 50        |
    Then the order summary should be:
      | totalAmount |
      | 1150        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: Purchase 10 different products - no bulk discount applies
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 商品A         | 1        | 50        |
      | 商品B         | 1        | 50        |
      | 商品C         | 1        | 50        |
      | 商品D         | 1        | 50        |
      | 商品E         | 1        | 50        |
      | 商品F         | 1        | 50        |
      | 商品G         | 1        | 50        |
      | 商品H         | 1        | 50        |
      | 商品I         | 1        | 50        |
      | 商品J         | 1        | 50        |
    Then the order summary should be:
      | totalAmount |
      | 500         |
    And the customer should receive:
      | productName | quantity |
      | 商品A         | 1        |
      | 商品B         | 1        |
      | 商品C         | 1        |
      | 商品D         | 1        |
      | 商品E         | 1        |
      | 商品F         | 1        |
      | 商品G         | 1        |
      | 商品H         | 1        |
      | 商品I         | 1        |
      | 商品J         | 1        |

  Scenario: Double Eleven with threshold discount - bulk discount then threshold discount
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 1000      | 100      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then the order summary should be:
      | originalAmount | doubleElevenDiscount | subtotalAfterDoubleEleven | thresholdDiscount | totalAmount |
      | 1200           | 200                  | 1000                      | 100               | 900         |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: Double Eleven with buy-one-get-one for cosmetics
    Given the buy one get one promotion for cosmetics is active
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 口紅          | cosmetics | 12       | 100       |
    Then the order summary should be:
      | originalAmount | doubleElevenDiscount | totalAmount |
      | 1200           | 200                  | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 口紅          | 13       |

  Scenario: Mixed products with Double Eleven discount
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | T-shirt     | 5        | 200       |
      | 襪子          | 15       | 50        |
    Then the order summary should be:
      | totalAmount |
      | 1650        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 5        |
      | 襪子          | 15       |

  Scenario: Large quantity with multiple bulk discount groups
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 35       | 100       |
    Then the order summary should be:
      | totalAmount |
      | 2900        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 35       |

  Scenario: Complex scenario with multiple promotions and Double Eleven
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 1000      | 100      |
    And the buy one get one promotion for cosmetics is active
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | T-shirt     | apparel   | 8        | 200       |
      | 口紅          | cosmetics | 12       | 100       |
    Then the order summary should be:
      | originalAmount | doubleElevenDiscount | subtotalAfterDoubleEleven | thresholdDiscount | totalAmount |
      | 2800           | 200                  | 2600                      | 100               | 2500        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 8        |
      | 口紅          | 13       |