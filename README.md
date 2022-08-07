# capybara_db
![logo](./.github/logo.jpg)

**Disk-based storage for capybaras.** 

## Packed Page Layout
- based on RandomAccessFile
- meta file in CBOR format (in the future)

```
table.data
__________
123223
```

## Query Examples
```sql
SELECT FROM example INDEX 1
SELECT FROM example FROM 2 TO 4
```