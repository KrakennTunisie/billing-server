ALTER TABLE mail_jobs
DROP CONSTRAINT IF EXISTS mail_jobs_status_check;

ALTER TABLE mail_jobs
    ADD CONSTRAINT mail_jobs_status_check
        CHECK (status IN (
                          'CREATED',
                          'DELIVERED',
                          'SENT',
                          'FAILED',
                          'OTHER'
            ));