CREATE TABLE public.address (
                                id_address uuid NOT NULL,
                                address_type character varying(255),
                                city character varying(255),
                                region character varying(255),
                                state character varying(255),
                                street character varying(255),
                                street2 character varying(255),
                                zip_code character varying(255)
);


ALTER TABLE public.address OWNER TO postgres;

--
-- Name: attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attachments (
                                    id uuid NOT NULL,
                                    mail_job_id uuid NOT NULL,
                                    file_name character varying(255) NOT NULL,
                                    file_path character varying(255) NOT NULL
);


ALTER TABLE public.attachments OWNER TO postgres;

--
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit_logs (
                                   event_date timestamp(6) without time zone,
                                   entity_id uuid,
                                   id_audit_log uuid NOT NULL,
                                   partner_id uuid,
                                   description character varying(1000),
                                   audit_event_trigger character varying(255),
                                   audit_event_type character varying(255),
                                   entity_name character varying(255),
                                   triggered_by character varying(255),
                                   CONSTRAINT audit_logs_audit_event_trigger_check CHECK (((audit_event_trigger)::text = ANY ((ARRAY['USER'::character varying, 'SYSTEM'::character varying, 'TTN'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT audit_logs_audit_event_type_check CHECK (((audit_event_type)::text = ANY ((ARRAY['CREATED'::character varying, 'UPDATED'::character varying, 'DELETED'::character varying, 'STATUS_CHANGED'::character varying, 'PAYMENT_REGISTERED'::character varying, 'PAYMENT_METHOD_UPDATED'::character varying, 'CANCELLED'::character varying, 'DOCUMENT_ATTACHED'::character varying, 'DOCUMENT_VALIDATED'::character varying, 'SIGNATURE_REQUESTED'::character varying, 'SIGNATURE_SUCCEEDED'::character varying, 'SIGNATURE_FAILED'::character varying, 'TTN_SUBMISSION_REQUESTED'::character varying, 'TTN_SUBMITTED'::character varying, 'TTN_ACCEPTED'::character varying, 'TTN_REJECTED'::character varying, 'FX_RATE_APPLIED'::character varying, 'REFUND_REQUESTED'::character varying, 'REFUND_COMPLETED'::character varying])::text[])))
);


ALTER TABLE public.audit_logs OWNER TO postgres;

--
-- Name: base_item_operation_category_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.base_item_operation_category_entity (
                                                            is_active boolean NOT NULL,
                                                            created_at timestamp(6) without time zone NOT NULL,
                                                            updated_at timestamp(6) without time zone NOT NULL,
                                                            id_operation_category uuid CONSTRAINT base_item_operation_category_ent_id_operation_category_not_null NOT NULL,
                                                            code character varying(255),
                                                            description character varying(255),
                                                            label character varying(255)
);


ALTER TABLE public.base_item_operation_category_entity OWNER TO postgres;

--
-- Name: document_content; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_content (
                                         id uuid NOT NULL,
                                         file_data oid NOT NULL,
                                         mime_type character varying(255) NOT NULL,
                                         original_file_name character varying(255) NOT NULL
);


ALTER TABLE public.document_content OWNER TO postgres;

--
-- Name: document_contents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_contents (
                                          file_size bigint NOT NULL,
                                          id_document uuid NOT NULL,
                                          file_name character varying(255),
                                          mime_type character varying(255),
                                          file_content bytea NOT NULL
);


ALTER TABLE public.document_contents OWNER TO postgres;

--
-- Name: document_number_counters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_number_counters (
                                                 year integer NOT NULL,
                                                 last_sequence bigint NOT NULL,
                                                 id uuid NOT NULL,
                                                 sequence_type character varying(255) NOT NULL,
                                                 CONSTRAINT document_number_counters_sequence_type_check CHECK (((sequence_type)::text = ANY ((ARRAY['INVOICE'::character varying, 'CREDIT_NOTE'::character varying, 'PURCHASE_ORDER'::character varying, 'PAYMENT'::character varying])::text[])))
);


ALTER TABLE public.document_number_counters OWNER TO postgres;

--
-- Name: documents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documents (
                                  uploaded_at timestamp(6) without time zone NOT NULL,
                                  contract_document_id uuid,
                                  document_content_id uuid,
                                  id_document uuid NOT NULL,
                                  rne_document_id uuid,
                                  mime_type character varying(120) NOT NULL,
                                  document_type character varying(255),
                                  file_name character varying(255),
                                  hash character varying(255),
                                  storage_mode character varying(255),
                                  storageurl character varying(255),
                                  CONSTRAINT documents_document_type_check CHECK (((document_type)::text = ANY ((ARRAY['INVOICE'::character varying, 'PATENT'::character varying, 'RNE'::character varying, 'CONTRACT'::character varying, 'PURCHASE_ORDER'::character varying, 'PAYMENT'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT documents_storage_mode_check CHECK (((storage_mode)::text = ANY ((ARRAY['CLOUD_URL'::character varying, 'DATABASE'::character varying, 'FILESYSTEM'::character varying])::text[])))
);


ALTER TABLE public.documents OWNER TO postgres;

--
-- Name: exchange_rates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exchange_rates (
                                       from_currency character varying(3) NOT NULL,
                                       quote numeric(19,6) NOT NULL,
                                       rate_date date NOT NULL,
                                       to_currency character varying(3) NOT NULL,
                                       created_at timestamp(6) without time zone NOT NULL,
                                       fetched_at timestamp(6) without time zone NOT NULL,
                                       updated_at timestamp(6) without time zone,
                                       id_exchange_rate uuid NOT NULL,
                                       source character varying(100),
                                       CONSTRAINT exchange_rates_from_currency_check CHECK (((from_currency)::text = ANY ((ARRAY['TND'::character varying, 'EUR'::character varying, 'USD'::character varying])::text[]))),
    CONSTRAINT exchange_rates_to_currency_check CHECK (((to_currency)::text = ANY ((ARRAY['TND'::character varying, 'EUR'::character varying, 'USD'::character varying])::text[])))
);


ALTER TABLE public.exchange_rates OWNER TO postgres;
--
-- Name: invoice_credit_note_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_credit_note_events (
                                                   event_date timestamp(6) without time zone,
                                                   credit_note_id uuid NOT NULL,
                                                   id_invoice_event uuid NOT NULL,
                                                   description character varying(1000),
                                                   event_trigger character varying(255),
                                                   event_type character varying(255),
                                                   triggered_by character varying(255),
                                                   CONSTRAINT invoice_credit_note_events_event_trigger_check CHECK (((event_trigger)::text = ANY ((ARRAY['USER'::character varying, 'SYSTEM'::character varying, 'TTN'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT invoice_credit_note_events_event_type_check CHECK (((event_type)::text = ANY ((ARRAY['CREATED'::character varying, 'UPDATED'::character varying, 'STATUS_CHANGED'::character varying, 'REFUND_REQUESTED'::character varying, 'REFUND_COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.invoice_credit_note_events OWNER TO postgres;

--
-- Name: invoice_credit_notes_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_credit_notes_items (
                                                   quantity integer,
                                                   id_invoice_credit_note_item uuid NOT NULL,
                                                   invoice_credit_note_id uuid NOT NULL,
                                                   invoice_item_id uuid
);


ALTER TABLE public.invoice_credit_notes_items OWNER TO postgres;

--
-- Name: invoice_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_items (
                                      credited_quantity integer DEFAULT 0 NOT NULL,
                                      discount_value double precision,
                                      quantity integer,
                                      total_price_inc_tax double precision,
                                      unity_priceexcl_tax double precision,
                                      vat_rate double precision,
                                      id_invoice_item uuid NOT NULL,
                                      invoice_id uuid,
                                      purchase_order_item_id_purchase_order_item uuid,
                                      description character varying(500) NOT NULL,
                                      discount_type character varying(255),
                                      operation_category character varying(255),
                                      CONSTRAINT invoice_items_discount_type_check CHECK (((discount_type)::text = ANY ((ARRAY['PERCENTAGE'::character varying, 'AMOUNT'::character varying])::text[])))
);


ALTER TABLE public.invoice_items OWNER TO postgres;

--
-- Name: invoices; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoices (
                                 applied_exchange_rate double precision,
                                 remaining_amount double precision,
                                 total_incl_taxtnd numeric(38,2),
                                 vat_rate double precision,
                                 created_at timestamp(6) without time zone NOT NULL,
                                 due_date timestamp(6) without time zone,
                                 exchange_rate_reference_date timestamp(6) without time zone,
                                 issue_date timestamp(6) without time zone,
                                 updated_at timestamp(6) without time zone NOT NULL,
                                 id_invoice uuid NOT NULL,
                                 invoice_document_id uuid,
                                 partner_id uuid,
                                 purchase_order_id uuid,
                                 invoice_type character varying(31) NOT NULL,
                                 comment character varying(255),
                                 complianceqrcode character varying(255),
                                 currency character varying(255),
                                 exchange_rate_source character varying(255),
                                 invoice_compliance_status character varying(255),
                                 invoice_status character varying(255),
                                 payment_condition character varying(255),
                                 payment_method character varying(255),
                                 reference character varying(255),
                                 CONSTRAINT invoices_currency_check CHECK (((currency)::text = ANY ((ARRAY['TND'::character varying, 'EUR'::character varying, 'USD'::character varying])::text[]))),
    CONSTRAINT invoices_exchange_rate_source_check CHECK (((exchange_rate_source)::text = ANY ((ARRAY['CENTRAL_BANK'::character varying, 'EUROPEAN_CENTRAL_BANK'::character varying, 'COMMERCIAL_BANK'::character varying, 'EXTERNAL_API'::character varying, 'MANUAL'::character varying])::text[]))),
    CONSTRAINT invoices_invoice_compliance_status_check CHECK (((invoice_compliance_status)::text = ANY ((ARRAY['RECEIVED'::character varying, 'SIGNING_PENDING'::character varying, 'SIGNING_FAILED'::character varying, 'SIGNING_SUCCEEDED'::character varying, 'TTN_PENDING'::character varying, 'TTN_SUBMITTED'::character varying, 'TTN_ACCEPTED'::character varying, 'TTN_REJECTED'::character varying, 'COMPLETED'::character varying, 'FAILED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT invoices_invoice_status_check CHECK (((invoice_status)::text = ANY ((ARRAY['DRAFT'::character varying, 'TO_PAY'::character varying, 'TO_COLLECT'::character varying, 'PARTIALLY_PAID'::character varying, 'PAID'::character varying, 'OVERDUE'::character varying, 'ARCHIVED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT invoices_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['BANK_TRANSFER'::character varying, 'CHECK'::character varying, 'CASH'::character varying])::text[])))
);


ALTER TABLE public.invoices OWNER TO postgres;

--
-- Name: invoices_credit_notes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoices_credit_notes (
                                              issue_date timestamp(6) without time zone,
                                              id_invoice_credit_note_entity uuid NOT NULL,
                                              invoice_credit_note_document_id uuid,
                                              invoice_id uuid,
                                              compliance_status character varying(255),
                                              description character varying(255),
                                              invoice_credit_note_number character varying(255),
                                              invoice_credit_note_status character varying(255),
                                              motif character varying(255),
                                              qr_code character varying(255),
                                              CONSTRAINT invoices_credit_notes_compliance_status_check CHECK (((compliance_status)::text = ANY ((ARRAY['RECEIVED'::character varying, 'SIGNING_PENDING'::character varying, 'SIGNING_FAILED'::character varying, 'SIGNING_SUCCEEDED'::character varying, 'TTN_PENDING'::character varying, 'TTN_SUBMITTED'::character varying, 'TTN_ACCEPTED'::character varying, 'TTN_REJECTED'::character varying, 'COMPLETED'::character varying, 'FAILED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT invoices_credit_notes_invoice_credit_note_status_check CHECK (((invoice_credit_note_status)::text = ANY ((ARRAY['REFUNDED'::character varying, 'NOT_REFUNDED'::character varying, 'IN_PROGRESS'::character varying, 'CANCELLED'::character varying, 'ARCHIVED'::character varying, 'DRAFT'::character varying])::text[])))
);


ALTER TABLE public.invoices_credit_notes OWNER TO postgres;

--
-- Name: invoices_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoices_events (
                                        event_date timestamp(6) without time zone,
                                        id_invoice_event uuid NOT NULL,
                                        invoice_id uuid NOT NULL,
                                        description character varying(1000),
                                        event_trigger character varying(255),
                                        invoice_event_type character varying(255),
                                        triggered_by character varying(255),
                                        CONSTRAINT invoices_events_event_trigger_check CHECK (((event_trigger)::text = ANY ((ARRAY['USER'::character varying, 'SYSTEM'::character varying, 'TTN'::character varying, 'OTHER'::character varying])::text[]))),
    CONSTRAINT invoices_events_invoice_event_type_check CHECK (((invoice_event_type)::text = ANY ((ARRAY['CREATED'::character varying, 'UPDATED'::character varying, 'STATUS_CHANGED'::character varying, 'PAYMENT_REGISTERED'::character varying, 'PAYMENT_METHOD_UPDATED'::character varying, 'CANCELLED'::character varying, 'DOCUMENT_ATTACHED'::character varying, 'DOCUMENT_VALIDATED'::character varying, 'SIGNATURE_REQUESTED'::character varying, 'SIGNATURE_SUCCEEDED'::character varying, 'SIGNATURE_FAILED'::character varying, 'TTN_SUBMISSION_REQUESTED'::character varying, 'TTN_SUBMITTED'::character varying, 'TTN_ACCEPTED'::character varying, 'TTN_REJECTED'::character varying, 'FX_RATE_APPLIED'::character varying])::text[])))
);


ALTER TABLE public.invoices_events OWNER TO postgres;

--
-- Name: mail_jobs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mail_jobs (
                                  date timestamp(6) without time zone NOT NULL,
                                  id uuid NOT NULL,
                                  body character varying(5000),
                                  status character varying(255) NOT NULL,
                                  subject character varying(255) NOT NULL,
                                  to_email character varying(255) NOT NULL,
                                  event_id character varying(255) NOT NULL,
                                  CONSTRAINT mail_jobs_status_check CHECK (((status)::text = ANY ((ARRAY['CREATED'::character varying, 'DELIVERED'::character varying, 'SENT'::character varying, 'FAILED'::character varying, 'OTHER'::character varying])::text[])))
);


ALTER TABLE public.mail_jobs OWNER TO postgres;


--
-- Name: partners; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.partners (
                                 active boolean NOT NULL,
                                 enable_portal boolean NOT NULL,
                                 billing_address_id uuid,
                                 id_partner uuid NOT NULL,
                                 patente_document_id uuid,
                                 shipping_address_id uuid,
                                 partner_type character varying(31) NOT NULL,
                                 company_name character varying(255),
                                 currency character varying(255),
                                 display_name character varying(255),
                                 email character varying(255),
                                 iban character varying(255),
                                 language character varying(255),
                                 marital_status character varying(255),
                                 partner_name character varying(255),
                                 payment_condition character varying(255),
                                 personnel_phone_number character varying(255),
                                 professionnal_phone_number character varying(255),
                                 tax_rate character varying(255),
                                 tax_registration_number character varying(255),
                                 CONSTRAINT partners_currency_check CHECK (((currency)::text = ANY ((ARRAY['TND'::character varying, 'EUR'::character varying, 'USD'::character varying])::text[])))
);


ALTER TABLE public.partners OWNER TO postgres;

--
-- Name: payment_condition_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_condition_entity (
                                                 is_active boolean NOT NULL,
                                                 created_at timestamp(6) without time zone NOT NULL,
                                                 updated_at timestamp(6) without time zone NOT NULL,
                                                 id_payment_condition uuid NOT NULL,
                                                 code character varying(255),
                                                 description character varying(255),
                                                 label character varying(255)
);


ALTER TABLE public.payment_condition_entity OWNER TO postgres;

--
-- Name: payment_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_entity (
                                       amount numeric(15,3) NOT NULL,
                                       currency character varying(3) NOT NULL,
                                       payment_date date NOT NULL,
                                       created_at timestamp(6) without time zone,
                                       updated_at timestamp(6) without time zone,
                                       id_payment uuid NOT NULL,
                                       invoice_id uuid NOT NULL,
                                       payment_document_id uuid,
                                       method character varying(30) NOT NULL,
                                       reference character varying(100),
                                       note character varying(500),
                                       payment_status character varying(255),
                                       CONSTRAINT payment_entity_method_check CHECK (((method)::text = ANY ((ARRAY['BANK_TRANSFER'::character varying, 'CHECK'::character varying, 'CASH'::character varying])::text[])))
);


ALTER TABLE public.payment_entity OWNER TO postgres;

--
-- Name: purchase_order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_order_items (
                                             invoiced_quantity integer,
                                             quantity integer,
                                             total_price_inc_tax double precision,
                                             unity_priceexcl_tax double precision,
                                             vat_rate double precision,
                                             id_purchase_order_item uuid NOT NULL,
                                             purchase_order_id uuid NOT NULL,
                                             description character varying(500) NOT NULL,
                                             operation_category character varying(255),
                                             discount_type character varying(255),
                                             discount_value double precision
);


ALTER TABLE public.purchase_order_items OWNER TO postgres;

--
-- Name: purchase_orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_orders (
                                        applied_exchange_rate double precision,
                                        vat_rate double precision,
                                        created_at timestamp(6) without time zone NOT NULL,
                                        exchange_rate_reference_date timestamp(6) without time zone,
                                        issue_date timestamp(6) without time zone,
                                        updated_at timestamp(6) without time zone NOT NULL,
                                        id_purchase_order uuid NOT NULL,
                                        partner_id uuid,
                                        purchase_order_document_id uuid,
                                        purchase_order_type character varying(31) NOT NULL,
                                        currency character varying(255),
                                        exchange_rate_source character varying(255),
                                        payment_condition character varying(255),
                                        payment_method character varying(255),
                                        purchase_order_status character varying(255) NOT NULL,
                                        reference character varying(255),
                                        CONSTRAINT purchase_orders_currency_check CHECK (((currency)::text = ANY ((ARRAY['TND'::character varying, 'EUR'::character varying, 'USD'::character varying])::text[]))),
    CONSTRAINT purchase_orders_exchange_rate_source_check CHECK (((exchange_rate_source)::text = ANY ((ARRAY['CENTRAL_BANK'::character varying, 'EUROPEAN_CENTRAL_BANK'::character varying, 'COMMERCIAL_BANK'::character varying, 'EXTERNAL_API'::character varying, 'MANUAL'::character varying])::text[]))),
    CONSTRAINT purchase_orders_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['BANK_TRANSFER'::character varying, 'CHECK'::character varying, 'CASH'::character varying])::text[])))
);


ALTER TABLE public.purchase_orders OWNER TO postgres;

--
-- Name: tva_rate_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tva_rate_entity (
                                        is_active boolean NOT NULL,
                                        created_at timestamp(6) without time zone NOT NULL,
                                        updated_at timestamp(6) without time zone NOT NULL,
                                        id_tva_rate uuid NOT NULL,
                                        code character varying(255),
                                        description character varying(255),
                                        label character varying(255)
);


ALTER TABLE public.tva_rate_entity OWNER TO postgres;