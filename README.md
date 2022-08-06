# todo
HTTP API сервис для менеджера задач

Стек: Java, Spring Boot, Maven, PostgreSQL.

Возможности:
1)	Вернуть список всех задач (GET: /tasks)
2)	Получить один тег по УИД и все его задачи (GET: /tag/{УИД тега})
3)	Добавить/изменить тег (POST: /tag)
4)	Добавить/изменить задачу (POST: /task)
5)	Удалить задачу (DELETE: /task/{УИД задачи})
6)	Удалить тег со всеми прикрепленными к нему задачами (DELETE: /tag/{id})