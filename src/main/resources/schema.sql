CREATE OR REPLACE PROCEDURE TRANSFER_MONEY (
    p_from_customer_id IN NUMBER,
    p_to_customer_id IN NUMBER,
    p_amount IN NUMBER
)
AS
    v_from_account_id NUMBER;
    v_to_account_id NUMBER;
    v_from_balance NUMBER;
    v_transaction_id NUMBER;
BEGIN
    SELECT id INTO v_from_account_id FROM accounts WHERE customer_id = p_from_customer_id;
    SELECT id INTO v_to_account_id FROM accounts WHERE customer_id = p_to_customer_id;

    SELECT balance INTO v_from_balance FROM accounts WHERE id = v_from_account_id;

    IF v_from_balance >= p_amount THEN
        UPDATE accounts SET balance = balance - p_amount WHERE id = v_from_account_id;
        UPDATE accounts SET balance = balance + p_amount WHERE id = v_to_account_id;

        INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)
        VALUES (TRANSACTION_SEQ.NEXTVAL, v_from_account_id, 'DEBIT', -p_amount, SYSDATE, p_from_customer_id, p_to_customer_id);

        INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)
        VALUES (TRANSACTION_SEQ.NEXTVAL, v_to_account_id, 'CREDIT', p_amount, SYSDATE, p_from_customer_id, p_to_customer_id);

        COMMIT;
    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient funds');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Customer or account not found');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
