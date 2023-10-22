## Дипломный проект профессии "Тестировщик"

___

### О проекте
Проект представляет собой комплексное автоматизированное тестирование сервиса по покупке туров через интернет-банк. 
Купить тур можно с помощью двух способов:

* оплата с помощью дебетовой карты
* покупка в кредит

Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:

* сервису платежей (Payment Gate)
* кредитному сервису (Credit Gate)

Приложение в собственной СУБД сохраняет информацию о том, каким способом был совершён платёж и успешно ли он был совершён.

[Ссылка на проект](https://github.com/netology-code/qa-diploma)

### Документация по проекту

1. [План автоматизации тестирования](https://github.com/Tatiana-Zenina/DIPLOM_QA58/blob/main/docs/Plan.md)
2. [Отчёт по итогам тестирования](https://github.com/Tatiana-Zenina/DIPLOM_QA58/blob/main/docs/Report.md)
3. [Отчёт по итогам автоматизации](https://github.com/Tatiana-Zenina/DIPLOM_QA58/blob/main/docs/Summary.md)


### Запуск приложения

Для запуска приложения необходимо следующее ПО:

* IntelliJ IDEA
* Docker

* Перед запуском необходимо склонировать репозиторий на свой ПК командой ```git clone https://github.com/Tatiana-Zenina/DIPLOM_QA58```
* Далее открыть проект в приложении IntelliJ IDEA, и в терминале командой ```docker-compose up```  развернуть контейнер, необходимый для дальнейшей работы.

В новой вкладке терминала запустить тестируемое приложение:
* Для MySQL:
   ```
    java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar

* Для PostgreSQL:
   ```
    java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

### Запуск тестов

В новой вкладке терминала запустить тесты:

1. Для MySQL:
   ```
   ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"
   ```
2. Для PostgreSQL:
   ```
   ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
   ```

### Генерируем отчет Allure по итогам тестирования, который автоматически откроется в браузере

  ```./gradlew allureServe (запуск и открытие отчета)```

После просмотра отчета останавливаем действие allureServe комбинацией клавиш Ctrl + C, подтверждаем закрытие клавишей Y и нажимаем Enter.

* остановить SUT комбинацией клавиш ```CTRL+C```

* Остановить контейнеры командой ```docker-compose stop``` и после удалить контейнеры командой
  ```docker-compose down```
