DROP TABLE IF EXISTS public.quote cascade;

CREATE TABLE public.quote
(
    ticker     varchar NOT NULL,
    last_price float8  NOT NULL,
    bid_price  float8  NOT NULL,
    bid_size   int4    NOT NULL,
    ask_price  float8  NOT NULL,
    ask_size   int4    NOT NULL,
    CONSTRAINT quote_pk PRIMARY KEY (ticker)
);