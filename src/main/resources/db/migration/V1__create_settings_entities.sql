CREATE TABLE IF NOT EXISTS base_item_operation_category_entity
(
    id_operation_category UUID                        NOT NULL,
    code                  VARCHAR(255),
    label                 VARCHAR(255),
    description           VARCHAR(255),
    is_active             BOOLEAN                     NOT NULL,
    created_at            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_baseitemoperationcategoryentity
    PRIMARY KEY (id_operation_category)
    );

CREATE TABLE IF NOT EXISTS payment_condition_entity
(
    id_payment_condition UUID                        NOT NULL,
    code                 VARCHAR(255),
    label                VARCHAR(255),
    description          VARCHAR(255),
    is_active            BOOLEAN                     NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_paymentconditionentity
    PRIMARY KEY (id_payment_condition)
    );

CREATE TABLE IF NOT EXISTS tva_rate_entity
(
    id_tva_rate UUID                        NOT NULL,
    code        VARCHAR(255),
    label       VARCHAR(255),
    description VARCHAR(255),
    is_active   BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tvarateentity
    PRIMARY KEY (id_tva_rate)
    );