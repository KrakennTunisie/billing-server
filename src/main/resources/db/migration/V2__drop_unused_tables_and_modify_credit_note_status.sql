ALTER TABLE invoices_credit_notes
DROP
COLUMN compliance_status;

ALTER TABLE invoices_credit_notes
DROP
COLUMN invoice_credit_note_status;

ALTER TABLE invoices_credit_notes
    ADD compliance_status VARCHAR(255);

ALTER TABLE invoices_credit_notes
    ADD invoice_credit_note_status VARCHAR(255);

ALTER TABLE invoices
ALTER
COLUMN total_incl_taxtnd TYPE DECIMAL USING (total_incl_taxtnd::DECIMAL);