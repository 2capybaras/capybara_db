Разработка базы данных для хранения чисел в таблице на основании текстовых файлов.

Формат текстового файла для хранения таблицы следующий:
* Разделителем называем любую комбинацию пробелов и табов в любых количествах (`\t`).
* Строки отделяются символом переноса строки `\n`. Перенос каретки (`\r`) игнорируется.
* Первая строка файла содержит имена столбцов. Это просто строки. Пробелы внутри имен недопустимы.
* Остальные строки содержат набор чисел в формате, совместимом со спецификацией числа с плавающей точкой в котлин (`Double.toString()`/ `String.toDouble()`)
* Пустые строки в файле игнорируются.

Задачи для работы с базой данных:
* Организовать запись в файл с несколькими колонками случайных данных (минимум 1000 строк).
* Организовать доступ к данным по индексу (должно быть API для объекта-таблицы и доступа к объекту строке по индексу).
* Сделать возможность выборки набора строк по диапазону, в котором находится значение того или иного поля.
* (*) Возможность сшивать строки из двух разных файлов по ключу (join - операция).
* (**) Персистентный индекс по полю. Возможность делать индексирующую таблицу, хранящую координату начала той или иной
  строки в зависимости от значения поля. Индексирующие таблицы используются для быстрого обращения к тому или иному
  сегменту базы, без чтения всего файла.

```text
SELECT * FROM example
SELECT RANGE(1, 10) FROM example
INSERT INTO example RANDOM
SELECT INDEX(1) FROM example
```