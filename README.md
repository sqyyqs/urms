urms - User Request Management System 


- admin:admin - ADMINISTRATOR 
- oper:oper - OPERATOR
- user:user - USER
- adminoper:adminoper - ADMINISTRATOR+OPERATOR
- adminuser:adminuser - ADMINISTRATOR+USER
- operuser:operuser - OPERATOR+USER
- usertobeop:usertobeop - USER

### Жизненный цикл заявки 
1. /request/create – только для USER, создает DRAFT 
2. /request/update - только для USER и DRAFT заявки, меняет основные параметры
3. /request/updateStatus –  *payload*`{..."newRequestStatus": "SENT"...}` – только для USER, меняет статус заявки на SENT 
4. /request/updateStatus –  *payload*`{..."newRequestStatus": "ACCEPTED/REJECTED"...}` – только для OPERATOR, меняет статус заявки на ACCEPTED/REJECTED


### /search
Если только USER - обязательно проставить *payload*`{..."fromCurrentUser": true...}`, покажет все заявки от текущего логина. 
Игнорирует name в этом случае. *payload*`{..."fromCurrentUser": true...}` доступен только для роли USER

ADMINISTRATOR и OPERATOR *payload*`{..."name": "something"...}` – поиск по имени/части имени

