INSERT INTO users (user_name, user_password, super_user) VALUES ('foo', 'bar', 0);
DECLARE 
    outparam1 number(10);
    outparam2 varchar2(255);
BEGIN
    
    registeruser('kenbone1','4prez2',outparam1,outparam2);

END;
