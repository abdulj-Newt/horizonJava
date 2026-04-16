ALTER SESSION SET CONTAINER = FREEPDB1;
/

ALTER SESSION SET CURRENT_SCHEMA = APPUSER;
/

-- Add Money Stored Procedure
-- This procedure handles adding money to customer accounts
-- Author: System Generated
-- Date: July 25, 2025

CREATE OR REPLACE PROCEDURE ADD_MONEY (
    p_customer_id IN NUMBER,
    p_amount IN NUMBER
)
AS
    v_account_id NUMBER;
    v_transaction_id NUMBER;
BEGIN
    -- Get the customer's account
    SELECT id INTO v_account_id FROM accounts WHERE customer_id = p_customer_id;
    
    -- Add amount to customer's account
    UPDATE accounts SET balance = balance + p_amount WHERE id = v_account_id;
    
    -- Insert transaction record for money addition
    INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)
    VALUES (TRANSACTION_SEQ.NEXTVAL, v_account_id, 'ADD_MONEY', p_amount, SYSDATE, 
            p_customer_id, p_customer_id);
    
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Customer or account not found');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
