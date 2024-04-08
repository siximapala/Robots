# Robots
The project to learn OO design concepts and MDI application development in Java

# Выполненные практические задачи

## 1. Знакомство с MVC (Ландышев Тимофей)

### Добавлено новое окно с координатами робота
- Реализовано новое окно, в котором отображаются текущие координаты робота при его движении.
- Логика отображения робота и модель робота были разделены (GameMovementModelling и GameVisualizer), модель описана как обозреваемый объект (Observable).
- Окно подписывается на уведомления от модели и обновляет свое состояние.
- Исправлена ошибка в логике управления роботом, которая приводила к неверному поведению.

## Промежуточное задание

### Выделение объектов в игровом поле через отдельный объект Entity
- Объекты на поле наследуются от класса Entity, содержащего главные и необходимые поля.
- Объекты сохранены в отдельном пакете robots/models

## 2. Знакомство с управлением разнородными объектами (Коптелов Артём)

### Сохранение состояния окошек
- Создан новый класс InternalFrameState через Serializable для реализации запоминания данных об окне.
- Реализовано сохранение состояния окна приложения при выходе из приложения, заполняя window_state.properties (метод saveWindowState() в MainApplicationFrame).
- Реализовано восстановление геометрии окон после возвращение в приложение (метод restoreWindowState() в MainApplicationFrame).

## 3. Пример 2. Локализация (Групповое Задание 1)

### Локализация приложения
- Реализовано новое окно меню в виде класса: Смена языка (LanguageMenu)
- Добавлен новый Resource Bundle "lang", содержащий файлы .properties для применения локализации
- Рефакторинг MainApplicationFrame (все окна меню теперь содержатся в отдельном классе)
- Добавлена возможность выбора языка интерфейса через меню.
- Реализована локализация с помощью классов ResourceBundle и метода форматирования сообщений.