<div>

## Аттестационная работа по курсу Skypro 
##           "Java-разработчик ISA"  

</div>

___

## Описание проекта и его функциональность

Был предоставлен фронтенд для учебного сайта, который описывает интернет-магазин, похожий на Avito
На этом сайте пользователи могут создавать объявления и оставлять комментарии к своим объявлениям и объявлениям других
пользователей.

### Реализованы следующие функции:

- Авторизация и аутентификация пользователей;
- CRUD-операции для объявлений на сайте;
- CRUD-операции для комментариев к объявлениям;
- Пользователи могут создавать, удалять или редактировать свои собственные объявления и комментарии. Администраторы
  могут удалять и редактировать все объявления и комментарии;
- Поиск объявлений по названию в шапке сайта;
- Загрузка и отображение изображений объявлений и аватаров пользователей.
___
## Задание:

### Исходные данные
- Для проекта платформы по перепродаже вещей создана фронтенд-часть сайта.

## Что необходимо сделать
- Нужно собрать бэкенд-часть сайта на Java.

## Бэкенд-часть проекта предполагает реализацию следующего функционала:

- Авторизация и аутентификация пользователей.
- Распределение ролей между пользователями: пользователь и администратор*.
- CRUD для объявлений на сайте: администратор может удалять или редактировать все объявления, а пользователи — только свои.
- Под каждым объявлением пользователи могут оставлять отзывы.
- В заголовке сайта можно осуществлять поиск объявлений по названию.
- Показывать и сохранять картинки объявлений.
___

## Инструменты, используемые в проекте:

* Backend:
    - JDK 11 + Spring
  
    - Spring Boot
    - Spring Web
    - Spring Data JPA
    - Spring Security
  
    - Maven, Swagger, Lombok, GIT
  
    - REST API, Stream API, JSON
  
* Database:
    - Spring JPA
  
    - PostgreSQL, H2
  
    - Liquibase
  
* Test:
  - Junit
  - Mockito
* Frontend:
  - Docker Image

___

## Запуск приложения

* Для запуска приложения требуется выполнить :
    - Клонировать проект и открыть его в среде разработки (например *IntelliJ IDEA*);
    - В файле **application.properties** указать путь к Вашей базе данных;
    - Запустить **Docker**;
    - Скачать  **Docker image**  командой  - ```docker pull ghcr.io/bizinmitya/front-react-avito:latest```;
    - Запустить **Docker image**   командой  - ```docker run -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:latest```;
    - Запустить приложение  **AdsOnlineApp.java**.

После успешного выполнения всех шагов, фронтенд будет доступен по адресу: http://localhost:3000

___

### Разработчик:

- [Иванов Игорь]

 

