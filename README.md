# Поштова служба / Postal service
Програма емулює роботу поштової служби: реєстрація клієнтів, авторизація за номером телефону, створення відділень, доставок, опрацювання доставок та відправка повідомлень про статус доставок.

The program emulates the work of the postal service: customer registration, authorization by phone number, creation of branches, deliveries, processing of deliveries and sending messages about the status of deliveries.

**Сценарій виконання програми / Program execution scenario:**
1) Зареєструвати користувача / Register user
2) Авторизуватися / Login
3) Створити кілька відділень / Create multiple branches
4) Створити декілька доставок / Create multiple deliveries
5) Дочекатися переходу всіх доставок у фінальний статус та дочекатися виконання відправок повідомлень / Wait for the transition of all deliveries to the final status and wait for the delivery of messages

**Процес доставки / Delivery process**
1) Користувач створює доставку / User creates delivery
2) Одержувач забирає чи не забирає посилку. Це реалізовано за допомогою окремого розкладу, який раз на секунду за кожною посилкою приймає рішення забрали посилку чи ні. Причому рішення, що посилку не забрали в 5 разів імовірніше. Через 5 секунд, якщо посилку не забрали, вона вважається не доставленою / The recipient takes or does not take the parcel. This is implemented with the help of a separate schedule, which takes the parcel once a second after each parcel. Moreover, the decision that the parcel was not taken 5 times more likely. After 5 seconds, if the parcel is not taken, it is considered not delivered
3) У разі переходу у фінальний стан до таблиці повідомлень додається повідомлення для відправника про успішну або неуспішну доставку / In the event of a final state, a message for the sender about successful or unsuccessful delivery is added to the message table
4) Окремий потік періодично вичитує ненаправлені повідомлення та здійснює відправку (виведення в лог), потім проставляє статус про відправку / A separate thread periodically reads out unsent messages and sends (output to the log), then sets the status of sending

**Сутності / Entities**
1. Користувач / Client
   * ідентифікатор / id
   * прізвище / surname
   * ім'я / firstName
   * по-батькові / patronymic
   * електронна пошта / email
   * телефон (виступає як логін) / phone number (acts as a login)
2. Відділення / Department
   * ідентифікатор / id
   * опис / description
3. Доставка / Delivery
   * ідентифікатор / id
   * користувач (відправник) / client (sender)
   * відділення відправника (з таблиці "Відділення") / department sender (from the table Department)
   * відділення одержувача (з таблиці "Відділення") / department recipient (from the table Department)
   * телефон одержувача / recipient phone
   * прізвище одержувача / recipient surname
   * ім'я одержувача / recipient first name
   * по-батькові одержувача / recipient patronymic
   * статус (Новий, Доставлена, Прострочена) / parcel status (New, Taken, Overdue)
   * дата + час створення / date + time of creation
   * дата + час зміни статусу / date + time of status change
4. Повідомлення / Notification
   * ідентифікатор / id
   * текст / message
   * статус (Новий, Надіслано) / notification status (New, Sent)
5. Статуси повідомлень / Notification status
   * ідентифікатор / id
   * статус / status
6. Статуси посилок / Parcel status
   * ідентифікатор / id
   * статус / status

**Приклад виконання програми / Example of program execution**

POST http://localhost:8080/api/registration

RequestBody:
{
"phoneNumber": 991234567,
"email": "o.solomon@gmail.com",
"surname": "Solomon",
"firstName": "Ofelia"
}

POST http://localhost:8080/api/login

RequestBody:
{
"phoneNumber": 991234567
}

POST http://localhost:8080/api/create-departments

RequestBody:
[
{
"description": "Department #1"
},
{
"description": "Department #2"
},
{
"description": "Department #3"
}
]

POST http://localhost:8080/api/create-deliveries

RequestBody:
[
{
"departmentSender": {
"id": 1
},
"departmentRecipient": {
"id": 2
},
"recipientPhone": 679861572,
"recipientSurname": "Beasley",
"recipientFirstName": "Travis"
},
{
"departmentSender": {
"id": 3
},
"departmentRecipient": {
"id": 1
},
"recipientPhone": 630952130,
"recipientSurname": "Montgomery",
"recipientFirstName": "Wade"
},
{
"departmentSender": {
"id": 2
},
"departmentRecipient": {
"id": 3
},
"recipientPhone": 506703491,
"recipientSurname": "Cunningham",
"recipientFirstName": "Gertrude"
}
]

GET http://localhost:8080/api/find-all-deliveries

