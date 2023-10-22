## Дипломный проект профессии "Тестировщик"

___

### Документация по проекту

1. [План автоматизации тестирования](https://github.com/Tatiana-Zenina/DIPLOM_QA58/blob/main/docs/Plan.md)
2. [Отчёт по итогам тестирования]()
3. [Отчёт по итогам автоматизации]()


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

   ```
* Для PostgreSQL:
   ```
    java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
  
  ```
### Запуск тестов
В новой вкладке терминала запустить тесты:
1. Для MySQL:
   ```
   ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"
   ```
1. Для PostgreSQL:
 ```
   ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
   ```

* запустить отчет командой:
  ```./gradlew allureServe (запуск и открытие отчета)```

* остановить SUT комбинацией клавиш ```CTRL+C```

* Остановить контейнеры командой ```docker-compose stop``` и после удалить контейнеры командой
  ```docker-compose down```

    