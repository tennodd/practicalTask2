Ця програма шукає всі прості числа до заданого користувачем числа N. Якщо ви введете N, яке знаходиться в діапазоні від 1 до 1000, програма розіб'є задачу на декілька частин і обчислить результати паралельно за допомогою потоків.
Прості числа обчислюються окремими потоками, і після завершення роботи всі результати збираються разом, сортуються та виводяться на екран.

**Як працює програма**

Користувач вводить максимальне число, до якого потрібно знайти прості числа. Введення повинно бути в діапазоні від 1 до 1000. Програма використовує 4 потоки. Діапазон чисел від 2 до N ділиться між потоками. Наприклад, якщо N = 100, кожен потік отримає діапазон по ~25 чисел. Кожен потік працює у своєму діапазоні, перевіряючи кожне число, чи є воно простим. Це відбувається за допомогою методу isPrime. Після завершення роботи всіх потоків результати збираються у спільний список, сортуються, і програма виводить список простих чисел. На екран виводяться всі знайдені прості числа у порядку зростання, а також час виконання програми.

**Пошук чисел**

Програма використовує алгоритм для перевірки чисел. Вона:
1. Відсікає числа, менші за 2.
2. Пропускає парні числа та числа, кратні 3.
3. Перевіряє тільки потенційні дільники до квадратного кореня з числа.

**Інформація для користувача**
Програма працює з 4 потоками, але це можна змінити, якщо потрібно більше паралельності.
Вона використовує Callable і Future для асинхронної роботи, а результати зберігаються у безпечному для потоків CopyOnWriteArrayList.
Час виконання залежить від значення N та кількості потоків, доступних у системі.
**Як запустити програму**
1. Запустіть код у середовищі, яке підтримує Java.
2. Введіть число N у діапазоні від 1 до 1000.
