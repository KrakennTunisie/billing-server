ALTER TABLE mail_notification_attachments
    ADD COLUMN IF NOT EXISTS id_document VARCHAR(255);