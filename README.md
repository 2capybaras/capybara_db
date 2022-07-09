# capybara_db
![logo](./.github/logo.jpg)

**Disk-based storage for capybaras.** 

## Packed Page Layout
- based on RandomAccessFile
- meta file in CBOR format (in the future)

```
table.meta
__________
rows
column1
column2
column3
column4


table.data
__________
1.02.234.5
```

## Query Examples
```sql
INSERT example RANDOM
SELECT * FROM example INDEX 1
SELECT * FROM example FROM 2 TO 3
SELECT * FROM example WHERE A FROM 2.0 TO 4.0
SELECT * FROM example JOIN join USING A
SELECT * FROM example JOIN join USING A, B
CREATE example(A, B, C)
DROP example
INDEX example ON A
SELECT * FROM example WHERE A FROM 2.0 TO 4.0
```