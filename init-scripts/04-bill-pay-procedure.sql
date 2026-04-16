ALTER SESSION SET CONTAINER = FREEPDB1;
/

ALTER SESSION SET CURRENT_SCHEMA = APPUSER;
/

-- Bill Pay Stored Procedure
-- This procedure handles bill payments for customers
-- Author: System Generated
-- Date: July 25, 2025

CREATE OR REPLACE PROCEDURE PAY_BILL (
    p_customer_id IN NUMBER,
    p_bill_type IN VARCHAR2,
    p_account_number IN VARCHAR2,
    p_amount IN NUMBER
)
AS
    v_account_id NUMBER;
    v_balance NUMBER;
    v_transaction_id NUMBER;
    v_transaction_type VARCHAR2(50);
BEGIN
    -- Get the customer's account
    SELECT id INTO v_account_id FROM accounts WHERE customer_id = p_customer_id;
    
    -- Get current balance
    SELECT balance INTO v_balance FROM accounts WHERE id = v_account_id;
    
    -- Check if sufficient funds
    IF v_balance >= p_amount THEN
        -- Deduct amount from customer's account
        UPDATE accounts SET balance = balance - p_amount WHERE id = v_account_id;
        
        -- Create transaction type based on bill type
        v_transaction_type := 'BILL_PAY_' || UPPER(p_bill_type);
        
        -- Insert transaction record (matching the existing table structure)
        INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)
        VALUES (TRANSACTION_SEQ.NEXTVAL, v_account_id, v_transaction_type, -p_amount, SYSDATE, 
                p_customer_id, p_customer_id);
        
        COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient funds for bill payment');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Customer or account not found');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
