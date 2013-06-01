# JDBCMySQLSample
```
By: Borys Niewiadomski
Date: 2013.05.31
```

# Info:

A sample application using MySQL JDBC. 

# Instructions:

**1.** Install and run MySQL Server, or connect to an already established server.

**2.** Create sample mysql DB and table, soemthing like...

```sql
create database summer_plans;
```

```sql
create table plans (
	id int not null auto_increment,
	name varchar(40) not null,
	date date not null,
	location varchar(40) not null,
	comment  varchar(100) not null
	primary key (id));
```

**3.** Setup connect info with Client.java.
May want to create a restricted user as well for the above database. 

**4.** Run & play. 