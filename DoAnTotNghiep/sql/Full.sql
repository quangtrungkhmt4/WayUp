CREATE TABLE "pg_aggregate" (
"aggfnoid" regproc NOT NULL,
"aggkind" "char" NOT NULL,
"aggnumdirectargs" int2 NOT NULL,
"aggtransfn" regproc NOT NULL,
"aggfinalfn" regproc NOT NULL,
"aggmtransfn" regproc NOT NULL,
"aggminvtransfn" regproc NOT NULL,
"aggmfinalfn" regproc NOT NULL,
"aggfinalextra" bool NOT NULL,
"aggmfinalextra" bool NOT NULL,
"aggsortop" oid NOT NULL,
"aggtranstype" oid NOT NULL,
"aggtransspace" int4 NOT NULL,
"aggmtranstype" oid NOT NULL,
"aggmtransspace" int4 NOT NULL,
"agginitval" text COLLATE "default",
"aggminitval" text COLLATE "default"
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_aggregate_fnoid_index" ON "pg_aggregate" USING btree ("aggfnoid" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_aggregate" OWNER TO "postgres";

CREATE TABLE "pg_am" (
"amname" name NOT NULL,
"amstrategies" int2 NOT NULL,
"amsupport" int2 NOT NULL,
"amcanorder" bool NOT NULL,
"amcanorderbyop" bool NOT NULL,
"amcanbackward" bool NOT NULL,
"amcanunique" bool NOT NULL,
"amcanmulticol" bool NOT NULL,
"amoptionalkey" bool NOT NULL,
"amsearcharray" bool NOT NULL,
"amsearchnulls" bool NOT NULL,
"amstorage" bool NOT NULL,
"amclusterable" bool NOT NULL,
"ampredlocks" bool NOT NULL,
"amkeytype" oid NOT NULL,
"aminsert" regproc NOT NULL,
"ambeginscan" regproc NOT NULL,
"amgettuple" regproc NOT NULL,
"amgetbitmap" regproc NOT NULL,
"amrescan" regproc NOT NULL,
"amendscan" regproc NOT NULL,
"ammarkpos" regproc NOT NULL,
"amrestrpos" regproc NOT NULL,
"ambuild" regproc NOT NULL,
"ambuildempty" regproc NOT NULL,
"ambulkdelete" regproc NOT NULL,
"amvacuumcleanup" regproc NOT NULL,
"amcanreturn" regproc NOT NULL,
"amcostestimate" regproc NOT NULL,
"amoptions" regproc NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_am_name_index" ON "pg_am" USING btree ("amname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_am_oid_index" ON "pg_am" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_am" OWNER TO "postgres";

CREATE TABLE "pg_amop" (
"amopfamily" oid NOT NULL,
"amoplefttype" oid NOT NULL,
"amoprighttype" oid NOT NULL,
"amopstrategy" int2 NOT NULL,
"amoppurpose" "char" NOT NULL,
"amopopr" oid NOT NULL,
"amopmethod" oid NOT NULL,
"amopsortfamily" oid NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_amop_fam_strat_index" ON "pg_amop" USING btree ("amopfamily" "pg_catalog"."oid_ops" ASC NULLS LAST, "amoplefttype" "pg_catalog"."oid_ops" ASC NULLS LAST, "amoprighttype" "pg_catalog"."oid_ops" ASC NULLS LAST, "amopstrategy" "pg_catalog"."int2_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_amop_oid_index" ON "pg_amop" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_amop_opr_fam_index" ON "pg_amop" USING btree ("amopopr" "pg_catalog"."oid_ops" ASC NULLS LAST, "amoppurpose" "pg_catalog"."char_ops" ASC NULLS LAST, "amopfamily" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_amop" OWNER TO "postgres";

CREATE TABLE "pg_amproc" (
"amprocfamily" oid NOT NULL,
"amproclefttype" oid NOT NULL,
"amprocrighttype" oid NOT NULL,
"amprocnum" int2 NOT NULL,
"amproc" regproc NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_amproc_fam_proc_index" ON "pg_amproc" USING btree ("amprocfamily" "pg_catalog"."oid_ops" ASC NULLS LAST, "amproclefttype" "pg_catalog"."oid_ops" ASC NULLS LAST, "amprocrighttype" "pg_catalog"."oid_ops" ASC NULLS LAST, "amprocnum" "pg_catalog"."int2_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_amproc_oid_index" ON "pg_amproc" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_amproc" OWNER TO "postgres";

CREATE TABLE "pg_attrdef" (
"adrelid" oid NOT NULL,
"adnum" int2 NOT NULL,
"adbin" pg_node_tree,
"adsrc" text COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_attrdef_adrelid_adnum_index" ON "pg_attrdef" USING btree ("adrelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "adnum" "pg_catalog"."int2_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_attrdef_oid_index" ON "pg_attrdef" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_attrdef" OWNER TO "postgres";

CREATE TABLE "pg_attribute" (
"attrelid" oid NOT NULL,
"attname" name NOT NULL,
"atttypid" oid NOT NULL,
"attstattarget" int4 NOT NULL,
"attlen" int2 NOT NULL,
"attnum" int2 NOT NULL,
"attndims" int4 NOT NULL,
"attcacheoff" int4 NOT NULL,
"atttypmod" int4 NOT NULL,
"attbyval" bool NOT NULL,
"attstorage" "char" NOT NULL,
"attalign" "char" NOT NULL,
"attnotnull" bool NOT NULL,
"atthasdef" bool NOT NULL,
"attisdropped" bool NOT NULL,
"attislocal" bool NOT NULL,
"attinhcount" int4 NOT NULL,
"attcollation" oid NOT NULL,
"attacl" aclitem[],
"attoptions" text[] COLLATE "default",
"attfdwoptions" text[] COLLATE "default"
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_attribute_relid_attnam_index" ON "pg_attribute" USING btree ("attrelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "attname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_attribute_relid_attnum_index" ON "pg_attribute" USING btree ("attrelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "attnum" "pg_catalog"."int2_ops" ASC NULLS LAST);
ALTER TABLE "pg_attribute" OWNER TO "postgres";

CREATE TABLE "pg_auth_members" (
"roleid" oid NOT NULL,
"member" oid NOT NULL,
"grantor" oid NOT NULL,
"admin_option" bool NOT NULL
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_auth_members_member_role_index" ON "pg_auth_members" USING btree ("member" "pg_catalog"."oid_ops" ASC NULLS LAST, "roleid" "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
CREATE UNIQUE INDEX "pg_auth_members_role_member_index" ON "pg_auth_members" USING btree ("roleid" "pg_catalog"."oid_ops" ASC NULLS LAST, "member" "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_auth_members" OWNER TO "postgres";

CREATE TABLE "pg_authid" (
"rolname" name NOT NULL,
"rolsuper" bool NOT NULL,
"rolinherit" bool NOT NULL,
"rolcreaterole" bool NOT NULL,
"rolcreatedb" bool NOT NULL,
"rolcatupdate" bool NOT NULL,
"rolcanlogin" bool NOT NULL,
"rolreplication" bool NOT NULL,
"rolconnlimit" int4 NOT NULL,
"rolpassword" text COLLATE "default",
"rolvaliduntil" timestamptz(6)
)
WITH OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_authid_oid_index" ON "pg_authid" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
CREATE UNIQUE INDEX "pg_authid_rolname_index" ON "pg_authid" USING btree ("rolname" "pg_catalog"."name_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_authid" OWNER TO "postgres";

CREATE TABLE "pg_cast" (
"castsource" oid NOT NULL,
"casttarget" oid NOT NULL,
"castfunc" oid NOT NULL,
"castcontext" "char" NOT NULL,
"castmethod" "char" NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_cast_oid_index" ON "pg_cast" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_cast_source_target_index" ON "pg_cast" USING btree ("castsource" "pg_catalog"."oid_ops" ASC NULLS LAST, "casttarget" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_cast" OWNER TO "postgres";

CREATE TABLE "pg_class" (
"relname" name NOT NULL,
"relnamespace" oid NOT NULL,
"reltype" oid NOT NULL,
"reloftype" oid NOT NULL,
"relowner" oid NOT NULL,
"relam" oid NOT NULL,
"relfilenode" oid NOT NULL,
"reltablespace" oid NOT NULL,
"relpages" int4 NOT NULL,
"reltuples" float4 NOT NULL,
"relallvisible" int4 NOT NULL,
"reltoastrelid" oid NOT NULL,
"relhasindex" bool NOT NULL,
"relisshared" bool NOT NULL,
"relpersistence" "char" NOT NULL,
"relkind" "char" NOT NULL,
"relnatts" int2 NOT NULL,
"relchecks" int2 NOT NULL,
"relhasoids" bool NOT NULL,
"relhaspkey" bool NOT NULL,
"relhasrules" bool NOT NULL,
"relhastriggers" bool NOT NULL,
"relhassubclass" bool NOT NULL,
"relispopulated" bool NOT NULL,
"relreplident" "char" NOT NULL,
"relfrozenxid" xid NOT NULL,
"relminmxid" xid NOT NULL,
"relacl" aclitem[],
"reloptions" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_class_oid_index" ON "pg_class" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_class_relname_nsp_index" ON "pg_class" USING btree ("relname" "pg_catalog"."name_ops" ASC NULLS LAST, "relnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE INDEX "pg_class_tblspc_relfilenode_index" ON "pg_class" USING btree ("reltablespace" "pg_catalog"."oid_ops" ASC NULLS LAST, "relfilenode" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_class" OWNER TO "postgres";

CREATE TABLE "pg_collation" (
"collname" name NOT NULL,
"collnamespace" oid NOT NULL,
"collowner" oid NOT NULL,
"collencoding" int4 NOT NULL,
"collcollate" name NOT NULL,
"collctype" name NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_collation_name_enc_nsp_index" ON "pg_collation" USING btree ("collname" "pg_catalog"."name_ops" ASC NULLS LAST, "collencoding" "pg_catalog"."int4_ops" ASC NULLS LAST, "collnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_collation_oid_index" ON "pg_collation" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_collation" OWNER TO "postgres";

CREATE TABLE "pg_constraint" (
"conname" name NOT NULL,
"connamespace" oid NOT NULL,
"contype" "char" NOT NULL,
"condeferrable" bool NOT NULL,
"condeferred" bool NOT NULL,
"convalidated" bool NOT NULL,
"conrelid" oid NOT NULL,
"contypid" oid NOT NULL,
"conindid" oid NOT NULL,
"confrelid" oid NOT NULL,
"confupdtype" "char" NOT NULL,
"confdeltype" "char" NOT NULL,
"confmatchtype" "char" NOT NULL,
"conislocal" bool NOT NULL,
"coninhcount" int4 NOT NULL,
"connoinherit" bool NOT NULL,
"conkey" int2[],
"confkey" int2[],
"conpfeqop" oid[],
"conppeqop" oid[],
"conffeqop" oid[],
"conexclop" oid[],
"conbin" pg_node_tree,
"consrc" text COLLATE "default"
)
WITH OIDS;

CREATE INDEX "pg_constraint_conname_nsp_index" ON "pg_constraint" USING btree ("conname" "pg_catalog"."name_ops" ASC NULLS LAST, "connamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE INDEX "pg_constraint_conrelid_index" ON "pg_constraint" USING btree ("conrelid" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE INDEX "pg_constraint_contypid_index" ON "pg_constraint" USING btree ("contypid" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_constraint_oid_index" ON "pg_constraint" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_constraint" OWNER TO "postgres";

CREATE TABLE "pg_conversion" (
"conname" name NOT NULL,
"connamespace" oid NOT NULL,
"conowner" oid NOT NULL,
"conforencoding" int4 NOT NULL,
"contoencoding" int4 NOT NULL,
"conproc" regproc NOT NULL,
"condefault" bool NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_conversion_default_index" ON "pg_conversion" USING btree ("connamespace" "pg_catalog"."oid_ops" ASC NULLS LAST, "conforencoding" "pg_catalog"."int4_ops" ASC NULLS LAST, "contoencoding" "pg_catalog"."int4_ops" ASC NULLS LAST, oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_conversion_name_nsp_index" ON "pg_conversion" USING btree ("conname" "pg_catalog"."name_ops" ASC NULLS LAST, "connamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_conversion_oid_index" ON "pg_conversion" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_conversion" OWNER TO "postgres";

CREATE TABLE "pg_database" (
"datname" name NOT NULL,
"datdba" oid NOT NULL,
"encoding" int4 NOT NULL,
"datcollate" name NOT NULL,
"datctype" name NOT NULL,
"datistemplate" bool NOT NULL,
"datallowconn" bool NOT NULL,
"datconnlimit" int4 NOT NULL,
"datlastsysoid" oid NOT NULL,
"datfrozenxid" xid NOT NULL,
"datminmxid" xid NOT NULL,
"dattablespace" oid NOT NULL,
"datacl" aclitem[]
)
WITH OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_database_datname_index" ON "pg_database" USING btree ("datname" "pg_catalog"."name_ops" ASC NULLS LAST) TABLESPACE "pg_global";
CREATE UNIQUE INDEX "pg_database_oid_index" ON "pg_database" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_database" OWNER TO "postgres";

CREATE TABLE "pg_db_role_setting" (
"setdatabase" oid NOT NULL,
"setrole" oid NOT NULL,
"setconfig" text[] COLLATE "default"
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_db_role_setting_databaseid_rol_index" ON "pg_db_role_setting" USING btree ("setdatabase" "pg_catalog"."oid_ops" ASC NULLS LAST, "setrole" "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_db_role_setting" OWNER TO "postgres";

CREATE TABLE "pg_default_acl" (
"defaclrole" oid NOT NULL,
"defaclnamespace" oid NOT NULL,
"defaclobjtype" "char" NOT NULL,
"defaclacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_default_acl_oid_index" ON "pg_default_acl" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_default_acl_role_nsp_obj_index" ON "pg_default_acl" USING btree ("defaclrole" "pg_catalog"."oid_ops" ASC NULLS LAST, "defaclnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST, "defaclobjtype" "pg_catalog"."char_ops" ASC NULLS LAST);
ALTER TABLE "pg_default_acl" OWNER TO "postgres";

CREATE TABLE "pg_depend" (
"classid" oid NOT NULL,
"objid" oid NOT NULL,
"objsubid" int4 NOT NULL,
"refclassid" oid NOT NULL,
"refobjid" oid NOT NULL,
"refobjsubid" int4 NOT NULL,
"deptype" "char" NOT NULL
)
WITHOUT OIDS;

CREATE INDEX "pg_depend_depender_index" ON "pg_depend" USING btree ("classid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objsubid" "pg_catalog"."int4_ops" ASC NULLS LAST);
CREATE INDEX "pg_depend_reference_index" ON "pg_depend" USING btree ("refclassid" "pg_catalog"."oid_ops" ASC NULLS LAST, "refobjid" "pg_catalog"."oid_ops" ASC NULLS LAST, "refobjsubid" "pg_catalog"."int4_ops" ASC NULLS LAST);
ALTER TABLE "pg_depend" OWNER TO "postgres";

CREATE TABLE "pg_description" (
"objoid" oid NOT NULL,
"classoid" oid NOT NULL,
"objsubid" int4 NOT NULL,
"description" text COLLATE "default"
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_description_o_c_o_index" ON "pg_description" USING btree ("objoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "classoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objsubid" "pg_catalog"."int4_ops" ASC NULLS LAST);
ALTER TABLE "pg_description" OWNER TO "postgres";

CREATE TABLE "pg_enum" (
"enumtypid" oid NOT NULL,
"enumsortorder" float4 NOT NULL,
"enumlabel" name NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_enum_oid_index" ON "pg_enum" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_enum_typid_label_index" ON "pg_enum" USING btree ("enumtypid" "pg_catalog"."oid_ops" ASC NULLS LAST, "enumlabel" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_enum_typid_sortorder_index" ON "pg_enum" USING btree ("enumtypid" "pg_catalog"."oid_ops" ASC NULLS LAST, "enumsortorder" "pg_catalog"."float4_ops" ASC NULLS LAST);
ALTER TABLE "pg_enum" OWNER TO "postgres";

CREATE TABLE "pg_event_trigger" (
"evtname" name NOT NULL,
"evtevent" name NOT NULL,
"evtowner" oid NOT NULL,
"evtfoid" oid NOT NULL,
"evtenabled" "char" NOT NULL,
"evttags" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_event_trigger_evtname_index" ON "pg_event_trigger" USING btree ("evtname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_event_trigger_oid_index" ON "pg_event_trigger" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_event_trigger" OWNER TO "postgres";

CREATE TABLE "pg_extension" (
"extname" name NOT NULL,
"extowner" oid NOT NULL,
"extnamespace" oid NOT NULL,
"extrelocatable" bool NOT NULL,
"extversion" text COLLATE "default",
"extconfig" oid[],
"extcondition" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_extension_name_index" ON "pg_extension" USING btree ("extname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_extension_oid_index" ON "pg_extension" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_extension" OWNER TO "postgres";

CREATE TABLE "pg_foreign_data_wrapper" (
"fdwname" name NOT NULL,
"fdwowner" oid NOT NULL,
"fdwhandler" oid NOT NULL,
"fdwvalidator" oid NOT NULL,
"fdwacl" aclitem[],
"fdwoptions" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_foreign_data_wrapper_name_index" ON "pg_foreign_data_wrapper" USING btree ("fdwname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_foreign_data_wrapper_oid_index" ON "pg_foreign_data_wrapper" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_foreign_data_wrapper" OWNER TO "postgres";

CREATE TABLE "pg_foreign_server" (
"srvname" name NOT NULL,
"srvowner" oid NOT NULL,
"srvfdw" oid NOT NULL,
"srvtype" text COLLATE "default",
"srvversion" text COLLATE "default",
"srvacl" aclitem[],
"srvoptions" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_foreign_server_name_index" ON "pg_foreign_server" USING btree ("srvname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_foreign_server_oid_index" ON "pg_foreign_server" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_foreign_server" OWNER TO "postgres";

CREATE TABLE "pg_foreign_table" (
"ftrelid" oid NOT NULL,
"ftserver" oid NOT NULL,
"ftoptions" text[] COLLATE "default"
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_foreign_table_relid_index" ON "pg_foreign_table" USING btree ("ftrelid" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_foreign_table" OWNER TO "postgres";

CREATE TABLE "pg_index" (
"indexrelid" oid NOT NULL,
"indrelid" oid NOT NULL,
"indnatts" int2 NOT NULL,
"indisunique" bool NOT NULL,
"indisprimary" bool NOT NULL,
"indisexclusion" bool NOT NULL,
"indimmediate" bool NOT NULL,
"indisclustered" bool NOT NULL,
"indisvalid" bool NOT NULL,
"indcheckxmin" bool NOT NULL,
"indisready" bool NOT NULL,
"indislive" bool NOT NULL,
"indisreplident" bool NOT NULL,
"indkey" int2[] NOT NULL,
"indcollation" oid[] NOT NULL,
"indclass" oid[] NOT NULL,
"indoption" int2[] NOT NULL,
"indexprs" pg_node_tree,
"indpred" pg_node_tree
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_index_indexrelid_index" ON "pg_index" USING btree ("indexrelid" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE INDEX "pg_index_indrelid_index" ON "pg_index" USING btree ("indrelid" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_index" OWNER TO "postgres";

CREATE TABLE "pg_inherits" (
"inhrelid" oid NOT NULL,
"inhparent" oid NOT NULL,
"inhseqno" int4 NOT NULL
)
WITHOUT OIDS;

CREATE INDEX "pg_inherits_parent_index" ON "pg_inherits" USING btree ("inhparent" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_inherits_relid_seqno_index" ON "pg_inherits" USING btree ("inhrelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "inhseqno" "pg_catalog"."int4_ops" ASC NULLS LAST);
ALTER TABLE "pg_inherits" OWNER TO "postgres";

CREATE TABLE "pg_language" (
"lanname" name NOT NULL,
"lanowner" oid NOT NULL,
"lanispl" bool NOT NULL,
"lanpltrusted" bool NOT NULL,
"lanplcallfoid" oid NOT NULL,
"laninline" oid NOT NULL,
"lanvalidator" oid NOT NULL,
"lanacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_language_name_index" ON "pg_language" USING btree ("lanname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_language_oid_index" ON "pg_language" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_language" OWNER TO "postgres";

CREATE TABLE "pg_largeobject" (
"loid" oid NOT NULL,
"pageno" int4 NOT NULL,
"data" bytea
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_largeobject_loid_pn_index" ON "pg_largeobject" USING btree ("loid" "pg_catalog"."oid_ops" ASC NULLS LAST, "pageno" "pg_catalog"."int4_ops" ASC NULLS LAST);
ALTER TABLE "pg_largeobject" OWNER TO "postgres";

CREATE TABLE "pg_largeobject_metadata" (
"lomowner" oid NOT NULL,
"lomacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_largeobject_metadata_oid_index" ON "pg_largeobject_metadata" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_largeobject_metadata" OWNER TO "postgres";

CREATE TABLE "pg_namespace" (
"nspname" name NOT NULL,
"nspowner" oid NOT NULL,
"nspacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_namespace_nspname_index" ON "pg_namespace" USING btree ("nspname" "pg_catalog"."name_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_namespace_oid_index" ON "pg_namespace" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_namespace" OWNER TO "postgres";

CREATE TABLE "pg_opclass" (
"opcmethod" oid NOT NULL,
"opcname" name NOT NULL,
"opcnamespace" oid NOT NULL,
"opcowner" oid NOT NULL,
"opcfamily" oid NOT NULL,
"opcintype" oid NOT NULL,
"opcdefault" bool NOT NULL,
"opckeytype" oid NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_opclass_am_name_nsp_index" ON "pg_opclass" USING btree ("opcmethod" "pg_catalog"."oid_ops" ASC NULLS LAST, "opcname" "pg_catalog"."name_ops" ASC NULLS LAST, "opcnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_opclass_oid_index" ON "pg_opclass" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_opclass" OWNER TO "postgres";

CREATE TABLE "pg_operator" (
"oprname" name NOT NULL,
"oprnamespace" oid NOT NULL,
"oprowner" oid NOT NULL,
"oprkind" "char" NOT NULL,
"oprcanmerge" bool NOT NULL,
"oprcanhash" bool NOT NULL,
"oprleft" oid NOT NULL,
"oprright" oid NOT NULL,
"oprresult" oid NOT NULL,
"oprcom" oid NOT NULL,
"oprnegate" oid NOT NULL,
"oprcode" regproc NOT NULL,
"oprrest" regproc NOT NULL,
"oprjoin" regproc NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_operator_oid_index" ON "pg_operator" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_operator_oprname_l_r_n_index" ON "pg_operator" USING btree ("oprname" "pg_catalog"."name_ops" ASC NULLS LAST, "oprleft" "pg_catalog"."oid_ops" ASC NULLS LAST, "oprright" "pg_catalog"."oid_ops" ASC NULLS LAST, "oprnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_operator" OWNER TO "postgres";

CREATE TABLE "pg_opfamily" (
"opfmethod" oid NOT NULL,
"opfname" name NOT NULL,
"opfnamespace" oid NOT NULL,
"opfowner" oid NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_opfamily_am_name_nsp_index" ON "pg_opfamily" USING btree ("opfmethod" "pg_catalog"."oid_ops" ASC NULLS LAST, "opfname" "pg_catalog"."name_ops" ASC NULLS LAST, "opfnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_opfamily_oid_index" ON "pg_opfamily" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_opfamily" OWNER TO "postgres";

CREATE TABLE "pg_pltemplate" (
"tmplname" name NOT NULL,
"tmpltrusted" bool NOT NULL,
"tmpldbacreate" bool NOT NULL,
"tmplhandler" text COLLATE "default",
"tmplinline" text COLLATE "default",
"tmplvalidator" text COLLATE "default",
"tmpllibrary" text COLLATE "default",
"tmplacl" aclitem[]
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_pltemplate_name_index" ON "pg_pltemplate" USING btree ("tmplname" "pg_catalog"."name_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_pltemplate" OWNER TO "postgres";

CREATE TABLE "pg_proc" (
"proname" name NOT NULL,
"pronamespace" oid NOT NULL,
"proowner" oid NOT NULL,
"prolang" oid NOT NULL,
"procost" float4 NOT NULL,
"prorows" float4 NOT NULL,
"provariadic" oid NOT NULL,
"protransform" regproc NOT NULL,
"proisagg" bool NOT NULL,
"proiswindow" bool NOT NULL,
"prosecdef" bool NOT NULL,
"proleakproof" bool NOT NULL,
"proisstrict" bool NOT NULL,
"proretset" bool NOT NULL,
"provolatile" "char" NOT NULL,
"pronargs" int2 NOT NULL,
"pronargdefaults" int2 NOT NULL,
"prorettype" oid NOT NULL,
"proargtypes" oid[] NOT NULL,
"proallargtypes" oid[],
"proargmodes" "char"[],
"proargnames" text[] COLLATE "default",
"proargdefaults" pg_node_tree,
"prosrc" text COLLATE "default",
"probin" text COLLATE "default",
"proconfig" text[] COLLATE "default",
"proacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_proc_oid_index" ON "pg_proc" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_proc_proname_args_nsp_index" ON "pg_proc" USING btree ("proname" "pg_catalog"."name_ops" ASC NULLS LAST, "proargtypes" "pg_catalog"."oidvector_ops" ASC NULLS LAST, "pronamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_proc" OWNER TO "postgres";

CREATE TABLE "pg_range" (
"rngtypid" oid NOT NULL,
"rngsubtype" oid NOT NULL,
"rngcollation" oid NOT NULL,
"rngsubopc" oid NOT NULL,
"rngcanonical" regproc NOT NULL,
"rngsubdiff" regproc NOT NULL
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_range_rngtypid_index" ON "pg_range" USING btree ("rngtypid" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_range" OWNER TO "postgres";

CREATE TABLE "pg_rewrite" (
"rulename" name NOT NULL,
"ev_class" oid NOT NULL,
"ev_type" "char" NOT NULL,
"ev_enabled" "char" NOT NULL,
"is_instead" bool NOT NULL,
"ev_qual" pg_node_tree,
"ev_action" pg_node_tree
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_rewrite_oid_index" ON "pg_rewrite" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_rewrite_rel_rulename_index" ON "pg_rewrite" USING btree ("ev_class" "pg_catalog"."oid_ops" ASC NULLS LAST, "rulename" "pg_catalog"."name_ops" ASC NULLS LAST);
ALTER TABLE "pg_rewrite" OWNER TO "postgres";

CREATE TABLE "pg_seclabel" (
"objoid" oid NOT NULL,
"classoid" oid NOT NULL,
"objsubid" int4 NOT NULL,
"provider" text COLLATE "default",
"label" text COLLATE "default"
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_seclabel_object_index" ON "pg_seclabel" USING btree ("objoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "classoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objsubid" "pg_catalog"."int4_ops" ASC NULLS LAST, "provider" "pg_catalog"."text_ops" ASC NULLS LAST);
ALTER TABLE "pg_seclabel" OWNER TO "postgres";

CREATE TABLE "pg_shdepend" (
"dbid" oid NOT NULL,
"classid" oid NOT NULL,
"objid" oid NOT NULL,
"objsubid" int4 NOT NULL,
"refclassid" oid NOT NULL,
"refobjid" oid NOT NULL,
"deptype" "char" NOT NULL
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE INDEX "pg_shdepend_depender_index" ON "pg_shdepend" USING btree ("dbid" "pg_catalog"."oid_ops" ASC NULLS LAST, "classid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objid" "pg_catalog"."oid_ops" ASC NULLS LAST, "objsubid" "pg_catalog"."int4_ops" ASC NULLS LAST) TABLESPACE "pg_global";
CREATE INDEX "pg_shdepend_reference_index" ON "pg_shdepend" USING btree ("refclassid" "pg_catalog"."oid_ops" ASC NULLS LAST, "refobjid" "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_shdepend" OWNER TO "postgres";

CREATE TABLE "pg_shdescription" (
"objoid" oid NOT NULL,
"classoid" oid NOT NULL,
"description" text COLLATE "default"
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_shdescription_o_c_index" ON "pg_shdescription" USING btree ("objoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "classoid" "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_shdescription" OWNER TO "postgres";

CREATE TABLE "pg_shseclabel" (
"objoid" oid NOT NULL,
"classoid" oid NOT NULL,
"provider" text COLLATE "default",
"label" text COLLATE "default"
)
WITHOUT OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_shseclabel_object_index" ON "pg_shseclabel" USING btree ("objoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "classoid" "pg_catalog"."oid_ops" ASC NULLS LAST, "provider" "pg_catalog"."text_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_shseclabel" OWNER TO "postgres";

CREATE TABLE "pg_statistic" (
"starelid" oid NOT NULL,
"staattnum" int2 NOT NULL,
"stainherit" bool NOT NULL,
"stanullfrac" float4 NOT NULL,
"stawidth" int4 NOT NULL,
"stadistinct" float4 NOT NULL,
"stakind1" int2 NOT NULL,
"stakind2" int2 NOT NULL,
"stakind3" int2 NOT NULL,
"stakind4" int2 NOT NULL,
"stakind5" int2 NOT NULL,
"staop1" oid NOT NULL,
"staop2" oid NOT NULL,
"staop3" oid NOT NULL,
"staop4" oid NOT NULL,
"staop5" oid NOT NULL,
"stanumbers1" float4[],
"stanumbers2" float4[],
"stanumbers3" float4[],
"stanumbers4" float4[],
"stanumbers5" float4[],
"stavalues1" anyarray,
"stavalues2" anyarray,
"stavalues3" anyarray,
"stavalues4" anyarray,
"stavalues5" anyarray
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_statistic_relid_att_inh_index" ON "pg_statistic" USING btree ("starelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "staattnum" "pg_catalog"."int2_ops" ASC NULLS LAST, "stainherit" "pg_catalog"."bool_ops" ASC NULLS LAST);
ALTER TABLE "pg_statistic" OWNER TO "postgres";

CREATE TABLE "pg_tablespace" (
"spcname" name NOT NULL,
"spcowner" oid NOT NULL,
"spcacl" aclitem[],
"spcoptions" text[] COLLATE "default"
)
WITH OIDS
TABLESPACE pg_global;

CREATE UNIQUE INDEX "pg_tablespace_oid_index" ON "pg_tablespace" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST) TABLESPACE "pg_global";
CREATE UNIQUE INDEX "pg_tablespace_spcname_index" ON "pg_tablespace" USING btree ("spcname" "pg_catalog"."name_ops" ASC NULLS LAST) TABLESPACE "pg_global";
ALTER TABLE "pg_tablespace" OWNER TO "postgres";

CREATE TABLE "pg_trigger" (
"tgrelid" oid NOT NULL,
"tgname" name NOT NULL,
"tgfoid" oid NOT NULL,
"tgtype" int2 NOT NULL,
"tgenabled" "char" NOT NULL,
"tgisinternal" bool NOT NULL,
"tgconstrrelid" oid NOT NULL,
"tgconstrindid" oid NOT NULL,
"tgconstraint" oid NOT NULL,
"tgdeferrable" bool NOT NULL,
"tginitdeferred" bool NOT NULL,
"tgnargs" int2 NOT NULL,
"tgattr" int2[] NOT NULL,
"tgargs" bytea,
"tgqual" pg_node_tree
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_trigger_oid_index" ON "pg_trigger" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE INDEX "pg_trigger_tgconstraint_index" ON "pg_trigger" USING btree ("tgconstraint" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_trigger_tgrelid_tgname_index" ON "pg_trigger" USING btree ("tgrelid" "pg_catalog"."oid_ops" ASC NULLS LAST, "tgname" "pg_catalog"."name_ops" ASC NULLS LAST);
ALTER TABLE "pg_trigger" OWNER TO "postgres";

CREATE TABLE "pg_ts_config" (
"cfgname" name NOT NULL,
"cfgnamespace" oid NOT NULL,
"cfgowner" oid NOT NULL,
"cfgparser" oid NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_ts_config_cfgname_index" ON "pg_ts_config" USING btree ("cfgname" "pg_catalog"."name_ops" ASC NULLS LAST, "cfgnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_ts_config_oid_index" ON "pg_ts_config" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_ts_config" OWNER TO "postgres";

CREATE TABLE "pg_ts_config_map" (
"mapcfg" oid NOT NULL,
"maptokentype" int4 NOT NULL,
"mapseqno" int4 NOT NULL,
"mapdict" oid NOT NULL
)
WITHOUT OIDS;

CREATE UNIQUE INDEX "pg_ts_config_map_index" ON "pg_ts_config_map" USING btree ("mapcfg" "pg_catalog"."oid_ops" ASC NULLS LAST, "maptokentype" "pg_catalog"."int4_ops" ASC NULLS LAST, "mapseqno" "pg_catalog"."int4_ops" ASC NULLS LAST);
ALTER TABLE "pg_ts_config_map" OWNER TO "postgres";

CREATE TABLE "pg_ts_dict" (
"dictname" name NOT NULL,
"dictnamespace" oid NOT NULL,
"dictowner" oid NOT NULL,
"dicttemplate" oid NOT NULL,
"dictinitoption" text COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_ts_dict_dictname_index" ON "pg_ts_dict" USING btree ("dictname" "pg_catalog"."name_ops" ASC NULLS LAST, "dictnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_ts_dict_oid_index" ON "pg_ts_dict" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_ts_dict" OWNER TO "postgres";

CREATE TABLE "pg_ts_parser" (
"prsname" name NOT NULL,
"prsnamespace" oid NOT NULL,
"prsstart" regproc NOT NULL,
"prstoken" regproc NOT NULL,
"prsend" regproc NOT NULL,
"prsheadline" regproc NOT NULL,
"prslextype" regproc NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_ts_parser_oid_index" ON "pg_ts_parser" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_ts_parser_prsname_index" ON "pg_ts_parser" USING btree ("prsname" "pg_catalog"."name_ops" ASC NULLS LAST, "prsnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_ts_parser" OWNER TO "postgres";

CREATE TABLE "pg_ts_template" (
"tmplname" name NOT NULL,
"tmplnamespace" oid NOT NULL,
"tmplinit" regproc NOT NULL,
"tmpllexize" regproc NOT NULL
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_ts_template_oid_index" ON "pg_ts_template" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_ts_template_tmplname_index" ON "pg_ts_template" USING btree ("tmplname" "pg_catalog"."name_ops" ASC NULLS LAST, "tmplnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_ts_template" OWNER TO "postgres";

CREATE TABLE "pg_type" (
"typname" name NOT NULL,
"typnamespace" oid NOT NULL,
"typowner" oid NOT NULL,
"typlen" int2 NOT NULL,
"typbyval" bool NOT NULL,
"typtype" "char" NOT NULL,
"typcategory" "char" NOT NULL,
"typispreferred" bool NOT NULL,
"typisdefined" bool NOT NULL,
"typdelim" "char" NOT NULL,
"typrelid" oid NOT NULL,
"typelem" oid NOT NULL,
"typarray" oid NOT NULL,
"typinput" regproc NOT NULL,
"typoutput" regproc NOT NULL,
"typreceive" regproc NOT NULL,
"typsend" regproc NOT NULL,
"typmodin" regproc NOT NULL,
"typmodout" regproc NOT NULL,
"typanalyze" regproc NOT NULL,
"typalign" "char" NOT NULL,
"typstorage" "char" NOT NULL,
"typnotnull" bool NOT NULL,
"typbasetype" oid NOT NULL,
"typtypmod" int4 NOT NULL,
"typndims" int4 NOT NULL,
"typcollation" oid NOT NULL,
"typdefaultbin" pg_node_tree,
"typdefault" text COLLATE "default",
"typacl" aclitem[]
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_type_oid_index" ON "pg_type" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_type_typname_nsp_index" ON "pg_type" USING btree ("typname" "pg_catalog"."name_ops" ASC NULLS LAST, "typnamespace" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_type" OWNER TO "postgres";

CREATE TABLE "pg_user_mapping" (
"umuser" oid NOT NULL,
"umserver" oid NOT NULL,
"umoptions" text[] COLLATE "default"
)
WITH OIDS;

CREATE UNIQUE INDEX "pg_user_mapping_oid_index" ON "pg_user_mapping" USING btree (oid "pg_catalog"."oid_ops" ASC NULLS LAST);
CREATE UNIQUE INDEX "pg_user_mapping_user_server_index" ON "pg_user_mapping" USING btree ("umuser" "pg_catalog"."oid_ops" ASC NULLS LAST, "umserver" "pg_catalog"."oid_ops" ASC NULLS LAST);
ALTER TABLE "pg_user_mapping" OWNER TO "postgres";

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

CREATE TABLE "sql_features" (
"feature_id" "information_schema"."character_data",
"feature_name" "information_schema"."character_data",
"sub_feature_id" "information_schema"."character_data",
"sub_feature_name" "information_schema"."character_data",
"is_supported" "information_schema"."yes_or_no",
"is_verified_by" "information_schema"."character_data",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_features" OWNER TO "postgres";

CREATE TABLE "sql_implementation_info" (
"implementation_info_id" "information_schema"."character_data",
"implementation_info_name" "information_schema"."character_data",
"integer_value" "information_schema"."cardinal_number",
"character_value" "information_schema"."character_data",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_implementation_info" OWNER TO "postgres";

CREATE TABLE "sql_languages" (
"sql_language_source" "information_schema"."character_data",
"sql_language_year" "information_schema"."character_data",
"sql_language_conformance" "information_schema"."character_data",
"sql_language_integrity" "information_schema"."character_data",
"sql_language_implementation" "information_schema"."character_data",
"sql_language_binding_style" "information_schema"."character_data",
"sql_language_programming_language" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_languages" OWNER TO "postgres";

CREATE TABLE "sql_packages" (
"feature_id" "information_schema"."character_data",
"feature_name" "information_schema"."character_data",
"is_supported" "information_schema"."yes_or_no",
"is_verified_by" "information_schema"."character_data",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_packages" OWNER TO "postgres";

CREATE TABLE "sql_parts" (
"feature_id" "information_schema"."character_data",
"feature_name" "information_schema"."character_data",
"is_supported" "information_schema"."yes_or_no",
"is_verified_by" "information_schema"."character_data",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_parts" OWNER TO "postgres";

CREATE TABLE "sql_sizing" (
"sizing_id" "information_schema"."cardinal_number",
"sizing_name" "information_schema"."character_data",
"supported_value" "information_schema"."cardinal_number",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_sizing" OWNER TO "postgres";

CREATE TABLE "sql_sizing_profiles" (
"sizing_id" "information_schema"."cardinal_number",
"sizing_name" "information_schema"."character_data",
"profile_id" "information_schema"."character_data",
"required_value" "information_schema"."cardinal_number",
"comments" "information_schema"."character_data"
)
WITHOUT OIDS;

ALTER TABLE "sql_sizing_profiles" OWNER TO "postgres";


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

