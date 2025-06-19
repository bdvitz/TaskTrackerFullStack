This code is based on course material from Amazon Junior Software Developer on Coursera. 
All rights to the original code belong to the course authors and platform.
This repository is for personal, educational use only.

original HELP.md contents below:

# Getting Started

## Setup Instructions to run this project locally

### 1️⃣ Set up MySQL

Ensure you have a MySQL server running locally.  
You will need to create a database and a user that matches the credentials in your environment file.

Example MySQL commands (run these in your MySQL client or terminal):

```sql
-- Replace 'myapp_db', 'myapp_user', and 'strong_password' as needed
CREATE DATABASE myapp_db;

CREATE USER 'myapp_user'@'localhost' IDENTIFIED BY 'strong_password';

GRANT ALL PRIVILEGES ON myapp_db.* TO 'myapp_user'@'localhost';

FLUSH PRIVILEGES;
```
### 2️⃣ Set up your environment variables
Copy the '.env.example' file to create an '.env' file
```cmd
copy .env.example .env
```
with the contents matching your MySQL database name and user details.
```ini
# .env file contents
DB_USERNAME=myapp_user
DB_PASSWORD=strong_password
DB_NAME=myapp_db
```

### 3️⃣ Run the application
Once your database and environment variables are set:
- Install dependencies listed in the pom.xml
- Run migrations (if applicable)
- Start your server

---

# Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.5/reference/web/servlet.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/3.3.5/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.3.5/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.3.5/reference/using/devtools.html)

# Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

