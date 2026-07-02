DROP TABLE document_content CASCADE;

DROP TABLE invoice_counters CASCADE;

DROP TABLE invoice_credit_note_events CASCADE;

DROP TABLE invoices_events CASCADE;

DROP TABLE mail_attachment_metadata_entity CASCADE;

ALTER TABLE invoices
ALTER
COLUMN total_incl_taxtnd TYPE DECIMAL USING (total_incl_taxtnd::DECIMAL);

ALTER TABLE invoices
DROP CONSTRAINT invoices_invoice_status_check;

ALTER TABLE invoices
    ADD CONSTRAINT invoices_invoice_status_check
        CHECK (
            invoice_status IN (
                               'DRAFT',
                               'TO_PAY',
                               'TO_COLLECT',
                               'PARTIALLY_PAID',
                               'PAID',
                               'OVERDUE',
                               'ARCHIVED',
                               'CANCELLED'
                )
            );

