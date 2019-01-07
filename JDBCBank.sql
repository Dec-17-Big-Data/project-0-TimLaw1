drop sequence user_seq;
drop sequence account_seq;
drop sequence transaction_seq;

create sequence user_seq;
create sequence account_seq;
create sequence transaction_seq;

drop table users cascade constraints;
drop table accounts cascade constraints;
drop table transactions cascade constraints;

create table users (
    user_id number(10),
    user_name varchar(255) not null unique,
    user_password varchar(255) not null,
    super_user number(1) not null check(super_user<2),
    constraint users_pk primary key (user_id)
);

create table accounts (
    account_id number(10),
    user_id number(10) not null,
    balance number(20) default 0 check(balance>=0),
    constraint accounts_pk primary key (account_id),
    constraint accounts_user_id_fk foreign key (user_id) references users(user_id) on delete cascade
);

create table transactions (
    transaction_id number(10),
    user_id number(10) not null,
    account_id number(10) not null,
    amount number(10) not null,
    withdrawal number(1) not null check(withdrawal<2),
    date_of_purchase timestamp,
    constraint transactions_pk primary key (transaction_id),
    constraint transactions_user_id_fk foreign key (user_id) references users(user_id) on delete cascade,
    constraint transactions_account_id_fk foreign key (account_id) references accounts(account_id) on delete cascade
);

CREATE OR REPLACE TRIGGER user_seq_trigger
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    IF :new.user_id IS NULL THEN
    SELECT user_seq.nextval INTO :new.user_id from dual;
    END IF;
END;    
/
CREATE OR REPLACE TRIGGER accounts_seq_trigger
BEFORE INSERT ON accounts
FOR EACH ROW
BEGIN
    IF :new.account_id IS NULL THEN
    SELECT account_seq.nextval INTO :new.account_id from dual;
    END IF;
END;    
/
CREATE OR REPLACE TRIGGER transaction_seq_trigger
BEFORE INSERT ON transactions
FOR EACH ROW
BEGIN
    IF :new.transaction_id IS NULL THEN
    SELECT transaction_seq.nextval INTO :new.transaction_id from dual;
    END IF;
END;    
/
CREATE OR REPLACE PROCEDURE loginUser(
    user_name_in IN users.user_name%TYPE, 
    user_password_in IN users.user_password%TYPE, 
    user_id_out OUT users.user_id%TYPE,
    super_user_out OUT users.super_user%TYPE,
    success_out OUT number)
IS
    l_user_id users.user_id%TYPE;
    l_password users.user_password%TYPE;
    l_super_user users.super_user%TYPE;
BEGIN
    SELECT user_id, user_password, super_user
    INTO l_user_id, l_password, l_super_user
    FROM users
    WHERE user_name = user_name_in;
    IF user_password_in = l_password
    THEN
        success_out := 1;
        user_id_out := l_user_id;
        super_user_out := l_super_user;
    ELSE
        success_out := 0;
    END IF;
END;
/
CREATE OR REPLACE PROCEDURE registerUser(
    user_name_in IN users.user_name%TYPE,
    user_password_in IN users.user_password%TYPE,
    user_id_out OUT users.user_id%TYPE,
    success_out OUT number)
IS
BEGIN
    INSERT INTO users (user_name, user_password, super_user) 
    VALUES(user_name_in, user_password_in, 0);
    success_out := 1;
    user_id_out := user_seq.CURRVAL;
    EXCEPTION
        WHEN OTHERS THEN
            success_out := 0;
    commit;
END;
/
CREATE OR REPLACE PROCEDURE getAllUsers(
    S OUT SYS_REFCURSOR)
IS
BEGIN
    OPEN S FOR
    SELECT * FROM users;
END;
/
create or replace PROCEDURE deleteUser(
    user_id_in IN users.user_id%TYPE,
    delete_success_out OUT NUMBER)
IS
BEGIN
    DELETE FROM users
    WHERE user_id = user_id_in;
    delete_success_out := 1;
    commit;
END;
/
create or replace PROCEDURE updateUser(
    user_id_in IN users.user_id%TYPE,
    user_name_in IN users.user_name%TYPE,
    user_password_in IN users.user_password%TYPE,
    success_out OUT number)
IS
BEGIN
    update users
    set user_name=user_name_in, user_password=user_password_in
    where user_id=user_id_in;
    success_out := 1;
    commit;
END;
/
CREATE OR REPLACE PROCEDURE registerAccount(
    uID_in IN accounts.user_id%TYPE,
    bal_in IN accounts.balance%TYPE,
    account_id_out OUT number,
    success_out OUT number)
IS
BEGIN
    IF bal_in > 0
    THEN
        INSERT INTO accounts (user_id, balance) 
        VALUES(uID_in, bal_in);
        commit;
        INSERT INTO transactions (user_id, account_id, amount, withdrawal, date_of_purchase)
        VALUES(uID_in,account_seq.CURRVAL,bal_in,0,CURRENT_TIMESTAMP);
        commit;
        success_out := 1;
        account_id_out := account_seq.CURRVAL;
    ELSE
        success_out := 0;
    END IF;
END;
/
create or replace PROCEDURE getAllAccounts(
    user_id_in IN users.user_id%TYPE,
    S OUT SYS_REFCURSOR)
IS
BEGIN
    OPEN S FOR
    SELECT * FROM accounts
    WHERE user_id = user_id_in;
END;
/
create or replace PROCEDURE withdrawFromAccount(
    account_id_in IN accounts.account_id%TYPE,
    amount_in IN number,
    success_out OUT number)
IS
    current_balance number(20);
    l_user_id number(10);
BEGIN
    select balance into current_balance 
    from accounts
    where account_id = account_id_in;
    if amount_in <= current_balance
    then
        UPDATE accounts
        SET balance = current_balance - amount_in
        WHERE account_id = account_id_in;
        SELECT user_id into l_user_id
        FROM accounts
        WHERE account_id = account_id_in;
        INSERT INTO transactions (user_id, account_id, amount, withdrawal, date_of_purchase)
        VALUES(l_user_id,account_id_in,amount_in,1,CURRENT_TIMESTAMP);
        success_out := 1;
        commit;
    else
        success_out := 0;
    end if;
END;
/
create or replace PROCEDURE depositToAccount(
    account_id_in IN accounts.account_id%TYPE,
    amount_in IN number)
IS
    l_user_id number(10);
BEGIN
    UPDATE accounts
    SET balance = balance + amount_in
    WHERE account_id = account_id_in;
    SELECT user_id into l_user_id
    FROM accounts
    WHERE account_id = account_id_in;
    INSERT INTO transactions (user_id, account_id, amount, withdrawal, date_of_purchase)
    VALUES(l_user_id,account_id_in,amount_in,0,CURRENT_TIMESTAMP);
    commit;
END;
/

create or replace PROCEDURE deleteAccount(
    account_id_in IN accounts.account_id%TYPE,
    success_out OUT number)
IS
    current_balance number(20);
BEGIN
    select balance into current_balance 
    from accounts
    where account_id = account_id_in;
    if current_balance = 0
    then
        DELETE FROM accounts
        WHERE account_id = account_id_in;
        success_out := 1;
        commit;
    else
        success_out := 0;
    end if;
END;
/

create or replace PROCEDURE getAllTransactionsByAccountID(
    account_id_in IN users.user_id%TYPE,
    S OUT SYS_REFCURSOR)
IS
BEGIN
    OPEN S FOR
    SELECT * FROM transactions
    WHERE account_id = account_id_in;
END;
/