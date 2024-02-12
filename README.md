# Яндекс Практикум. Проект ExploreWithMe.
Проект — афиша, в которой можно предложить какое-либо событие и собрать компанию для участия в нём. Состоит из двух модулей: основного сервиса и сервиса статистики.

Стек:  **Java 11, Spring Boot, Maven, PostgreSQL, Hibernate, Docker, REST.**  

[Cпецификация API сервиса статистики приложения.](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)  
[Cпецификация API основного сервиса приложения.](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)  
Спецификации можно открыть в https://editor-next.swagger.io/

---

### Запуск приложения

Чтобы запустить приложение необходимо
* Скачать или клонировать репозиторий (git clone https://github.com/shumitum/java-explore-with-me.git)
* Собрать проект командой
```bash
mvn clean package
```
* После успешной сборки запустить контейнеры командой
```bash
docker-compose up
```

---

ER-диаграмма БД проекта:  
![ewm_er_diagram.png](ewm_er_diagram.png)
