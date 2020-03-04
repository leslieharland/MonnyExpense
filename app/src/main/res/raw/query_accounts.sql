SELECT accountId, accountName, image, a.currencyId, abv,
 (SELECT COUNT(*) FROM expense WHERE expense.accountId = a.accountId) AS numberOfRecords FROM accountDatabase a
 INNER JOIN currency c ON a.currencyId = c.currencyId