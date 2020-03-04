
SELECT dateGroup.dateId AS _id,
dateGroup.dateId,
dateGroup.dateString,
-1 AS expenseId,
NULL as description,
expense.image,
category.categoryId,
category.categoryIcon,
NULL as categoryName,
NULL as amount,
dateGroup.dateString as displayTitle
FROM dateGroup
INNER JOIN expense ON dateGroup.expenseId = expense.expenseId
INNER JOIN category ON expense.categoryId = category.categoryId
UNION

SELECT (10000 + expense.expenseId) AS _id,
dateGroup.dateId,
dateGroup.dateString,
expense.expenseId,
expense.description,
expense.image,
category.categoryId,
category.categoryIcon,
category.categoryName,
expense.amount,
dateGroup.dateString || CASE WHEN expense.expenseId IS NOT NULL THEN ' : ' || category.categoryName ELSE '' END AS displayTitle
FROM dateGroup
INNER JOIN expense ON dateGroup.expenseId = expense.expenseId
INNER JOIN category ON expense.categoryId = category.categoryId
ORDER BY dateGroup.dateString DESC, expenseId DESC
