CREATE TABLE "tbl_admins" (
"id_admin" int4 NOT NULL DEFAULT nextval('tbl_admins_id_admin_seq'::regclass),
"email" varchar(50) COLLATE "default" NOT NULL,
"password" varchar(50) COLLATE "default" NOT NULL,
CONSTRAINT "tbl_admins_pk" PRIMARY KEY ("id_admin") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_admins" OWNER TO "postgres";

CREATE TABLE "tbl_apply" (
"id_apply" int4 NOT NULL DEFAULT nextval('tbl_apply_id_apply_seq'::regclass),
"email" varchar(50) COLLATE "default" NOT NULL,
"name" varchar(50) COLLATE "default" NOT NULL,
"created_at" text COLLATE "default" NOT NULL,
"status" int4 NOT NULL,
"sent_mail" int4 NOT NULL,
"id_user" int4 NOT NULL,
"id_job" int4 NOT NULL,
"profile" text COLLATE "default" NOT NULL,
CONSTRAINT "tbl_apply_pk" PRIMARY KEY ("id_apply") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_apply" OWNER TO "postgres";

CREATE TABLE "tbl_companies" (
"id_company" int4 NOT NULL DEFAULT nextval('tbl_companies_id_company_seq'::regclass),
"name" text COLLATE "default" NOT NULL,
"type" varchar(50) COLLATE "default" NOT NULL,
"member" varchar(50) COLLATE "default" NOT NULL,
"country" varchar(50) COLLATE "default" NOT NULL,
"thumbnail" text COLLATE "default" NOT NULL,
"image" text COLLATE "default" NOT NULL,
"time_for_work" text COLLATE "default" NOT NULL,
"address" text COLLATE "default" NOT NULL,
"contact" text COLLATE "default" NOT NULL,
"description" text COLLATE "default" NOT NULL,
"over_time" text COLLATE "default" NOT NULL,
"title" text COLLATE "default" NOT NULL,
CONSTRAINT "tbl_companies_pk" PRIMARY KEY ("id_company") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_companies" OWNER TO "postgres";

CREATE TABLE "tbl_company_comments" (
"id_comment" int4 NOT NULL DEFAULT nextval('tbl_company_comments_id_comment_seq'::regclass),
"comment" text COLLATE "default" NOT NULL,
"created_at" text COLLATE "default" NOT NULL,
"id_user" int4 NOT NULL,
"id_company" int4 NOT NULL,
CONSTRAINT "tbl_company_comments_pk" PRIMARY KEY ("id_comment") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_company_comments" OWNER TO "postgres";

CREATE TABLE "tbl_hrs" (
"id_hr" int4 NOT NULL DEFAULT nextval('tbl_hrs_id_hr_seq'::regclass),
"id_user" int4 NOT NULL,
"id_company" int4 NOT NULL,
"code_confirm" int4 NOT NULL,
CONSTRAINT "tbl_hrs_pk" PRIMARY KEY ("id_hr") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_hrs" OWNER TO "postgres";

CREATE TABLE "tbl_images" (
"id_image" int4 NOT NULL DEFAULT nextval('tbl_images_id_image_seq'::regclass),
"url" text COLLATE "default" NOT NULL,
"id_job" int4 NOT NULL,
CONSTRAINT "tbl_images_pk" PRIMARY KEY ("id_image") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_images" OWNER TO "postgres";

CREATE TABLE "tbl_job_comments" (
"id_comment" int4 NOT NULL DEFAULT nextval('tbl_job_comments_id_comment_seq'::regclass),
"comment" text COLLATE "default" NOT NULL,
"created_at" text COLLATE "default" NOT NULL,
"id_user" int4 NOT NULL,
"id_job" int4 NOT NULL,
CONSTRAINT "tbl_job_comments_pk" PRIMARY KEY ("id_comment") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_job_comments" OWNER TO "postgres";

CREATE TABLE "tbl_jobs" (
"id_job" int4 NOT NULL DEFAULT nextval('tbl_jobs_id_job_seq'::regclass),
"title" text COLLATE "default" NOT NULL,
"thumbnail" text COLLATE "default" NOT NULL,
"information" text COLLATE "default" NOT NULL,
"address" text COLLATE "default" NOT NULL,
"join_date" text COLLATE "default" NOT NULL,
"estimatetime" int4 NOT NULL,
"lock" int4 NOT NULL,
"salary" int4 NOT NULL,
"skills" text COLLATE "default" NOT NULL,
"fast_info" text COLLATE "default" NOT NULL,
"id_company" int4 NOT NULL,
CONSTRAINT "tbl_jobs_pk" PRIMARY KEY ("id_job") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_jobs" OWNER TO "postgres";

CREATE TABLE "tbl_notifications" (
"id_noti" int4 NOT NULL DEFAULT nextval('tbl_notifications_id_noti_seq'::regclass),
"skill" text COLLATE "default" NOT NULL,
"email" varchar(50) COLLATE "default" NOT NULL,
CONSTRAINT "tbl_notifications_pk" PRIMARY KEY ("id_noti") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_notifications" OWNER TO "postgres";

CREATE TABLE "tbl_profiles" (
"id_profile" int4 NOT NULL DEFAULT nextval('tbl_profiles_id_profile_seq'::regclass),
"url" text COLLATE "default" NOT NULL,
"id_user" int4 NOT NULL,
CONSTRAINT "tbl_profiles_pk" PRIMARY KEY ("id_profile") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_profiles" OWNER TO "postgres";

CREATE TABLE "tbl_sessions" (
"id_session" int4 NOT NULL DEFAULT nextval('tbl_sessions_id_session_seq'::regclass),
"token" text COLLATE "default" NOT NULL,
"id_user" int4 NOT NULL,
CONSTRAINT "tbl_sessions_pk" PRIMARY KEY ("id_session") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_sessions" OWNER TO "postgres";

CREATE TABLE "tbl_user_rank" (
"id_user_rank" int4 NOT NULL,
"point" int4 NOT NULL,
"id_user" int4 NOT NULL,
"id_company" int4 NOT NULL,
CONSTRAINT "tbl_user_rank_pk" PRIMARY KEY ("id_user_rank") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_user_rank" OWNER TO "postgres";

CREATE TABLE "tbl_users" (
"id_user" int4 NOT NULL DEFAULT nextval('tbl_users_id_user_seq'::regclass),
"email" varchar(50) COLLATE "default" NOT NULL,
"password" varchar(50) COLLATE "default" NOT NULL,
"name" varchar(50) COLLATE "default" NOT NULL,
"phone" varchar(12) COLLATE "default" NOT NULL,
"image" text COLLATE "default" NOT NULL,
"gender" varchar(5) COLLATE "default" NOT NULL,
"created_at" text COLLATE "default" NOT NULL,
"permission" int4 NOT NULL,
"lock" int4 NOT NULL,
"target" text COLLATE "default" NOT NULL,
"birthday" text COLLATE "default" NOT NULL,
"skype" text COLLATE "default" NOT NULL,
"address" text COLLATE "default" NOT NULL,
"education" text COLLATE "default" NOT NULL,
"experience" text COLLATE "default" NOT NULL,
"hard_skill" text COLLATE "default" NOT NULL,
"soft_skill" text COLLATE "default" NOT NULL,
"info" text COLLATE "default" NOT NULL,
CONSTRAINT "tbl_users_pk" PRIMARY KEY ("id_user") 
)
WITHOUT OIDS;

ALTER TABLE "tbl_users" OWNER TO "postgres";


ALTER TABLE "tbl_apply" ADD CONSTRAINT "tbl_apply_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tbl_apply" ADD CONSTRAINT "tbl_apply_fk1" FOREIGN KEY ("id_job") REFERENCES "tbl_jobs" ("id_job") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_companies" ADD CONSTRAINT "fkel58ttb5w2r1nx8swbk7d6lr1" FOREIGN KEY ("id_company") REFERENCES "tbl_companies" ("id_company") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_company_comments" ADD CONSTRAINT "tbl_company_comments_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_company_comments" ADD CONSTRAINT "tbl_company_comments_fk1" FOREIGN KEY ("id_company") REFERENCES "tbl_companies" ("id_company") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_hrs" ADD CONSTRAINT "tbl_hrs_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tbl_hrs" ADD CONSTRAINT "tbl_hrs_fk1" FOREIGN KEY ("id_company") REFERENCES "tbl_companies" ("id_company") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_images" ADD CONSTRAINT "tbl_images_fk0" FOREIGN KEY ("id_job") REFERENCES "tbl_jobs" ("id_job") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_job_comments" ADD CONSTRAINT "tbl_job_comments_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tbl_job_comments" ADD CONSTRAINT "tbl_job_comments_fk1" FOREIGN KEY ("id_job") REFERENCES "tbl_jobs" ("id_job") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_jobs" ADD CONSTRAINT "tbl_jobs_fk0" FOREIGN KEY ("id_company") REFERENCES "tbl_companies" ("id_company") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_profiles" ADD CONSTRAINT "tbl_profiles_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "tbl_sessions" ADD CONSTRAINT "tbl_sessions_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tbl_user_rank" ADD CONSTRAINT "tbl_user_rank_fk0" FOREIGN KEY ("id_user") REFERENCES "tbl_users" ("id_user") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "tbl_user_rank" ADD CONSTRAINT "tbl_user_rank_fk1" FOREIGN KEY ("id_company") REFERENCES "tbl_companies" ("id_company") ON DELETE CASCADE ON UPDATE NO ACTION;

