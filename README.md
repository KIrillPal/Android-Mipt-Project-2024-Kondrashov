# Stage 2
---
### Что сделано:

1. В приложении есть два экрана с динамическими списками
2. Делегатов нет, у списков по 2 view holder'а.
3. DiffUtil есть, но времени реализовывать удаление сообщений нет. Добавления есть, красивые
4. RxJava используется в ApplicationData и NetworkService.
5. Retrofit для взаимодействия с сервером. 
6. Glide используется, но только в одном месте. Кончилось время на остальное.
7. Подгрузка из сети есть, бесконечного списка нет.
8. Приложение логично, походы в сеть имеют логический смысл

### Как запустить базу данных:
1. ```cd ~/kondrashovki-project```
2. ```sudo docker run --name chatapp -p 3001:3000 -e POSTGRES_PASSWORD=123 -d kirillpal/chatapp_android:chatapp-db```
3. ```sudo docker exec -it chatapp /bin/sh```
4. ```cd ~/kondrashovki-project && psql -U postgres```
5. ```\i chatapp_ddl.sql```
6. ```\i chatapp_inserts.sql```
7. ```\i chatapp_queries.sql```
8. ```\q```
9. ```cd / && bash run.sh```

#### Готово

Остановить: ```sudo docker stop chatapp```

Удалить: ```sudo docker rm chatapp```
