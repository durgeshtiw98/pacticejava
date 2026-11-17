# Shop debugging practice

A Java 17 / Maven / JUnit 5 interview exercise. You own a small shop backend that handles
catalog lookup, prices, discounts, stock reservations, orders, and a bounded cache.
There are intentional behavioral defects. A red test run is the starting point.

## Run

Use JDK 17 or newer and Maven 3.8+; compilation targets Java 17.
No database, service, credentials, or environment variables are needed. Maven needs internet
access on the first run to resolve dependencies.

```sh
cd shop-debug-practice
mvn test
```

From the repository root, use `mvn -f shop-debug-practice/pom.xml test`.
The parent directory contains a separate exercise; its Maven command does not run this one.

Run one class or method while investigating:

```sh
mvn -Dtest=MoneyTest test
mvn '-Dtest=MoneyTest#amountsWithDifferentScalesAreEqual' test
```

Read full results in `target/surefire-reports/`. In VS Code, open this folder, install
Extension Pack for Java if needed, and use the Testing view or the gutter next to a test
to debug it with breakpoints. Java 17 is the language/API target even with a newer local JDK.

## Interview brief

Treat the `Spec:` Javadoc on each production class as its contract. Preserve public method
signatures. Fix behavior in production code and add focused regression tests. Do not delete,
disable, or weaken existing tests. The initial suite exercises only part of the contract;
a green suite alone does not demonstrate correctness.

Suggested 60-minute round:

| Minutes | Activity |
| --- | --- |
| 0–8 | Read contracts, trace checkout, run the full suite |
| 8–35 | Investigate failures, explain hypotheses, make small fixes |
| 35–52 | Add regression tests and explore untested contract edges |
| 52–60 | Rerun the full suite, review the diff, explain remaining risks |

## Code map

| Package | Responsibility |
| --- | --- |
| `com.shop.money` | Immutable amounts and currency arithmetic |
| `com.shop.catalog` | Product identity, lookup, listing, and removal |
| `com.shop.inventory` | Shared stock ledger and reservation errors |
| `com.shop.pricing` | Coupons and discounts using an injected clock |
| `com.shop.order` | Line totals, lifecycle, checkout, and cancellation |
| `com.shop.cache` | Bounded cache with least-recently-used eviction |

`OrderService.place` constructs an `Order` and reserves its lines in `InventoryService`.
`Order.total` sums `OrderLine` values, each calculated from a product's `Money` price.
`DiscountService` can quote a coupon-adjusted subtotal. Catalog and cache APIs can be
investigated independently. Persistence and HTTP are outside this exercise.

In coach mode, work through one failure at a time: state the expected result, observed
result, and relevant contract before changing code. Ask for a hint if you get stuck.

## Additional practice

Four more services provide an additional round in the same project:

| Class | Responsibility |
| --- | --- |
| `ProductSearch` | Name search and paginated product listings |
| `BulkPricingService` | Quantity-based unit prices and totals |
| `ExpiringCache` | Clock-based entry lifetime |
| `ReturnPolicy` | Order eligibility and delivery-based return windows |

Each class has a `Spec:` contract and starter tests. As with the original round, some
contract rules need new tests. All time-based tests use controlled clocks and no sleeps.

Run only the additional round:

```sh
mvn '-Dtest=ProductSearchTest,BulkPricingServiceTest,ExpiringCacheTest,ReturnPolicyTest' test
```

Run `mvn test` to include both rounds.
