Домашнее задание
Самодельный ORM
Работа должна использовать базу данных H2.
Создайте в базе таблицу User с полями:

• id bigint(20) NOT NULL auto_increment
• name varchar(255)
• age int(3)

Создайте свою аннотацию @Id

Создайте класс User (с полями, которые соответствуют таблице, поле id отметьте аннотацией).

Напишите JdbcTemplate, который умеет работать с классами, в котрых есть поле с аннотацией @Id.
Executor должен сохранять объект в базу и читать объект из базы.
Имя таблицы должно соответствовать имени класса, а поля класса - это колонки в таблице.

Методы JdbcTemplate'а:
void create(T objectData);
void update(T objectData);
void createOrUpdate(T objectData); // опционально.
<T> T load(long id, Class<T> clazz);

Проверьте его работу на классе User.

Метод createOrUpdate - необязательный.
Он должен "проверять" наличие объекта в таблице и создавать новый или обновлять.

Создайте еще одну таблицу Account:
• no bigint(20) NOT NULL auto_increment
• type varchar(255)
• rest number

Создайте для этой таблицы класс Account и проверьте работу JdbcTemplate на этом классе.
