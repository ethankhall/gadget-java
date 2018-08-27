CREATE TABLE redirect (
  redirect_id  BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  alias        VARCHAR(256)  NOT NULL UNIQUE,
  variables    VARCHAR(256),
  destination  VARCHAR(4096) NOT NULL,
  user         VARCHAR(256)  NOT NULL,
  update_count BIGINT        NOT NULL,
  created_at   TIMESTAMP(6)  NOT NULL
);

CREATE TABLE redirect_history (
  redirect_history_id BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  alias               VARCHAR(256)  NOT NULL,
  variables           VARCHAR(256),
  destination         VARCHAR(4096) NOT NULL,
  user                VARCHAR(256)  NOT NULL,
  created_at          TIMESTAMP(6)  NOT NULL,
  deleted_at          TIMESTAMP(6)  NOT NULL
);
