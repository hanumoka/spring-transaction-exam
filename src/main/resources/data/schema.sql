-- ===============================
-- ACCOUNT_ACTION 테이블 생성
-- ===============================
-- 기존 테이블이 있다면 안전하게 제거
DROP TABLE IF EXISTS account_action CASCADE;

-- ID 생성을 위한 시퀀스 재설정
DROP SEQUENCE IF EXISTS account_action_id_seq;
CREATE SEQUENCE account_action_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE CACHE 1;

-- account_action 테이블 생성
CREATE TABLE account_action
(
    id              BIGINT DEFAULT nextval('account_action_id_seq') NOT NULL,
    account_id      BIGINT                                          NOT NULL,
    login_count     BIGINT NULL DEFAULT 0,
    last_login_at   TIMESTAMP NULL,
    log_out_count   BIGINT NULL DEFAULT 0,
    last_log_out_at TIMESTAMP NULL,
    created_by      VARCHAR(255) NULL,
    created_at      TIMESTAMP NULL,
    updated_by      VARCHAR(255) NULL,
    updated_at      TIMESTAMP NULL,
    version         INTEGER,

    CONSTRAINT pk_account_action PRIMARY KEY (id),
    CONSTRAINT uk_account_action_account_id UNIQUE (account_id)
);

-- 테이블에 대한 코멘트
COMMENT
ON TABLE account_action IS '계정 활동 정보';

-- 컬럼에 대한 코멘트
COMMENT
ON COLUMN account_action.id IS 'pk';
COMMENT
ON COLUMN account_action.account_id IS '계정 ID (account 테이블의 FK)';
COMMENT
ON COLUMN account_action.login_count IS '로그인 횟수';
COMMENT
ON COLUMN account_action.last_login_at IS '마지막 로그인 일시';
COMMENT
ON COLUMN account_action.log_out_count IS '로그아웃 횟수';
COMMENT
ON COLUMN account_action.last_log_out_at IS '마지막 로그아웃 일시';
COMMENT
ON COLUMN account_action.created_by IS '생성자';
COMMENT
ON COLUMN account_action.created_at IS '생성일시';
COMMENT
ON COLUMN account_action.updated_by IS '수정자';
COMMENT
ON COLUMN account_action.updated_at IS '수정일시';
COMMENT
ON COLUMN account_action.version IS '버전 (낙관적 락에 사용)';


-- 조회 성능 최적화를 위한 인덱스
CREATE INDEX idx_account_action_account_id ON account_action (account_id);

-- 인덱스에 대한 코멘트
COMMENT
ON INDEX idx_account_action_account_id IS '계정 ID로 계정 활동 정보를 조회하기 위한 인덱스';
COMMENT
ON CONSTRAINT uk_account_action_account_id ON account_action IS '한 계정당 하나의 활동 정보만 존재할 수 있도록 보장하는 고유 제약조건';