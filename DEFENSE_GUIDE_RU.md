# Защита DevOps Midterm — Мамбетали Аружан

## Что я разработала — текст для выступления на английском

> Hello. My name is Mambetali Aruzhan Imanaliyevna. This is my individual DevOps midterm project. I developed a Student REST API using Java, Spring Boot, Gradle, and PostgreSQL. The project contains CRUD operations: create, read, update, and delete. I implemented seven integration tests and created a multi-stage Dockerfile. Docker Compose starts the application and database in a dedicated network and uses a named volume to preserve PostgreSQL data. Git tracks my changes and branches, while Jenkins automates checkout, compilation, tests, Docker image build, and verification.

> In the Linux part, I created a Bash system monitoring script. It writes my name, system information, CPU, RAM and disk statistics to my personal log file. I configured file permissions and created a README backup. Finally, I demonstrated Git merges and a merge conflict resolution, API operations, PostgreSQL persistence, and the Jenkins pipeline.

Последнее предложение произноси только после того, как **лично выполнишь соответствующие демонстрации**. Если что-то ещё не запускала, говори `I prepared the configuration, and I will demonstrate it now`.

## Порядок показа экрана

| Шаг | Что открыть и показать | На что обратить внимание |
|---|---|---|
| 1 | Терминал в папке проекта: `pwd`, `find . -maxdepth 3 -print` | Название персональное, все обязательные папки и файлы существуют |
| 2 | `ls -l scripts/Mambetali_Aruzhan_system_report.sh` | `-rwxr--r--` = `744` |
| 3 | `bash scripts/Mambetali_Aruzhan_system_report.sh` | Имя, дата, ОС, CPU, RAM, диск |
| 4 | `tail -n 5 logs/Mambetali_Aruzhan_system_report.log` и `grep -n 'Mambetali' ...` | Сохранение и поиск по логу |
| 5 | `diff README.md backups/README_Mambetali_Aruzhan_backup.md` | Пустой вывод значит файлы одинаковые |
| 6 | `git log --graph --all --oneline --decorate` | Feature ветки, merge, конфликт, tag |
| 7 | GitHub: открыть опубликованный репозиторий | Commits, branches, tag; если не опубликовано, этот пункт ещё не готов |
| 8 | `docker compose up -d --build` и `docker compose ps` | Два контейнера; БД `healthy` |
| 9 | `docker compose logs --tail 30 app` | Spring Boot подключился к базе |
| 10 | `bash scripts/api_demo.sh` | HTTP 201, 200, 200, 200, 204, 404 |
| 11 | `bash scripts/persistence_demo.sh` | Запись есть до и после перезапуска |
| 12 | Jenkins Job → Build Now → Stage View → Console Output | Пять стадий, твоё имя, success |
| 13 | Jenkins: специально ошибочный тест на отдельной ветке, затем исправление | Failure → fix → success |
| 14 | Jenkins Build History после нового Git-коммита | Автоматический запуск polling trigger |

Перед пунктом 8 нужно создать `.env` на основе `.env.example` и задать локальный пароль. Перед пунктом 12 должен быть установлен Jenkins с настроенным агентом `docker`, а репозиторий опубликован на GitHub.

## Часть 1 — Linux

Созданы папки `scripts`, `logs`, `backups`, `src`, файлы `Dockerfile`, `docker-compose.yml`, `Jenkinsfile`, `build.gradle`, `README.md`. Папки нужны для логического разделения проекта.

Для демонстрации используй:

```bash
chmod 744 scripts/Mambetali_Aruzhan_system_report.sh
ls -l scripts/Mambetali_Aruzhan_system_report.sh
bash scripts/Mambetali_Aruzhan_system_report.sh
tail -n 5 logs/Mambetali_Aruzhan_system_report.log
grep -n Mambetali logs/Mambetali_Aruzhan_system_report.log > logs/Mambetali_Aruzhan_log_analysis.txt
cat logs/Mambetali_Aruzhan_log_analysis.txt
cp README.md backups/README_Mambetali_Aruzhan_backup.md
diff README.md backups/README_Mambetali_Aruzhan_backup.md
ps aux | grep '[j]ava' || true
```

- `#!/bin/bash` — shebang: система запускает файл через Bash.
- `mkdir` — создаёт каталог; `mkdir -p` также создаёт вложенные каталоги и не падает, если каталог уже есть.
- `pwd` — текущая папка; абсолютный путь начинается с `/`, относительный отсчитывается от текущей папки.
- `ls -la` — в том числе скрытые файлы.
- `chmod 744` — owner `rwx`, group `r--`, others `r--`; цифры `4=read`, `2=write`, `1=execute`.
- `>` — перезаписывает файл; `>>` — дописывает в конец. `tee -a` тоже добавляет строки, но одновременно выводит их на экран.
- `touch file.log` — создаёт пустой файл; `tail -n 5` — последние 5 строк.
- `grep 'Mambetali' file.log` — поиск фамилии по содержимому.
- `ps aux` — процессы; PID — идентификатор процесса; `kill PID` посылает сигнал завершения.
- `free -h` — память; `df -h` — занятое и доступное место на дисках; `uname -r` — ядро Linux.
- `cp` — копия, исходный файл остаётся; `mv` — перемещение/переименование.

## Часть 2 — Git

Мой проект использует ветки `main`, `feature/mambetali-aruzhan-crud` и `feature/mambetali-aruzhan-documentation`. Последовательность проверяется через реальный `git log --all --graph`.

- `git init` создаёт локальный репозиторий; `git status` показывает изменённые и подготовленные файлы.
- Рабочая папка — где редактируем; staging area — выбранные изменения (`git add`); repository — история сохранённых коммитов (`git commit`).
- `git commit` сохраняет снимок изменений в локальной истории; `git push` отправляет коммиты на сервер.
- Git — система контроля версий; GitHub — облачная платформа для хранения Git-репозиториев и совместной работы.
- Branch — отдельная линия разработки; `git switch -c feature/name` создаёт новую ветку и переключает на неё.
- `git merge` сохраняет историю веток и объединяет их; `git rebase` переносит коммиты на другую базу и может переписать их идентификаторы.
- Merge conflict появляется, если обе ветки по-разному изменили один участок файла. Удаляем маркеры `<<<<<<<`, `=======`, `>>>>>>>`, сохраняем итоговый текст, выполняем `git add`, затем `git commit`.
- `git fetch` загружает удалённые изменения без встраивания в текущую ветку; `git pull` также пытается интегрировать их.
- Git tag помечает определённый коммит как версию; commit — запись об изменении, а tag — именованная ссылка на конкретную запись.
- `.gitignore` исключает файлы из новых добавлений, но не удаляет уже закоммиченные секреты из истории.
- Пароли и `.env` не отправляем на GitHub: их сможет получить посторонний через историю или публичный репозиторий. Вместо этого используем локальные env переменные и Jenkins Credentials.

Если удалённый GitHub отличается от локального: `git status`, `git remote -v`, `git fetch`, `git log --oneline --graph --all`, `git diff main origin/main`; только после проверки решаем, нужен ли pull/merge/rebase.

## Часть 3 — Docker

В `Dockerfile` есть две стадии. Первая (Gradle и JDK) собирает JAR. Вторая (JRE) содержит только Java Runtime и готовый JAR, поэтому итоговый образ меньше и не содержит Gradle-кэша.

- Docker image — неизменяемый шаблон приложения и зависимостей; container — запущенный экземпляр image.
- `FROM` выбирает базовый образ; `WORKDIR` задаёт рабочую папку; `COPY` копирует файлы; `RUN` выполняется во время сборки image; `CMD`/`ENTRYPOINT` определяет процесс при запуске контейнера; `EXPOSE` документирует порт.
- `RUN` создаёт слой image при сборке; `CMD` задаёт команду по умолчанию на запуске.
- `COPY` копирует файлы явно; `ADD` также умеет работать с архивами/URL, для обычного копирования предпочитаем `COPY`.
- `docker build -t name:tag .` собирает image; `docker ps` показывает работающие контейнеры; `docker stop` останавливает, `docker rm` удаляет контейнер.
- `docker compose` поднимает несколько сервисов из YAML. У нас `app` и `db`.
- Docker network обеспечивает связь по имени сервиса; Docker volume сохраняет файлы отдельно от жизни контейнера.
- Из Spring Boot контейнера `localhost` — **сам контейнер приложения**, а не PostgreSQL. Поэтому правильный адрес `jdbc:postgresql://db:5432/student_db`.
- PostgreSQL health check исполняет `pg_isready`; приложение ждёт готовности `db`, а не просто запуска процесса контейнера.
- Пароль БД берётся из `.env` через Compose, но не хранится в Git. У нас нет публичного порта PostgreSQL, только внутренний доступ в сети Compose.

Основные команды:

```bash
docker compose ps
docker compose logs --tail 30 app
docker compose exec app java -version
docker inspect mambetali-aruzhan-student-api-app-1
docker compose stop app
docker compose start app
docker images
docker image inspect mambetali-aruzhan-student-api:v1.0-mambetali-aruzhan
```

Если приложение не подключается к PostgreSQL: `docker compose ps`, `docker compose logs db`, `docker compose logs app`, проверь `SPRING_DATASOURCE_URL`, имя сервиса `db`, healthcheck и данные `.env`.

## Часть 4 — Jenkins

`Jenkinsfile` — описание Pipeline в коде, которое можно менять и версионировать вместе с проектом. **Declarative Pipeline** использует структуру `pipeline { agent ... stages ... }`; Scripted Pipeline более гибкий и пишется в Groovy-логике.

- Jenkins job — настроенная задача CI/CD; controller управляет Jenkins, agent выполняет сборку.
- Checkout — получает нужный commit из GitHub.
- Build — компилирует Java-код через Gradle Wrapper.
- Test — запускает JUnit интеграционные тесты. При ошибке команда Gradle возвращает ненулевой код и следующие стадии не стартуют.
- Docker Build — создаёт персональный image; Verify — делает `docker image inspect` и показывает ID/size.
- Jenkins Credentials безопаснее пароля в Jenkinsfile: Jenkins хранит доступ централизованно с контролем прав.
- CI (Continuous Integration) — регулярная интеграция изменений и автоматическая проверка сборки/тестов.
- Continuous Delivery — результат готов к развёртыванию, но запуск может требовать ручного подтверждения.
- Continuous Deployment — успешная версия развёртывается автоматически без ручного решения.
- У этого задания автоматическое развёртывание не требуется.
- Polling проверяет Git по расписанию; Webhook уведомляет Jenkins по событию push. У нас настроен `pollSCM('H/5 * * * *')`.

Если Pipeline компилируется, но `Docker Build` падает: проверь `docker version`, доступ агента к Docker daemon, наличие Dockerfile, контекст сборки и лог конкретной стадии. Исправляй на своей тестовой ветке, не в чужом production окружении.

## Частые вопросы преподавателя и короткие ответы

| Вопрос | Ответ |
|---|---|
| Зачем Spring Boot? | Быстро поднимает Java REST API со встроенным сервером и интеграцией с базой. |
| Что такое CRUD? | Create, Read, Update, Delete — четыре базовые операции над данными. |
| Что такое REST? | Стиль API, где ресурсы доступны по URL и HTTP-методам. |
| Что делает GET? | Возвращает данные без их изменения. |
| POST? | Создаёт ресурс и здесь возвращает `201 Created`. |
| PUT? | Полностью обновляет существующий ресурс. |
| DELETE? | Удаляет ресурс, здесь возвращает `204 No Content`. |
| Почему 404? | Студента с запрошенным ID нет. |
| Зачем PostgreSQL? | Хранит записи студентов в реляционной базе. |
| Зачем JPA? | Отображает Java-сущности в таблицы и упрощает CRUD. |
| Зачем Gradle? | Управляет зависимостями и автоматизирует сборку и тесты. |
| Зачем Gradle Wrapper? | Фиксирует одинаковую версию Gradle на ноутбуке и Jenkins. |
| Что делает `.gitignore`? | Исключает чувствительные и генерируемые файлы из новых Git add. |
| Что такое shebang? | Строка `#!/bin/bash` задаёт интерпретатор скрипта. |
| Docker image vs container? | Образ — шаблон; контейнер — экземпляр этого шаблона. |
| Почему volume? | Данные PostgreSQL переживают удаление/создание контейнера. |
| Почему `db`, а не `localhost`? | Внутри app-контейнера `localhost` указывает на сам app. |
| Что проверяют тесты? | Успешные CRUD, 404, некорректный email 400 и health endpoint. |
| Почему тесты перед Docker image? | Чтобы не собирать заведомо неисправную версию. |
| Как посмотреть последние пять строк? | `tail -n 5 filename.log`. |
| Как сохраняем поиск? | `grep -n Mambetali logfile > analysis.txt`. |
| `git status`? | Показывает ветку, изменённые и staged файлы. |
| Как создать ветку? | `git switch -c feature/my-change`. |
| Для чего Git tag? | Чтобы обозначить конкретный релиз. |
| Что будет при failed Jenkins stage? | Pipeline останавливается, статус failure, смотри Console Output. |

## Что ещё нужно сделать лично перед отправкой

1. Проверить проект командами `./gradlew clean test` и `docker compose up -d --build` на твоём Ubuntu/WSL, где есть Java, Gradle Wrapper и Docker.
2. Создать персональный репозиторий на GitHub и запушить локальную историю с ветками и тегом.
3. Запустить Jenkins Job и сделать настоящие скриншоты успешного билда, тестового падения/исправления и автоматического старта.
4. Включить скриншоты в отчёт преподавателю. Никаких выдуманных зелёных статусов или фиктивного Console Output.
