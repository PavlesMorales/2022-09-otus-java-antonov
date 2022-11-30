# Задание 5
# Описание/Пошаговая инструкция выполнения домашнего задания:

Разработайте такой функционал:
метод класса можно пометить самодельной аннотацией @Log, например, так:

```java
class TestLogging implements TestLoggingInterface {
    @Log
    public void calculation(int param) {
    }
}
```
При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
Например так.

```java
class Demo {
   public void action() {
       new TestLogging().calculation(6);
   }
}
```

В консоле дожно быть:
executed method: calculation, param: 6
Обратите внимание: явного вызова логирования быть не должно.
Учтите, что аннотацию можно поставить, например, на такие методы:

```java
public void calculation(int param1);
public void calculation(int param1, int param2);
public void calculation(int param1, int param2, String param3);
```