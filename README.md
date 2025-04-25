







La base de datos JPhW tendrá la siguiente estructura:

```sql
CREATE TABLE images (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    data BLOB NOT NULL
);
```